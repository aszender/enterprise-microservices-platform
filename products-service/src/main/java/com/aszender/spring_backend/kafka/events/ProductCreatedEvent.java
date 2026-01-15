package com.aszender.spring_backend.kafka.events;

import java.time.Instant;

public record ProductCreatedEvent(
        Long productId,
        String name,
        Double price,
        Instant createdAt
) {
}
