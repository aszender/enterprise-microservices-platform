package com.aszender.inventory.kafka.events;

public record ProductCreatedEvent(
        Long productId,
        String name,
        Double price,
        String createdAt
) {
}
