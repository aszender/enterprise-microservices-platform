package com.aszender.inventory.kafka.publish;

import com.aszender.inventory.kafka.events.StockReservationFailedEvent;
import com.aszender.inventory.kafka.events.StockReleasedEvent;
import com.aszender.inventory.kafka.events.StockReservedEvent;
import com.aszender.inventory.kafka.events.LowStockEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("!kafka")
public class NoOpStockEventsPublisher implements StockEventsPublisher {

    private static final Logger log = LoggerFactory.getLogger(NoOpStockEventsPublisher.class);

    @Override
    public void publishStockReserved(StockReservedEvent event) {
        log.info("[NO-OP] publish StockReservedEvent: {}", event);
    }

    @Override
    public void publishStockReleased(StockReleasedEvent event) {
        log.info("[NO-OP] publish StockReleasedEvent: {}", event);
    }

    @Override
    public void publishStockReservationFailed(StockReservationFailedEvent event) {
        log.info("[NO-OP] publish StockReservationFailedEvent: {}", event);
    }

    @Override
    public void publishLowStock(LowStockEvent event) {
        log.info("[NO-OP] publish LowStockEvent: {}", event);
    }
}
