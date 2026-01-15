package com.aszender.orders.kafka.publish;

import com.aszender.orders.model.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("!kafka")
public class NoOpOrderEventsPublisher implements OrderEventsPublisher {

    private static final Logger log = LoggerFactory.getLogger(NoOpOrderEventsPublisher.class);

    @Override
    public void publishOrderCreated(Order order) {
        // Intentionally no-op. Keeps local/tests running without Kafka.
        log.debug("Kafka profile disabled; not publishing OrderCreatedEvent for orderId={}", order.getId());
    }
}
