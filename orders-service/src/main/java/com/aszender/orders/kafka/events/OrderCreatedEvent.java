package com.aszender.orders.kafka.events;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record OrderCreatedEvent(
        String eventId,
        String eventType,
        int eventVersion,
        String aggregateId,
        String aggregateType,
        Instant occurredAt,
        String correlationId,
        String producer,
        OrderCreatedPayload payload
) {
    private static final String EVENT_TYPE = "orders.order-created.v1";
    private static final int EVENT_VERSION = 1;
    private static final String AGGREGATE_TYPE = "Order";

    public OrderCreatedEvent(Long orderId, String customerName, BigDecimal total, String createdAt, List<OrderItemEvent> items) {
        this(
                UUID.randomUUID().toString(),
                EVENT_TYPE,
                EVENT_VERSION,
                orderId == null ? null : orderId.toString(),
                AGGREGATE_TYPE,
                Instant.now(),
                null,
                "orders-service",
                new OrderCreatedPayload(orderId, customerName, total, createdAt, items)
        );
    }

    public Long orderId() {
        return payload == null ? null : payload.orderId();
    }

    public String customerName() {
        return payload == null ? null : payload.customerName();
    }

    public BigDecimal total() {
        return payload == null ? null : payload.total();
    }

    public String createdAt() {
        return payload == null ? null : payload.createdAt();
    }

    public List<OrderItemEvent> items() {
        return payload == null ? List.of() : payload.items();
    }

    public record OrderCreatedPayload(
            Long orderId,
            String customerName,
            BigDecimal total,
            String createdAt,
            List<OrderItemEvent> items
    ) {
        public OrderCreatedPayload {
            items = items == null ? List.of() : List.copyOf(items);
        }
    }
}
