package com.aszender.spring_backend.kafka.outbox.service;

import com.aszender.spring_backend.kafka.events.ProductCreatedEvent;
import com.aszender.spring_backend.kafka.outbox.model.OutboxEvent;
import com.aszender.spring_backend.kafka.outbox.model.OutboxEventStatus;
import com.aszender.spring_backend.kafka.outbox.repository.OutboxEventRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import java.time.Instant;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;

@Service
@Profile("kafka")
public class ProductOutboxPublisher {

    private final OutboxEventRepository outboxEventRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public ProductOutboxPublisher(
            OutboxEventRepository outboxEventRepository,
            KafkaTemplate<String, Object> kafkaTemplate,
            ObjectMapper objectMapper
    ) {
        this.outboxEventRepository = outboxEventRepository;
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    @Scheduled(fixedDelayString = "${app.kafka.outbox.poll-delay-ms:5000}")
    @Transactional
    public void publishPending() {
        outboxEventRepository.findTop50ByStatusOrderByCreatedAtAsc(OutboxEventStatus.PENDING)
                .forEach(this::publishOne);
    }

    private void publishOne(OutboxEvent event) {
        if (event.isPublished()) {
            return;
        }

        try {
            kafkaTemplate.send(event.getTopic(), event.getEventKey(), deserializePayload(event)).get();
            event.markPublished(Instant.now());
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            event.recordFailure(ex);
        } catch (ExecutionException | RuntimeException ex) {
            event.recordFailure(unwrap(ex));
        }

        outboxEventRepository.save(event);
    }

    private Object deserializePayload(OutboxEvent event) {
        return switch (event.getEventType()) {
            case ProductOutboxEventTypes.PRODUCT_CREATED ->
                    objectMapper.readValue(event.getPayload(), ProductCreatedEvent.class);
            default -> throw new IllegalArgumentException("Unsupported outbox event type: " + event.getEventType());
        };
    }

    private Throwable unwrap(Throwable error) {
        if ((error instanceof ExecutionException || error instanceof CompletionException)
                && error.getCause() != null) {
            return error.getCause();
        }
        return error;
    }
}
