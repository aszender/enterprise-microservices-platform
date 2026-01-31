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
        // Idempotency guard: use inbox pattern to avoid double-processing when Kafka
        // delivers messages more than once (at-least-once semantics).
        // If the message was already processed, log and skip.
        if (!inboxService.tryConsume(record)) {
            log.info("Duplicate ProductCreatedEvent ignored: topic={} partition={} offset={}",
                    record.topic(), record.partition(), record.offset());
            return;
        }

        log.info("Received ProductCreatedEvent: {}", event);
        if (event == null || event.productId() == null) {
            return;
        }

        // Trade-off comment: we intentionally avoid distributed transactions here.
        // Compensation and idempotency are preferred over coordination in this demo.
        try {
            inventoryService.ensureStockItemExists(event.productId());
        } catch (Exception ex) {
            // Failure handling: record the failure and surface it via logs/alerts.
            // In a fuller system we'd publish a failed-event or push to a DLQ for manual review.
            log.error("Failed to ensure stock item for productId={}: {}", event.productId(), ex.getMessage(), ex);
            // Domain decision example: mark the operation for retry by leaving the inbox entry absent
            // or by pushing a compensating message. Here we simply log to make the failure explicit.
        }
    }
}
