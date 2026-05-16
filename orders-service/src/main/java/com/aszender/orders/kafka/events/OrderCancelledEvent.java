package com.aszender.orders.kafka.events;

import java.time.Instant;
import java.util.UUID;

public record OrderCancelledEvent(
        String eventId,
        String eventType,
        int eventVersion,
        String aggregateId,
        String aggregateType,
        Instant occurredAt,
        String correlationId,
        String producer,
        OrderCancelledPayload payload
) {
    private static final String EVENT_TYPE = "orders.order-cancelled.v1";
    private static final int EVENT_VERSION = 1;
    private static final String AGGREGATE_TYPE = "Order";

    public OrderCancelledEvent(Long orderId, String cancelledAt) {
        this(
                UUID.randomUUID().toString(),
                EVENT_TYPE,
                EVENT_VERSION,
                orderId == null ? null : orderId.toString(),
                AGGREGATE_TYPE,
                Instant.now(),
                null,
                "orders-service",
                new OrderCancelledPayload(orderId, cancelledAt)
        );
    }

    public Long orderId() {
        return payload == null ? null : payload.orderId();
    }

    public String cancelledAt() {
        return payload == null ? null : payload.cancelledAt();
    }

    public record OrderCancelledPayload(
            Long orderId,
            String cancelledAt
    ) {
    }
}
