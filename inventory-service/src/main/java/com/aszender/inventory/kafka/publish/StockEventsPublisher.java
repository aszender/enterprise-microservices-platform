package com.aszender.inventory.kafka.publish;

import com.aszender.inventory.kafka.events.StockReservationFailedEvent;
import com.aszender.inventory.kafka.events.StockReleasedEvent;
import com.aszender.inventory.kafka.events.StockReservedEvent;
import com.aszender.inventory.kafka.events.LowStockEvent;

public interface StockEventsPublisher {
    void publishStockReserved(StockReservedEvent event);

    void publishStockReleased(StockReleasedEvent event);

    void publishStockReservationFailed(StockReservationFailedEvent event);

    void publishLowStock(LowStockEvent event);
}
