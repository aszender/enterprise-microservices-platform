package com.aszender.orders.kafka.publish;

import com.aszender.orders.kafka.events.OrderCancelledEvent;
import com.aszender.orders.kafka.events.OrderCreatedEvent;
import com.aszender.orders.kafka.events.OrderItemEvent;
import com.aszender.orders.model.Order;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@Profile("kafka")
public class KafkaOrderEventsPublisher implements OrderEventsPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final String orderCreatedTopic;
    private final String orderCancelledTopic;

    public KafkaOrderEventsPublisher(
            KafkaTemplate<String, Object> kafkaTemplate,
            @Value("${app.kafka.topics.order-created:orders.order-created.v1}") String orderCreatedTopic,
            @Value("${app.kafka.topics.order-cancelled:orders.order-cancelled.v1}") String orderCancelledTopic
    ) {
        this.kafkaTemplate = kafkaTemplate;
        this.orderCreatedTopic = orderCreatedTopic;
        this.orderCancelledTopic = orderCancelledTopic;
    }

    @Override
    public void publishOrderCreated(Order order) {
        OrderCreatedEvent event = new OrderCreatedEvent(
                order.getId(),
                order.getCreatedAt() == null ? null : order.getCreatedAt().toString(),
                order.getItems().stream()
                        .map(i -> new OrderItemEvent(i.getProductId(), i.getQuantity()))
                        .toList()
        );

        kafkaTemplate.send(orderCreatedTopic, String.valueOf(order.getId()), event);
    }

    @Override
    public void publishOrderCancelled(Order order) {
        OrderCancelledEvent event = new OrderCancelledEvent(order.getId(), Instant.now().toString());
        kafkaTemplate.send(orderCancelledTopic, String.valueOf(order.getId()), event);
    }
}
