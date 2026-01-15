package com.aszender.orders.kafka.events;

import java.time.Instant;

public record StockReservationFailedEvent(
        Long orderId,
        Instant failedAt,
        String reason
) {
}
