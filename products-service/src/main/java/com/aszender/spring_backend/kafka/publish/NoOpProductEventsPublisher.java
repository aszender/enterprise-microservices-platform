package com.aszender.spring_backend.kafka.publish;

import com.aszender.spring_backend.model.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("!kafka")
public class NoOpProductEventsPublisher implements ProductEventsPublisher {

    private static final Logger log = LoggerFactory.getLogger(NoOpProductEventsPublisher.class);

    @Override
    public void publishProductCreated(Product product) {
        // Intentionally no-op. Keeps local/tests running without Kafka.
        log.debug("Kafka profile disabled; not publishing ProductCreatedEvent for productId={}", product.getId());
    }
}
