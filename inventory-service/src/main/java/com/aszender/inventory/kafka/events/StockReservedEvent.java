package com.aszender.inventory.kafka.events;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record StockReservedEvent(
        String eventId,
        String eventType,
        int eventVersion,
        String aggregateId,
        String aggregateType,
        Instant occurredAt,
        String correlationId,
        String producer,
        StockReservedPayload payload
) {
    private static final String EVENT_TYPE = "inventory.stock-reserved.v1";
    private static final int EVENT_VERSION = 1;
    private static final String AGGREGATE_TYPE = "Order";

    public StockReservedEvent(Long orderId, String reservedAt, List<OrderItemEvent> items) {
        this(
                UUID.randomUUID().toString(),
                EVENT_TYPE,
                EVENT_VERSION,
                orderId == null ? null : orderId.toString(),
                AGGREGATE_TYPE,
                Instant.now(),
                null,
                "inventory-service",
                new StockReservedPayload(orderId, reservedAt, items)
        );
    }

    public Long orderId() {
        return payload == null ? null : payload.orderId();
    }

    public String reservedAt() {
        return payload == null ? null : payload.reservedAt();
    }

    public List<OrderItemEvent> items() {
        return payload == null ? List.of() : payload.items();
    }

    public record StockReservedPayload(
            Long orderId,
            String reservedAt,
            List<OrderItemEvent> items
    ) {
        public StockReservedPayload {
            items = items == null ? List.of() : List.copyOf(items);
        }
    }
}
