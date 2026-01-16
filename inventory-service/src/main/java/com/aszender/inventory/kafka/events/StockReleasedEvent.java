package com.aszender.inventory.kafka.events;

public record StockReleasedEvent(
        Long orderId,
        String releasedAt
) {
}
