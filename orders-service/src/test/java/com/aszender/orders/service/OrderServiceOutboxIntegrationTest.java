package com.aszender.orders.service;

import com.aszender.orders.dto.CreateOrderRequest;
import com.aszender.orders.dto.OrderItemRequest;
import com.aszender.orders.inventory.GrpcInventoryClient;
import com.aszender.orders.kafka.outbox.model.OutboxEvent;
import com.aszender.orders.kafka.outbox.model.OutboxEventStatus;
import com.aszender.orders.kafka.outbox.repository.OutboxEventRepository;
import com.aszender.orders.model.Order;
import com.aszender.orders.model.OrderStatus;
import com.aszender.orders.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class OrderServiceOutboxIntegrationTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OutboxEventRepository outboxEventRepository;

    @MockitoBean
    private GrpcInventoryClient inventoryClient;

    @BeforeEach
    void cleanDatabase() {
        outboxEventRepository.deleteAll();
        orderRepository.deleteAll();
    }

    @Test
    void create_persistsOrderAndOutboxEventTogether() {
        Order saved = orderService.create(orderRequest());

        assertThat(orderRepository.findById(saved.getId())).isPresent();

        List<OutboxEvent> events = outboxEventRepository.findAll();
        assertThat(events).singleElement().satisfies(event -> {
            assertThat(event.getStatus()).isEqualTo(OutboxEventStatus.PENDING);
            assertThat(event.getEventType()).isEqualTo("orders.order-created.v1");
            assertThat(event.getAggregateType()).isEqualTo("ORDER");
            assertThat(event.getAggregateId()).isEqualTo(String.valueOf(saved.getId()));
            assertThat(event.getEventKey()).isEqualTo(String.valueOf(saved.getId()));
            assertThat(event.getPayload()).contains("\"orderId\":" + saved.getId());
        });
    }

    @Test
    void updateStatus_whenOrderBecomesCancelled_persistsOutboxEvent() {
        Order saved = orderService.create(orderRequest());
        outboxEventRepository.deleteAll();

        orderService.updateStatus(saved.getId(), OrderStatus.CANCELLED);

        assertThat(outboxEventRepository.findAll()).singleElement().satisfies(event -> {
            assertThat(event.getStatus()).isEqualTo(OutboxEventStatus.PENDING);
            assertThat(event.getEventType()).isEqualTo("orders.order-cancelled.v1");
            assertThat(event.getAggregateId()).isEqualTo(String.valueOf(saved.getId()));
        });
    }

    private CreateOrderRequest orderRequest() {
        return new CreateOrderRequest(
                "Ada",
                List.of(new OrderItemRequest(10L, 2, new BigDecimal("12.50")))
        );
    }
}
