package com.aszender.inventory.kafka.events;

import java.time.Instant;
import java.util.List;

public record OrderCreatedEvent(
        Long orderId,
        Instant createdAt,
        List<OrderItemEvent> items
) {
}
