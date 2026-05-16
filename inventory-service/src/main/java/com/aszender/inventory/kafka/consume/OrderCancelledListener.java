package com.aszender.inventory.kafka.consume;

import com.aszender.inventory.kafka.events.OrderCancelledEvent;
import com.aszender.inventory.kafka.events.StockReleasedEvent;
import com.aszender.inventory.kafka.inbox.KafkaInboxService;
import com.aszender.inventory.kafka.publish.StockEventsPublisher;
import com.aszender.inventory.service.InventoryService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@Profile("kafka")
public class OrderCancelledListener {

    private static final Logger log = LoggerFactory.getLogger(OrderCancelledListener.class);

    private final InventoryService inventoryService;
    private final StockEventsPublisher stockEventsPublisher;
    private final KafkaInboxService inboxService;

    public OrderCancelledListener(
            InventoryService inventoryService,
            StockEventsPublisher stockEventsPublisher,
            KafkaInboxService inboxService
    ) {
        this.inventoryService = inventoryService;
        this.stockEventsPublisher = stockEventsPublisher;
        this.inboxService = inboxService;
    }

    @KafkaListener(
            topics = "${app.kafka.topics.order-cancelled}",
            groupId = "inventory-service",
            properties = {
                    "spring.json.value.default.type=com.aszender.inventory.kafka.events.OrderCancelledEvent"
            }
    )
    public void onOrderCancelled(OrderCancelledEvent event, ConsumerRecord<String, OrderCancelledEvent> record) {
        KafkaInboxService.ProcessingDecision decision = inboxService.beginProcessing(
                record,
                event == null ? null : new KafkaInboxService.EventMetadata(event.eventId(), event.eventType(), event.eventVersion())
        );
        if (!decision.shouldProcess()) {
            return;
        }

        try {
            log.info("Received OrderCancelledEvent: {}", event);
            if (event == null || event.orderId() == null) {
                throw new IllegalArgumentException("OrderCancelledEvent payload.orderId is required");
            }

            boolean released = inventoryService.releaseReservation(event.orderId());
            if (released) {
                stockEventsPublisher.publishStockReleased(new StockReleasedEvent(event.orderId(), Instant.now().toString()));
            }
            inboxService.markProcessed(record);
        } catch (RuntimeException ex) {
            inboxService.markFailed(record, ex);
            throw ex;
        }
    }
}
