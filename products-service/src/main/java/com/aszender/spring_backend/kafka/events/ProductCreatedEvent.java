package com.aszender.spring_backend.kafka.events;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record ProductCreatedEvent(
        String eventId,
        String eventType,
        int eventVersion,
        String aggregateId,
        String aggregateType,
        Instant occurredAt,
        String correlationId,
        String producer,
        ProductCreatedPayload payload
) {
    private static final String EVENT_TYPE = "products.product-created.v1";
    private static final int EVENT_VERSION = 1;
    private static final String AGGREGATE_TYPE = "Product";

    public ProductCreatedEvent(Long productId, String name, BigDecimal price, String createdAt) {
        this(
                UUID.randomUUID().toString(),
                EVENT_TYPE,
                EVENT_VERSION,
                productId == null ? null : productId.toString(),
                AGGREGATE_TYPE,
                Instant.now(),
                null,
                "products-service",
                new ProductCreatedPayload(productId, name, price, createdAt)
        );
    }

    public Long productId() {
        return payload == null ? null : payload.productId();
    }

    public String name() {
        return payload == null ? null : payload.name();
    }

    public BigDecimal price() {
        return payload == null ? null : payload.price();
    }

    public String createdAt() {
        return payload == null ? null : payload.createdAt();
    }

    public record ProductCreatedPayload(
            Long productId,
            String name,
            BigDecimal price,
            String createdAt
    ) {
    }
}
