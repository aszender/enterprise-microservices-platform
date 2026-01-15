package com.aszender.spring_backend.kafka.publish;

import com.aszender.spring_backend.kafka.events.ProductCreatedEvent;
import com.aszender.spring_backend.model.Product;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@Profile("kafka")
public class KafkaProductEventsPublisher implements ProductEventsPublisher {

    private final KafkaTemplate<Object, Object> kafkaTemplate;
    private final String topic;

    public KafkaProductEventsPublisher(
            KafkaTemplate<Object, Object> kafkaTemplate,
            @Value("${app.kafka.topics.product-created:products.product-created.v1}") String topic
    ) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
    }

    @Override
    public void publishProductCreated(Product product) {
        ProductCreatedEvent event = new ProductCreatedEvent(
                product.getId(),
                product.getName(),
                product.getPrice(),
                Instant.now()
        );

        kafkaTemplate.send(topic, String.valueOf(product.getId()), event);
    }
}
