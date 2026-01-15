package com.aszender.inventory.kafka.events;

import java.time.Instant;

public record OrderCancelledEvent(
        Long orderId,
        Instant cancelledAt
) {
}
