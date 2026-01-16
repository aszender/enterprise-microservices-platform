package com.aszender.inventory.kafka.events;

public record StockReservationFailedEvent(
        Long orderId,
        String failedAt,
        String reason
) {
}
