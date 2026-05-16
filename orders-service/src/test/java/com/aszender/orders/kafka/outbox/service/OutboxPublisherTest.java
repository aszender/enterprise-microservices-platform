package com.aszender.orders.kafka.outbox.service;

import com.aszender.orders.kafka.events.OrderCreatedEvent;
import com.aszender.orders.kafka.outbox.model.OutboxEvent;
import com.aszender.orders.kafka.outbox.model.OutboxEventStatus;
import com.aszender.orders.kafka.outbox.repository.OutboxEventRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OutboxPublisherTest {

    @Mock
    private OutboxEventRepository outboxEventRepository;

    @Mock
    private KafkaTemplate<String, Object> kafkaTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    private OutboxPublisher outboxPublisher;

    @BeforeEach
    void setUp() {
        outboxPublisher = new OutboxPublisher(outboxEventRepository, kafkaTemplate, objectMapper);
    }

    @Test
    void publishPending_whenSendFails_leavesEventPendingAndRecordsFailure() throws Exception {
        OutboxEvent event = orderCreatedOutboxEvent();
        CompletableFuture<SendResult<String, Object>> failedSend = new CompletableFuture<>();
        failedSend.completeExceptionally(new IllegalStateException("broker down"));

        when(outboxEventRepository.findTop50ByStatusOrderByCreatedAtAsc(OutboxEventStatus.PENDING))
                .thenReturn(List.of(event));
        when(kafkaTemplate.send(eq("orders.order-created.v1"), eq("1"), any(OrderCreatedEvent.class)))
                .thenReturn(failedSend);

        outboxPublisher.publishPending();

        assertThat(event.getStatus()).isEqualTo(OutboxEventStatus.PENDING);
        assertThat(event.getAttempts()).isEqualTo(1);
        assertThat(event.getLastError()).contains("broker down");
        assertThat(event.getPublishedAt()).isNull();
        verify(outboxEventRepository).save(event);
    }

    @Test
    void publishPending_whenSendSucceeds_marksEventPublished() throws Exception {
        OutboxEvent event = orderCreatedOutboxEvent();
        CompletableFuture<SendResult<String, Object>> successfulSend = CompletableFuture.completedFuture(null);

        when(outboxEventRepository.findTop50ByStatusOrderByCreatedAtAsc(OutboxEventStatus.PENDING))
                .thenReturn(List.of(event));
        when(kafkaTemplate.send(eq("orders.order-created.v1"), eq("1"), any(OrderCreatedEvent.class)))
                .thenReturn(successfulSend);

        outboxPublisher.publishPending();

        assertThat(event.getStatus()).isEqualTo(OutboxEventStatus.PUBLISHED);
        assertThat(event.getAttempts()).isZero();
        assertThat(event.getLastError()).isNull();
        assertThat(event.getPublishedAt()).isNotNull();
        verify(outboxEventRepository).save(event);
    }

    @Test
    void publishPending_whenCalledAgain_skipsAlreadyPublishedEvent() throws Exception {
        OutboxEvent event = orderCreatedOutboxEvent();
        CompletableFuture<SendResult<String, Object>> successfulSend = CompletableFuture.completedFuture(null);

        when(outboxEventRepository.findTop50ByStatusOrderByCreatedAtAsc(OutboxEventStatus.PENDING))
                .thenReturn(List.of(event), List.of(event));
        when(kafkaTemplate.send(eq("orders.order-created.v1"), eq("1"), any(OrderCreatedEvent.class)))
                .thenReturn(successfulSend);

        outboxPublisher.publishPending();
        outboxPublisher.publishPending();

        assertThat(event.getStatus()).isEqualTo(OutboxEventStatus.PUBLISHED);
        verify(kafkaTemplate, times(1)).send(eq("orders.order-created.v1"), eq("1"), any(OrderCreatedEvent.class));
        verify(outboxEventRepository, times(1)).save(event);
    }

    @Test
    void publishPending_whenRepositoryReturnsPublishedEvent_doesNotSend() throws Exception {
        OutboxEvent event = orderCreatedOutboxEvent();
        event.markPublished(Instant.now());

        when(outboxEventRepository.findTop50ByStatusOrderByCreatedAtAsc(OutboxEventStatus.PENDING))
                .thenReturn(List.of(event));

        outboxPublisher.publishPending();

        verifyNoInteractions(kafkaTemplate);
        verify(outboxEventRepository, never()).save(event);
    }

    private OutboxEvent orderCreatedOutboxEvent() {
        return OutboxEvent.pending(
                "ORDER",
                1L,
                OrderOutboxEventTypes.ORDER_CREATED,
                "orders.order-created.v1",
                "1",
                """
                        {
                          "eventId": "evt-1",
                          "eventType": "OrderCreated",
                          "eventVersion": 1,
                          "aggregateId": "1",
                          "aggregateType": "Order",
                          "occurredAt": null,
                          "correlationId": null,
                          "producer": "orders-service",
                          "payload": {
                            "orderId": 1,
                            "customerName": "Ada",
                            "total": 25.00,
                            "createdAt": "2026-01-01T00:00:00Z",
                            "items": [
                              {
                                "productId": 10,
                                "quantity": 2,
                                "unitPrice": 12.50
                              }
                            ]
                          }
                        }
                        """
        );
    }
}
