package com.aszender.orders.kafka.publish;

import com.aszender.orders.kafka.events.OrderCreatedEvent;
import com.aszender.orders.model.Order;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Profile("kafka")
public class KafkaOrderEventsPublisher implements OrderEventsPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final String topic;

    public KafkaOrderEventsPublisher(
            KafkaTemplate<String, Object> kafkaTemplate,
            @Value("${app.kafka.topics.order-created:orders.order-created.v1}") String topic
    ) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
    }

    @Override
    public void publishOrderCreated(Order order) {
        OrderCreatedEvent event = new OrderCreatedEvent(
                order.getId(),
                order.getCustomerName(),
                order.getTotal(),
                order.getCreatedAt()
        );

        kafkaTemplate.send(topic, String.valueOf(order.getId()), event);
    }
}
