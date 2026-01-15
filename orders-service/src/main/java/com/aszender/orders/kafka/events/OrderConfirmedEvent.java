package com.aszender.orders.kafka.events;

import java.time.Instant;

public record OrderConfirmedEvent(
        Long orderId,
        Instant confirmedAt
) {
}
