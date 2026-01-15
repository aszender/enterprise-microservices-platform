package com.aszender.orders.kafka.inbox;

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
    public boolean tryConsume(ConsumerRecord<?, ?> record) {
        if (record == null) {
            return true;
        }

        String topic = record.topic();
        int partitionId = record.partition();
        long offsetValue = record.offset();

        if (repository.existsByTopicAndPartitionIdAndOffsetValue(topic, partitionId, offsetValue)) {
            return false;
        }

        repository.save(new KafkaInboxMessage(topic, partitionId, offsetValue));
        return true;
    }
}
