package com.aszender.orders.kafka.consume;

import com.aszender.orders.kafka.events.StockReservationFailedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Profile("kafka")
public class StockReservationFailedListener {

    private static final Logger log = LoggerFactory.getLogger(StockReservationFailedListener.class);

    public StockReservationFailedListener() {
    }

    @KafkaListener(
            topics = "${app.kafka.topics.stock-reservation-failed}",
            groupId = "orders-service",
            properties = {
                    "spring.json.value.default.type=com.aszender.orders.kafka.events.StockReservationFailedEvent"
            }
    )
    public void onStockReservationFailed(StockReservationFailedEvent event) {
        log.info("Received StockReservationFailedEvent: {}", event);
    }
}
