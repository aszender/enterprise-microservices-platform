package com.aszender.spring_backend.kafka.events;

public record ProductCreatedEvent(
        Long productId,
        String name,
        Double price,
        String createdAt
) {
}
