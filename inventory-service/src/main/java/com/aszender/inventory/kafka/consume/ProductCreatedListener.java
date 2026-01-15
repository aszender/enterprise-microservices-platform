package com.aszender.inventory.kafka.consume;

import com.aszender.inventory.kafka.events.ProductCreatedEvent;
import com.aszender.inventory.service.InventoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Profile("kafka")
public class ProductCreatedListener {

    private static final Logger log = LoggerFactory.getLogger(ProductCreatedListener.class);

    private final InventoryService inventoryService;

    public ProductCreatedListener(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @KafkaListener(
            topics = "${app.kafka.topics.product-created}",
            groupId = "inventory-service",
            properties = {
                    "spring.json.value.default.type=com.aszender.inventory.kafka.events.ProductCreatedEvent"
            }
    )
    public void onProductCreated(ProductCreatedEvent event) {
        log.info("Received ProductCreatedEvent: {}", event);
        if (event == null || event.productId() == null) {
            return;
        }
        inventoryService.ensureStockItemExists(event.productId());
    }
}
