package com.aszender.orders.kafka.events;

public record OrderCancelledEvent(
        Long orderId,
        String cancelledAt
) {
}
