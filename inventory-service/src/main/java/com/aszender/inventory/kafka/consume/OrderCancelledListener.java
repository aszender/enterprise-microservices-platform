package com.aszender.inventory.kafka.consume;

import com.aszender.inventory.kafka.events.OrderCancelledEvent;
import com.aszender.inventory.service.InventoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Profile("kafka")
public class OrderCancelledListener {

    private static final Logger log = LoggerFactory.getLogger(OrderCancelledListener.class);

    private final InventoryService inventoryService;

    public OrderCancelledListener(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @KafkaListener(
            topics = "${app.kafka.topics.order-cancelled}",
            groupId = "inventory-service",
            properties = {
                    "spring.json.value.default.type=com.aszender.inventory.kafka.events.OrderCancelledEvent"
            }
    )
    public void onOrderCancelled(OrderCancelledEvent event) {
        log.info("Received OrderCancelledEvent: {}", event);
        if (event == null || event.orderId() == null) {
            return;
        }

        inventoryService.releaseReservation(event.orderId());
    }
}
