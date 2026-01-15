package com.aszender.inventory.kafka.events;

import java.time.Instant;

public record StockReservationFailedEvent(
        Long orderId,
        Instant failedAt,
        String reason
) {
}
