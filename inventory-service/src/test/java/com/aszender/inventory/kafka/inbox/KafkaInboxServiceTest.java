package com.aszender.inventory.kafka.inbox;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class KafkaInboxServiceTest {

    private KafkaInboxRepository repository;
    private KafkaInboxService service;
    private ConsumerRecord<String, String> record;

    @BeforeEach
    void setUp() {
        repository = mock(KafkaInboxRepository.class);
        service = new KafkaInboxService(repository);
        record = new ConsumerRecord<>("orders.order-created.v1", 0, 42L, "key", "value");
    }

    @Test
    void duplicateProcessedMessageIsIgnored() {
        KafkaInboxMessage message = new KafkaInboxMessage(record.topic(), record.partition(), record.offset(), null, null, null);
        message.markProcessing();
        message.markProcessed();

        when(repository.findByTopicAndPartitionIdAndOffsetValue(record.topic(), record.partition(), record.offset()))
                .thenReturn(Optional.of(message));

        KafkaInboxService.ProcessingDecision decision = service.beginProcessing(record);

        assertFalse(decision.shouldProcess());
        assertTrue(decision.duplicateProcessed());
        assertEquals(KafkaInboxStatus.PROCESSED, message.getStatus());
    }

    @Test
    void failureDoesNotMarkMessageProcessed() {
        KafkaInboxMessage message = new KafkaInboxMessage(record.topic(), record.partition(), record.offset(), null, null, null);
        message.markProcessing();

        when(repository.findByTopicAndPartitionIdAndOffsetValue(record.topic(), record.partition(), record.offset()))
                .thenReturn(Optional.of(message));

        RuntimeException failure = new RuntimeException("boom");
        service.markFailed(record, failure);

        assertEquals(KafkaInboxStatus.FAILED, message.getStatus());
        assertNull(message.getProcessedAt());
        assertTrue(message.getLastError().contains("boom"));
    }

    @Test
    void failedMessageCanRetry() {
        KafkaInboxMessage message = new KafkaInboxMessage(record.topic(), record.partition(), record.offset(), null, null, null);
        message.markProcessing();
        message.markFailed(new RuntimeException("first attempt failed"));

        when(repository.findByTopicAndPartitionIdAndOffsetValue(record.topic(), record.partition(), record.offset()))
                .thenReturn(Optional.of(message));

        KafkaInboxService.ProcessingDecision decision = service.beginProcessing(record);

        assertTrue(decision.shouldProcess());
        assertEquals(KafkaInboxStatus.PROCESSING, message.getStatus());
        assertEquals(2, message.getAttempts());
        assertNull(message.getLastError());
    }

    @Test
    void newMessageStartsInProcessingState() {
        when(repository.findByTopicAndPartitionIdAndOffsetValue(record.topic(), record.partition(), record.offset()))
                .thenReturn(Optional.empty());
        when(repository.save(any(KafkaInboxMessage.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        KafkaInboxService.ProcessingDecision decision = service.beginProcessing(record);

        assertTrue(decision.shouldProcess());
        ArgumentCaptor<KafkaInboxMessage> captor = ArgumentCaptor.forClass(KafkaInboxMessage.class);
        org.mockito.Mockito.verify(repository).save(captor.capture());
        assertEquals(KafkaInboxStatus.PROCESSING, captor.getValue().getStatus());
        assertEquals(1, captor.getValue().getAttempts());
    }
}
