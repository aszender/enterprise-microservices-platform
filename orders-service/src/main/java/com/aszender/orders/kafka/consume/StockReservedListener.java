package com.aszender.orders.kafka.consume;

import com.aszender.orders.kafka.events.StockReservedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Profile("kafka")
public class StockReservedListener {

    private static final Logger log = LoggerFactory.getLogger(StockReservedListener.class);

    public StockReservedListener() {
    }

    @KafkaListener(
            topics = "${app.kafka.topics.stock-reserved}",
            groupId = "orders-service",
            properties = {
                    "spring.json.value.default.type=com.aszender.orders.kafka.events.StockReservedEvent"
            }
    )
    public void onStockReserved(StockReservedEvent event) {
        log.info("Received StockReservedEvent: {}", event);
    }
}
