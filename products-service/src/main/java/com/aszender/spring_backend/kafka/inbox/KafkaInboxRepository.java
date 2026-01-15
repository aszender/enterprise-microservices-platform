package com.aszender.spring_backend.kafka.inbox;

import org.springframework.data.jpa.repository.JpaRepository;

public interface KafkaInboxRepository extends JpaRepository<KafkaInboxMessage, Long> {
    boolean existsByTopicAndPartitionIdAndOffsetValue(String topic, int partitionId, long offsetValue);
}
