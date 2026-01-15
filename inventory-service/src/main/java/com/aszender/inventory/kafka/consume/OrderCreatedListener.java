package com.aszender.inventory.kafka.consume;

import com.aszender.inventory.kafka.events.OrderCreatedEvent;
import com.aszender.inventory.kafka.events.StockReservationFailedEvent;
import com.aszender.inventory.kafka.events.StockReservedEvent;
import com.aszender.inventory.kafka.publish.StockEventsPublisher;
import com.aszender.inventory.service.InventoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@Profile("kafka")
public class OrderCreatedListener {

    private static final Logger log = LoggerFactory.getLogger(OrderCreatedListener.class);

    private final InventoryService inventoryService;
    private final StockEventsPublisher stockEventsPublisher;

    public OrderCreatedListener(InventoryService inventoryService, StockEventsPublisher stockEventsPublisher) {
        this.inventoryService = inventoryService;
        this.stockEventsPublisher = stockEventsPublisher;
    }

    @KafkaListener(
            topics = "${app.kafka.topics.order-created}",
            groupId = "inventory-service",
            properties = {
                    "spring.json.value.default.type=com.aszender.inventory.kafka.events.OrderCreatedEvent"
            }
    )
    public void onOrderCreated(OrderCreatedEvent event) {
        log.info("Received OrderCreatedEvent: {}", event);
        if (event == null || event.orderId() == null) {
            return;
        }

        try {
            boolean reserved = inventoryService.reserveStock(
                    event.orderId(),
                    event.items().stream()
                            .map(i -> new InventoryService.ReservationLine(i.productId(), i.quantity()))
                            .toList()
            );

            if (reserved) {
                stockEventsPublisher.publishStockReserved(new StockReservedEvent(
                        event.orderId(),
                        Instant.now(),
                        event.items()
                ));
            } else {
                stockEventsPublisher.publishStockReservationFailed(new StockReservationFailedEvent(
                        event.orderId(),
                        Instant.now(),
                        "INSUFFICIENT_STOCK"
                ));
            }
        } catch (Exception ex) {
            stockEventsPublisher.publishStockReservationFailed(new StockReservationFailedEvent(
                    event.orderId(),
                    Instant.now(),
                    ex.getMessage()
            ));
        }
    }
}
