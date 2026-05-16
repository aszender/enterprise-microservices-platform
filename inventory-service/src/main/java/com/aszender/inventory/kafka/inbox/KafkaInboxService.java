package com.aszender.inventory.kafka.inbox;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class KafkaInboxService {

    private final KafkaInboxRepository repository;

    public KafkaInboxService(KafkaInboxRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public ProcessingDecision beginProcessing(ConsumerRecord<?, ?> record) {
        return beginProcessing(record, null);
    }

    @Transactional
    public ProcessingDecision beginProcessing(ConsumerRecord<?, ?> record, EventMetadata metadata) {
        if (record == null) {
            return ProcessingDecision.process();
        }

        KafkaInboxMessage message = repository.findByTopicAndPartitionIdAndOffsetValue(
                        record.topic(),
                        record.partition(),
                        record.offset()
                )
                .orElse(null);

        if (message != null) {
            if (message.getStatus() == KafkaInboxStatus.PROCESSED) {
                return ProcessingDecision.duplicateProcessedDecision();
            }

            message.markProcessing();
            return ProcessingDecision.process();
        }

        message = new KafkaInboxMessage(
                record.topic(),
                record.partition(),
                record.offset(),
                metadata == null ? null : metadata.eventId(),
                metadata == null ? null : metadata.eventType(),
                metadata == null ? null : metadata.eventVersion()
        );
        message.markProcessing();
        repository.save(message);
        return ProcessingDecision.process();
    }

    @Transactional
    public void markProcessed(ConsumerRecord<?, ?> record) {
        if (record == null) {
            return;
        }

        KafkaInboxMessage message = findRequired(record);
        message.markProcessed();
    }

    @Transactional
    public void markFailed(ConsumerRecord<?, ?> record, Throwable failure) {
        if (record == null) {
            return;
        }

        KafkaInboxMessage message = findRequired(record);
        message.markFailed(failure);
    }

    private KafkaInboxMessage findRequired(ConsumerRecord<?, ?> record) {
        return repository.findByTopicAndPartitionIdAndOffsetValue(
                        record.topic(),
                        record.partition(),
                        record.offset()
                )
                .orElseThrow(() -> new IllegalStateException(
                        "Kafka inbox message not found for topic=%s partition=%d offset=%d"
                                .formatted(record.topic(), record.partition(), record.offset())
                ));
    }

    public record ProcessingDecision(boolean shouldProcess, boolean duplicateProcessed) {
        static ProcessingDecision process() {
            return new ProcessingDecision(true, false);
        }

        static ProcessingDecision duplicateProcessedDecision() {
            return new ProcessingDecision(false, true);
        }
    }

    public record EventMetadata(String eventId, String eventType, Integer eventVersion) {
    }
}
