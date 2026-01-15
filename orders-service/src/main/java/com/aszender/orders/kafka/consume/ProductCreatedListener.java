package com.aszender.orders.kafka.consume;

import com.aszender.orders.kafka.events.ProductCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Profile("kafka")
public class ProductCreatedListener {

    private static final Logger log = LoggerFactory.getLogger(ProductCreatedListener.class);

    @KafkaListener(
        topics = "${app.kafka.topics.product-created:products.product-created.v1}",
            groupId = "orders-service",
            properties = {
                    "spring.json.value.default.type=com.aszender.orders.kafka.events.ProductCreatedEvent"
            }
    )
    public void onProductCreated(ProductCreatedEvent event) {
        // For now: just log. Later: you might maintain a local product read-model for pricing, etc.
        log.info("Consumed ProductCreatedEvent: productId={}, name={}, price={}, createdAt={}",
                event.productId(), event.name(), event.price(), event.createdAt());
    }
}
