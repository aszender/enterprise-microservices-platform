package com.aszender.orders.kafka.events;

import java.util.List;

public record OrderCreatedEvent(
        Long orderId,
        String createdAt,
        List<OrderItemEvent> items
) {
}
