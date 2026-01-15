package com.aszender.orders.kafka.inbox;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.time.Instant;

@Entity
@Table(
        name = "kafka_inbox_messages",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_kafka_inbox_topic_partition_offset",
                        columnNames = {"topic", "partition_id", "offset_value"}
                )
        }
)
public class KafkaInboxMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String topic;

    @Column(name = "partition_id", nullable = false)
    private int partitionId;

    @Column(name = "offset_value", nullable = false)
    private long offsetValue;

    @Column(name = "received_at", nullable = false)
    private Instant receivedAt = Instant.now();

    protected KafkaInboxMessage() {
    }

    public KafkaInboxMessage(String topic, int partitionId, long offsetValue) {
        this.topic = topic;
        this.partitionId = partitionId;
        this.offsetValue = offsetValue;
    }

    public Long getId() {
        return id;
    }

    public String getTopic() {
        return topic;
    }

    public int getPartitionId() {
        return partitionId;
    }

    public long getOffsetValue() {
        return offsetValue;
    }

    public Instant getReceivedAt() {
        return receivedAt;
    }
}
