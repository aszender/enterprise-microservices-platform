package com.aszender.inventory.kafka.events;

import java.time.Instant;
import java.util.UUID;

public record LowStockEvent(
        String eventId,
        String eventType,
        int eventVersion,
        String aggregateId,
        String aggregateType,
        Instant occurredAt,
        String correlationId,
        String producer,
        LowStockPayload payload
) {
    private static final String EVENT_TYPE = "inventory.low-stock.v1";
    private static final int EVENT_VERSION = 1;
    private static final String AGGREGATE_TYPE = "Product";

    public LowStockEvent(Long productId, int available, int threshold, String detectedAt) {
        this(
                UUID.randomUUID().toString(),
                EVENT_TYPE,
                EVENT_VERSION,
                productId == null ? null : productId.toString(),
                AGGREGATE_TYPE,
                Instant.now(),
                null,
                "inventory-service",
                new LowStockPayload(productId, available, threshold, detectedAt)
        );
    }

    public Long productId() {
        return payload == null ? null : payload.productId();
    }

    public int available() {
        return payload == null ? 0 : payload.available();
    }

    public int threshold() {
        return payload == null ? 0 : payload.threshold();
    }

    public String detectedAt() {
        return payload == null ? null : payload.detectedAt();
    }

    public record LowStockPayload(
            Long productId,
            int available,
            int threshold,
            String detectedAt
    ) {
    }
}
