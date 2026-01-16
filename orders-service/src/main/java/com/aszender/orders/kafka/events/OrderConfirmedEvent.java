package com.aszender.orders.kafka.events;

public record OrderConfirmedEvent(
        Long orderId,
        String confirmedAt
) {
}
