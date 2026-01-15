package com.aszender.orders.kafka.consume;

import com.aszender.orders.kafka.events.StockReservationFailedEvent;
import com.aszender.orders.kafka.inbox.KafkaInboxService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Profile("kafka")
public class StockReservationFailedListener {

    private static final Logger log = LoggerFactory.getLogger(StockReservationFailedListener.class);

    private final KafkaInboxService inboxService;

    public StockReservationFailedListener(KafkaInboxService inboxService) {
        this.inboxService = inboxService;
    }

    @KafkaListener(
            topics = "${app.kafka.topics.stock-reservation-failed}",
            groupId = "orders-service",
            properties = {
                    "spring.json.value.default.type=com.aszender.orders.kafka.events.StockReservationFailedEvent"
            }
    )
    public void onStockReservationFailed(StockReservationFailedEvent event, ConsumerRecord<String, StockReservationFailedEvent> record) {
        if (!inboxService.tryConsume(record)) {
            return;
        }
        log.info("Received StockReservationFailedEvent: {}", event);
    }
}
