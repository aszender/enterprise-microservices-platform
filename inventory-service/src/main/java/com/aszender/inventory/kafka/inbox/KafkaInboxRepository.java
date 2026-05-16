package com.aszender.inventory.kafka.inbox;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface KafkaInboxRepository extends JpaRepository<KafkaInboxMessage, Long> {
    boolean existsByTopicAndPartitionIdAndOffsetValue(String topic, int partitionId, long offsetValue);

    Optional<KafkaInboxMessage> findByTopicAndPartitionIdAndOffsetValue(String topic, int partitionId, long offsetValue);
}
