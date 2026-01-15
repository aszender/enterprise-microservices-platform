package com.aszender.inventory.kafka.events;

import java.time.Instant;

public record StockReleasedEvent(
        Long orderId,
        Instant releasedAt
) {
}
