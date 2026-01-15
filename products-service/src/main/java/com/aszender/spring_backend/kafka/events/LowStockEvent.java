package com.aszender.spring_backend.kafka.events;

import java.time.Instant;

public record LowStockEvent(
        Long productId,
        int available,
        int threshold,
        Instant detectedAt
) {
}
