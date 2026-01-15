package com.aszender.inventory.service;

import com.aszender.inventory.kafka.events.LowStockEvent;
import com.aszender.inventory.kafka.events.OrderItemEvent;
import com.aszender.inventory.kafka.events.StockReservationFailedEvent;
import com.aszender.inventory.kafka.events.StockReservedEvent;
import com.aszender.inventory.kafka.publish.StockEventsPublisher;
import com.aszender.inventory.repository.StockItemRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class InventoryReservationOrchestrator {

    private final InventoryService inventoryService;
    private final StockItemRepository stockItemRepository;
    private final StockEventsPublisher stockEventsPublisher;
    private final int lowStockThreshold;

    public InventoryReservationOrchestrator(
            InventoryService inventoryService,
            StockItemRepository stockItemRepository,
            StockEventsPublisher stockEventsPublisher,
            @Value("${app.inventory.low-stock-threshold:5}") int lowStockThreshold
    ) {
        this.inventoryService = inventoryService;
        this.stockItemRepository = stockItemRepository;
        this.stockEventsPublisher = stockEventsPublisher;
        this.lowStockThreshold = lowStockThreshold;
    }

    public record ReserveLine(Long productId, int quantity) {
    }

    public record ReserveResult(boolean reserved, String reason) {
    }

    public ReserveResult reserve(Long orderId, List<ReserveLine> items) {
        boolean reserved = inventoryService.reserveStock(
                orderId,
                items.stream()
                        .map(i -> new InventoryService.ReservationLine(i.productId(), i.quantity()))
                        .toList()
        );

        if (reserved) {
            stockEventsPublisher.publishStockReserved(new StockReservedEvent(
                    orderId,
                    Instant.now(),
                    items.stream()
                            .map(i -> new OrderItemEvent(i.productId(), i.quantity()))
                            .toList()
            ));

            // Low-stock event (only when crossing the threshold).
            Map<Long, Integer> reservedQtyByProductId = items.stream()
                    .collect(Collectors.groupingBy(ReserveLine::productId, Collectors.summingInt(ReserveLine::quantity)));

            for (Map.Entry<Long, Integer> entry : reservedQtyByProductId.entrySet()) {
                Long productId = entry.getKey();
                int reservedQty = entry.getValue();

                stockItemRepository.findByProductId(productId).ifPresent(stockItem -> {
                    int availableAfter = stockItem.getAvailable();
                    int availableBefore = availableAfter + reservedQty;

                    boolean crossedToLow = availableBefore > lowStockThreshold && availableAfter <= lowStockThreshold;
                    if (crossedToLow) {
                        stockEventsPublisher.publishLowStock(new LowStockEvent(
                                productId,
                                availableAfter,
                                lowStockThreshold,
                                Instant.now()
                        ));
                    }
                });
            }

            return new ReserveResult(true, null);
        }

        stockEventsPublisher.publishStockReservationFailed(new StockReservationFailedEvent(
                orderId,
                Instant.now(),
                "INSUFFICIENT_STOCK"
        ));

        return new ReserveResult(false, "INSUFFICIENT_STOCK");
    }
}
