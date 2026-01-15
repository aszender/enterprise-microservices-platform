package com.aszender.inventory.kafka.consume;

import com.aszender.inventory.kafka.events.OrderCreatedEvent;
import com.aszender.inventory.kafka.inbox.KafkaInboxService;
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

    public OrderCreatedListener(KafkaInboxService inboxService) {
        this.inboxService = inboxService;
    }

    @KafkaListener(
            topics = "${app.kafka.topics.order-created}",
            groupId = "inventory-service",
            properties = {
                    "spring.json.value.default.type=com.aszender.inventory.kafka.events.OrderCreatedEvent"
            }
    )
    public void onOrderCreated(OrderCreatedEvent event, ConsumerRecord<String, OrderCreatedEvent> record) {
        if (!inboxService.tryConsume(record)) {
            return;
        }
        log.info("Received OrderCreatedEvent: {}", event);
    }
}
