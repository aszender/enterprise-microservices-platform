package com.aszender.orders.kafka.outbox.service;

import com.aszender.orders.kafka.events.OrderCancelledEvent;
import com.aszender.orders.kafka.events.OrderCreatedEvent;
import com.aszender.orders.kafka.events.OrderItemEvent;
import com.aszender.orders.kafka.outbox.model.OutboxEvent;
import com.aszender.orders.kafka.outbox.repository.OutboxEventRepository;
import com.aszender.orders.model.Order;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class OrderOutboxService {

    private static final String ORDER_AGGREGATE = "ORDER";

    private final OutboxEventRepository outboxEventRepository;
    private final ObjectMapper objectMapper;
    private final String orderCreatedTopic;
    private final String orderCancelledTopic;

    public OrderOutboxService(
            OutboxEventRepository outboxEventRepository,
            ObjectMapper objectMapper,
            @Value("${app.kafka.topics.order-created:orders.order-created.v1}") String orderCreatedTopic,
            @Value("${app.kafka.topics.order-cancelled:orders.order-cancelled.v1}") String orderCancelledTopic
    ) {
        this.outboxEventRepository = outboxEventRepository;
        this.objectMapper = objectMapper;
        this.orderCreatedTopic = orderCreatedTopic;
        this.orderCancelledTopic = orderCancelledTopic;
    }

    public OutboxEvent enqueueOrderCreated(Order order) {
        OrderCreatedEvent event = new OrderCreatedEvent(
                order.getId(),
                order.getCustomerName(),
                order.getTotal(),
                order.getCreatedAt() == null ? null : order.getCreatedAt().toString(),
                order.getItems().stream()
                        .map(i -> new OrderItemEvent(i.getProductId(), i.getQuantity(), i.getUnitPrice()))
                        .toList()
        );

        return save(order, OrderOutboxEventTypes.ORDER_CREATED, orderCreatedTopic, event);
    }

    public OutboxEvent enqueueOrderCancelled(Order order) {
        OrderCancelledEvent event = new OrderCancelledEvent(order.getId(), Instant.now().toString());

        return save(order, OrderOutboxEventTypes.ORDER_CANCELLED, orderCancelledTopic, event);
    }

    private OutboxEvent save(Order order, String eventType, String topic, Object event) {
        OutboxEvent outboxEvent = OutboxEvent.pending(
                ORDER_AGGREGATE,
                order.getId(),
                eventType,
                topic,
                String.valueOf(order.getId()),
                toJson(event)
        );

        return outboxEventRepository.save(outboxEvent);
    }

    private String toJson(Object event) {
        try {
            return objectMapper.writeValueAsString(event);
        } catch (JsonProcessingException ex) {
            throw new IllegalStateException("Failed to serialize order outbox event", ex);
        }
    }
}
