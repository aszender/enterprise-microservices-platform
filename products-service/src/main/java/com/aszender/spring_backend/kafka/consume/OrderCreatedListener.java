package com.aszender.spring_backend.kafka.consume;

import com.aszender.spring_backend.kafka.events.OrderCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Profile("kafka")
public class OrderCreatedListener {

    private static final Logger log = LoggerFactory.getLogger(OrderCreatedListener.class);

    @KafkaListener(
        topics = "${app.kafka.topics.order-created:orders.order-created.v1}",
            groupId = "products-service",
            properties = {
                    "spring.json.value.default.type=com.aszender.spring_backend.kafka.events.OrderCreatedEvent"
            }
    )
    public void onOrderCreated(OrderCreatedEvent event) {
        // For now: just log. Later: you could reserve stock, update projections, etc.
        log.info("Consumed OrderCreatedEvent: orderId={}, customerName={}, total={}, createdAt={}",
                event.orderId(), event.customerName(), event.total(), event.createdAt());
    }
}
