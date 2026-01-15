package com.aszender.orders.kafka.events;

public record OrderItemEvent(
        Long productId,
        Integer quantity
) {
}
