package com.aszender.orders.kafka.events;

import java.time.Instant;
import java.util.List;

public record StockReservedEvent(
        Long orderId,
        Instant reservedAt,
        List<OrderItemEvent> items
) {
}
