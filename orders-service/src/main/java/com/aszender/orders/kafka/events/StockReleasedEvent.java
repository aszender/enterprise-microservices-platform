package com.aszender.orders.kafka.events;

import java.time.Instant;

public record StockReleasedEvent(
        Long orderId,
        Instant releasedAt
) {
}
