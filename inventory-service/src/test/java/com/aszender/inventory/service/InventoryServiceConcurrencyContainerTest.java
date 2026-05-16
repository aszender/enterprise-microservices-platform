package com.aszender.inventory.service;

import com.aszender.inventory.model.ReservationStatus;
import com.aszender.inventory.model.StockItem;
import com.aszender.inventory.model.StockReservation;
import com.aszender.inventory.repository.StockItemRepository;
import com.aszender.inventory.repository.StockReservationRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Same scenarios as {@link InventoryServiceConcurrencyTest} but running against a real
 * PostgreSQL instance via Testcontainers, proving that the atomic UPDATE + optimistic-lock
 * oversell guard holds under genuine MVCC concurrency.
 */
@SpringBootTest(properties = {
        "app.grpc.server.enabled=false",
        "spring.jpa.show-sql=false"
})
@Testcontainers(disabledWithoutDocker = true)
class InventoryServiceConcurrencyContainerTest {

    @Container
    static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    @DynamicPropertySource
    static void overrideDataSource(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private StockItemRepository stockItemRepository;

    @Autowired
    private StockReservationRepository stockReservationRepository;

    @BeforeEach
    void setUp() {
        stockReservationRepository.deleteAll();
        stockItemRepository.deleteAll();
        stockReservationRepository.flush();
        stockItemRepository.flush();
    }

    @AfterEach
    void tearDown() {
        stockReservationRepository.deleteAll();
        stockItemRepository.deleteAll();
    }

    @Test
    void twoConcurrentReservationsDoNotOversell() throws Exception {
        stockItemRepository.saveAndFlush(new StockItem(200L, 10));

        ExecutorService executor = Executors.newFixedThreadPool(2);
        CountDownLatch ready = new CountDownLatch(2);
        CountDownLatch start = new CountDownLatch(1);

        try {
            Future<Boolean> first = executor.submit(reserveAtSameTime(1L, 200L, 7, ready, start));
            Future<Boolean> second = executor.submit(reserveAtSameTime(2L, 200L, 7, ready, start));

            assertThat(ready.await(5, TimeUnit.SECONDS)).isTrue();
            start.countDown();

            assertThat(List.of(first.get(5, TimeUnit.SECONDS), second.get(5, TimeUnit.SECONDS)))
                    .containsExactlyInAnyOrder(true, false);
        } finally {
            executor.shutdownNow();
        }

        StockItem stockItem = stockItemRepository.findByProductId(200L).orElseThrow();
        assertThat(stockItem.getAvailable()).isEqualTo(3);
        assertThat(stockItem.getReserved()).isEqualTo(7);
        assertThat(stockItem.getVersion()).isEqualTo(1);
        assertThat(stockReservationRepository.findAll())
                .extracting(StockReservation::getOrderId)
                .hasSize(1);
    }

    @Test
    void duplicateReserveIsIdempotent() {
        stockItemRepository.saveAndFlush(new StockItem(201L, 10));

        boolean first = inventoryService.reserveStock(10L, List.of(new InventoryService.ReservationLine(201L, 4)));
        boolean duplicate = inventoryService.reserveStock(10L, List.of(new InventoryService.ReservationLine(201L, 4)));

        assertThat(first).isTrue();
        assertThat(duplicate).isTrue();

        StockItem stockItem = stockItemRepository.findByProductId(201L).orElseThrow();
        assertThat(stockItem.getAvailable()).isEqualTo(6);
        assertThat(stockItem.getReserved()).isEqualTo(4);

        StockReservation reservation = stockReservationRepository.findByOrderIdWithItems(10L).orElseThrow();
        assertThat(reservation.getStatus()).isEqualTo(ReservationStatus.RESERVED);
        assertThat(reservation.getItems()).hasSize(1);
    }

    @Test
    void insufficientStockDoesNotLeavePartialMutation() {
        stockItemRepository.saveAllAndFlush(List.of(
                new StockItem(202L, 10),
                new StockItem(203L, 2)
        ));

        boolean reserved = inventoryService.reserveStock(11L, List.of(
                new InventoryService.ReservationLine(202L, 4),
                new InventoryService.ReservationLine(203L, 3)
        ));

        assertThat(reserved).isFalse();
        assertStock(202L, 10, 0, 0);
        assertStock(203L, 2, 0, 0);
        assertThat(stockReservationRepository.findByOrderId(11L)).isEmpty();
    }

    @Test
    void releaseRestoresReservedStockOnce() {
        stockItemRepository.saveAndFlush(new StockItem(204L, 10));
        inventoryService.reserveStock(12L, List.of(new InventoryService.ReservationLine(204L, 4)));

        boolean released = inventoryService.releaseReservation(12L);

        assertThat(released).isTrue();
        assertStock(204L, 10, 0, 2);
        assertThat(stockReservationRepository.findByOrderId(12L).orElseThrow().getStatus())
                .isEqualTo(ReservationStatus.RELEASED);
    }

    @Test
    void duplicateReleaseIsSafe() {
        stockItemRepository.saveAndFlush(new StockItem(205L, 10));
        inventoryService.reserveStock(13L, List.of(new InventoryService.ReservationLine(205L, 4)));

        boolean first = inventoryService.releaseReservation(13L);
        boolean duplicate = inventoryService.releaseReservation(13L);

        assertThat(first).isTrue();
        assertThat(duplicate).isTrue();
        assertStock(205L, 10, 0, 2);
        assertThat(stockReservationRepository.findByOrderId(13L).orElseThrow().getStatus())
                .isEqualTo(ReservationStatus.RELEASED);
    }

    private Callable<Boolean> reserveAtSameTime(
            Long orderId,
            Long productId,
            int quantity,
            CountDownLatch ready,
            CountDownLatch start
    ) {
        return () -> {
            ready.countDown();
            assertThat(start.await(5, TimeUnit.SECONDS)).isTrue();
            return inventoryService.reserveStock(orderId, List.of(new InventoryService.ReservationLine(productId, quantity)));
        };
    }

    private void assertStock(Long productId, int available, int reserved, long version) {
        StockItem stockItem = stockItemRepository.findByProductId(productId).orElseThrow();
        assertThat(stockItem.getAvailable()).isEqualTo(available);
        assertThat(stockItem.getReserved()).isEqualTo(reserved);
        assertThat(stockItem.getVersion()).isEqualTo(version);
    }
}
