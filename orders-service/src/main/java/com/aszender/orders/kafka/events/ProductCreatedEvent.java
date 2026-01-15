package com.aszender.orders.kafka.events;

import java.time.Instant;

public record ProductCreatedEvent(
        Long productId,
        String name,
        Double price,
        Instant createdAt
) {
}
