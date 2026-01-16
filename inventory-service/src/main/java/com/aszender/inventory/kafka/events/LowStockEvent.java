package com.aszender.inventory.kafka.events;

public record LowStockEvent(
        Long productId,
        int available,
        int threshold,
        String detectedAt
) {
}
