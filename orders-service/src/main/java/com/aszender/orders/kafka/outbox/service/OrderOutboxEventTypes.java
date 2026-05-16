package com.aszender.orders.kafka.outbox.service;

final class OrderOutboxEventTypes {
    static final String ORDER_CREATED = "orders.order-created.v1";
    static final String ORDER_CANCELLED = "orders.order-cancelled.v1";

    private OrderOutboxEventTypes() {
    }
}
