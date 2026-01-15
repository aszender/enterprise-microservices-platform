package com.aszender.orders.kafka.events;

import java.time.Instant;

public record OrderCreatedEvent(
        Long orderId,
        String customerName,
        Double total,
        Instant createdAt
) {
}
