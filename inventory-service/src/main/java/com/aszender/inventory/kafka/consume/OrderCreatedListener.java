package com.aszender.inventory.kafka.consume;

import com.aszender.inventory.kafka.events.OrderCreatedEvent;
import com.aszender.inventory.kafka.inbox.KafkaInboxService;
import com.aszender.inventory.service.InventoryReservationOrchestrator;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Profile("kafka")
public class OrderCreatedListener {

    private static final Logger log = LoggerFactory.getLogger(OrderCreatedListener.class);

    private final KafkaInboxService inboxService;
    private final InventoryReservationOrchestrator reservationOrchestrator;

    public OrderCreatedListener(KafkaInboxService inboxService, InventoryReservationOrchestrator reservationOrchestrator) {
        this.inboxService = inboxService;
        this.reservationOrchestrator = reservationOrchestrator;
    }

    @KafkaListener(
            topics = "${app.kafka.topics.order-created}",
            groupId = "inventory-service",
            properties = {
                    "spring.json.value.default.type=com.aszender.inventory.kafka.events.OrderCreatedEvent"
            }
    )
    public void onOrderCreated(OrderCreatedEvent event, ConsumerRecord<String, OrderCreatedEvent> record) {
        KafkaInboxService.ProcessingDecision decision = inboxService.beginProcessing(
                record,
                event == null ? null : new KafkaInboxService.EventMetadata(event.eventId(), event.eventType(), event.eventVersion())
        );
        if (!decision.shouldProcess()) {
            return;
        }

        try {
            log.info("Received OrderCreatedEvent: {}", event);
            if (event == null || event.orderId() == null || event.items().isEmpty()) {
                throw new IllegalArgumentException("OrderCreatedEvent payload.orderId and payload.items are required");
            }

            reservationOrchestrator.reserve(
                    event.orderId(),
                    event.items().stream()
                            .map(item -> new InventoryReservationOrchestrator.ReserveLine(
                                    item.productId(),
                                    item.quantity()
                            ))
                            .toList()
            );
            inboxService.markProcessed(record);
        } catch (RuntimeException ex) {
            inboxService.markFailed(record, ex);
            throw ex;
        }
    }
}
