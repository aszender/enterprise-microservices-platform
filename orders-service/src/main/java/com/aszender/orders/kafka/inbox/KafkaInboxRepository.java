package com.aszender.orders.kafka.inbox;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface KafkaInboxRepository extends JpaRepository<KafkaInboxMessage, Long> {
    Optional<KafkaInboxMessage> findByTopicAndPartitionIdAndOffsetValue(String topic, int partitionId, long offsetValue);
}
