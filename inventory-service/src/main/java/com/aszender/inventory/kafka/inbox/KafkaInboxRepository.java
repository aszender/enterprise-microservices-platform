package com.aszender.inventory.kafka.inbox;

import org.springframework.data.jpa.repository.JpaRepository;

public interface KafkaInboxRepository extends JpaRepository<KafkaInboxMessage, Long> {
    boolean existsByTopicAndPartitionIdAndOffsetValue(String topic, int partitionId, long offsetValue);
}
