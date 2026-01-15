package com.aszender.inventory.kafka.publish;

import com.aszender.inventory.kafka.events.StockReservationFailedEvent;
import com.aszender.inventory.kafka.events.StockReleasedEvent;
import com.aszender.inventory.kafka.events.StockReservedEvent;
import com.aszender.inventory.kafka.events.LowStockEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Profile("kafka")
public class KafkaStockEventsPublisher implements StockEventsPublisher {

    private static final Logger log = LoggerFactory.getLogger(KafkaStockEventsPublisher.class);

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final String stockReservedTopic;
    private final String stockReleasedTopic;
    private final String stockReservationFailedTopic;
    private final String lowStockTopic;

    public KafkaStockEventsPublisher(
            KafkaTemplate<String, Object> kafkaTemplate,
            @Value("${app.kafka.topics.stock-reserved}") String stockReservedTopic,
            @Value("${app.kafka.topics.stock-released}") String stockReleasedTopic,
            @Value("${app.kafka.topics.stock-reservation-failed}") String stockReservationFailedTopic,
            @Value("${app.kafka.topics.low-stock}") String lowStockTopic
    ) {
        this.kafkaTemplate = kafkaTemplate;
        this.stockReservedTopic = stockReservedTopic;
        this.stockReleasedTopic = stockReleasedTopic;
        this.stockReservationFailedTopic = stockReservationFailedTopic;
        this.lowStockTopic = lowStockTopic;
    }

    @Override
    public void publishStockReserved(StockReservedEvent event) {
        log.info("Publishing StockReservedEvent to {}: {}", stockReservedTopic, event);
        kafkaTemplate.send(stockReservedTopic, event.orderId().toString(), event);
    }

    @Override
    public void publishStockReleased(StockReleasedEvent event) {
        log.info("Publishing StockReleasedEvent to {}: {}", stockReleasedTopic, event);
        kafkaTemplate.send(stockReleasedTopic, event.orderId().toString(), event);
    }

    @Override
    public void publishStockReservationFailed(StockReservationFailedEvent event) {
        log.info("Publishing StockReservationFailedEvent to {}: {}", stockReservationFailedTopic, event);
        kafkaTemplate.send(stockReservationFailedTopic, event.orderId().toString(), event);
    }

    @Override
    public void publishLowStock(LowStockEvent event) {
        log.info("Publishing LowStockEvent to {}: {}", lowStockTopic, event);
        kafkaTemplate.send(lowStockTopic, event.productId().toString(), event);
    }
}
