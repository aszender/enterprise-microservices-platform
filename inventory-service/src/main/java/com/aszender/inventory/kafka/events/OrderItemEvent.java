package com.aszender.inventory.kafka.events;

public record OrderItemEvent(
        Long productId,
        int quantity
) {
}
