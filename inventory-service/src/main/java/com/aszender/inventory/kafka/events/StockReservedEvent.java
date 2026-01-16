package com.aszender.inventory.kafka.events;

import java.util.List;

public record StockReservedEvent(
        Long orderId,
        String reservedAt,
        List<OrderItemEvent> items
) {
}
