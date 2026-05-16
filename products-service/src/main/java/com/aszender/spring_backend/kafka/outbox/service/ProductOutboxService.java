package com.aszender.spring_backend.kafka.outbox.service;

import com.aszender.spring_backend.kafka.events.ProductCreatedEvent;
import com.aszender.spring_backend.kafka.outbox.model.OutboxEvent;
import com.aszender.spring_backend.kafka.outbox.repository.OutboxEventRepository;
import com.aszender.spring_backend.model.Product;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.time.Instant;

@Service
public class ProductOutboxService {

    private static final String PRODUCT_AGGREGATE = "PRODUCT";

    private final OutboxEventRepository outboxEventRepository;
    private final ObjectMapper objectMapper;
    private final String productCreatedTopic;

    public ProductOutboxService(
            OutboxEventRepository outboxEventRepository,
            ObjectMapper objectMapper,
            @Value("${app.kafka.topics.product-created:products.product-created.v1}") String productCreatedTopic
    ) {
        this.outboxEventRepository = outboxEventRepository;
        this.objectMapper = objectMapper;
        this.productCreatedTopic = productCreatedTopic;
    }

    public OutboxEvent enqueueProductCreated(Product product) {
        ProductCreatedEvent event = new ProductCreatedEvent(
                product.getId(),
                product.getName(),
                product.getPrice(),
                Instant.now().toString()
        );

        OutboxEvent outboxEvent = OutboxEvent.pending(
                PRODUCT_AGGREGATE,
                product.getId(),
                ProductOutboxEventTypes.PRODUCT_CREATED,
                productCreatedTopic,
                String.valueOf(product.getId()),
                toJson(event)
        );

        return outboxEventRepository.save(outboxEvent);
    }

    private String toJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception ex) {
            throw new IllegalStateException("Failed to serialize product outbox event", ex);
        }
    }
}
