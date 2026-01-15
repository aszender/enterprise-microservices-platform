package com.aszender.inventory.kafka.events;

import java.time.Instant;

public record LowStockEvent(
        Long productId,
        int available,
        int threshold,
        Instant detectedAt
) {
}
