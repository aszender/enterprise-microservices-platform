package com.aszender.inventory.kafka.publish;

import com.aszender.inventory.kafka.events.StockReservationFailedEvent;
import com.aszender.inventory.kafka.events.StockReservedEvent;

public interface StockEventsPublisher {
    void publishStockReserved(StockReservedEvent event);

    void publishStockReservationFailed(StockReservationFailedEvent event);
}
