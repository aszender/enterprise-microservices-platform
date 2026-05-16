package com.aszender.inventory.kafka.consume;

import com.aszender.inventory.kafka.events.ProductCreatedEvent;
import com.aszender.inventory.kafka.inbox.KafkaInboxService;
import com.aszender.inventory.service.InventoryService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
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
    private final KafkaInboxService inboxService;

    public ProductCreatedListener(InventoryService inventoryService, KafkaInboxService inboxService) {
        this.inventoryService = inventoryService;
        this.inboxService = inboxService;
    }

    @KafkaListener(
            topics = "${app.kafka.topics.product-created}",
            groupId = "inventory-service",
            properties = {
                    "spring.json.value.default.type=com.aszender.inventory.kafka.events.ProductCreatedEvent"
            }
    )
    public void onProductCreated(ProductCreatedEvent event, ConsumerRecord<String, ProductCreatedEvent> record) {
        KafkaInboxService.ProcessingDecision decision = inboxService.beginProcessing(
                record,
                event == null ? null : new KafkaInboxService.EventMetadata(event.eventId(), event.eventType(), event.eventVersion())
        );
        if (!decision.shouldProcess()) {
            log.info("Duplicate ProductCreatedEvent ignored: topic={} partition={} offset={}",
                    record.topic(), record.partition(), record.offset());
            return;
        }

        Long productId = event == null ? null : event.productId();
        try {
            log.info("Received ProductCreatedEvent: {}", event);
            if (productId == null) {
                throw new IllegalArgumentException("ProductCreatedEvent payload.productId is required");
            }

            inventoryService.ensureStockItemExists(productId);
            inboxService.markProcessed(record);
        } catch (RuntimeException ex) {
            log.error("Failed to ensure stock item for productId={}: {}", productId, ex.getMessage(), ex);
            inboxService.markFailed(record, ex);
            throw ex;
        }
    }
}
