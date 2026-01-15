package com.aszender.orders.kafka.consume;

import com.aszender.orders.kafka.events.StockReservedEvent;
import com.aszender.orders.kafka.inbox.KafkaInboxService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Profile("kafka")
public class StockReservedListener {

    private static final Logger log = LoggerFactory.getLogger(StockReservedListener.class);

    private final KafkaInboxService inboxService;

    public StockReservedListener(KafkaInboxService inboxService) {
        this.inboxService = inboxService;
    }

    @KafkaListener(
            topics = "${app.kafka.topics.stock-reserved}",
            groupId = "orders-service",
            properties = {
                    "spring.json.value.default.type=com.aszender.orders.kafka.events.StockReservedEvent"
            }
    )
    public void onStockReserved(StockReservedEvent event, ConsumerRecord<String, StockReservedEvent> record) {
        if (!inboxService.tryConsume(record)) {
            return;
        }
        log.info("Received StockReservedEvent: {}", event);
    }
}
