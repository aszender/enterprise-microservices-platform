package com.aszender.orders.kafka.outbox.service;

import com.aszender.orders.kafka.events.OrderCancelledEvent;
import com.aszender.orders.kafka.events.OrderCreatedEvent;
import com.aszender.orders.kafka.outbox.model.OutboxEvent;
import com.aszender.orders.kafka.outbox.model.OutboxEventStatus;
import com.aszender.orders.kafka.outbox.repository.OutboxEventRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;

@Service
@Profile("kafka")
public class OutboxPublisher {

    private final OutboxEventRepository outboxEventRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public OutboxPublisher(
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
        } catch (ExecutionException | JsonProcessingException | RuntimeException ex) {
            event.recordFailure(unwrap(ex));
        }

        outboxEventRepository.save(event);
    }

    private Object deserializePayload(OutboxEvent event) throws JsonProcessingException {
        return switch (event.getEventType()) {
            case OrderOutboxEventTypes.ORDER_CREATED -> objectMapper.readValue(event.getPayload(), OrderCreatedEvent.class);
            case OrderOutboxEventTypes.ORDER_CANCELLED -> objectMapper.readValue(event.getPayload(), OrderCancelledEvent.class);
            default -> throw new IllegalArgumentException("Unsupported outbox event type: " + event.getEventType());
        };
    }

    private Throwable unwrap(Throwable error) {
        if ((error instanceof ExecutionException || error instanceof CompletionException) && error.getCause() != null) {
            return error.getCause();
        }
        return error;
    }
}
