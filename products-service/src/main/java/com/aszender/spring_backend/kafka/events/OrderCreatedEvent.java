package com.aszender.spring_backend.kafka.events;

import java.time.Instant;

public record OrderCreatedEvent(
        Long orderId,
        String customerName,
        Double total,
        Instant createdAt
) {
}
