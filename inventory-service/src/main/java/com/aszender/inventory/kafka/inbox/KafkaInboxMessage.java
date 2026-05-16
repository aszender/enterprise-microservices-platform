package com.aszender.inventory.kafka.inbox;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
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

    @Column(name = "event_id")
    private String eventId;

    @Column(name = "event_type")
    private String eventType;

    @Column(name = "event_version")
    private Integer eventVersion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private KafkaInboxStatus status = KafkaInboxStatus.RECEIVED;

    @Column(nullable = false)
    private int attempts;

    @Column(name = "last_error", length = 2000)
    private String lastError;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt = Instant.now();

    @Column(name = "processing_started_at")
    private Instant processingStartedAt;

    @Column(name = "processed_at")
    private Instant processedAt;

    @Column(name = "failed_at")
    private Instant failedAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt = Instant.now();

    protected KafkaInboxMessage() {
    }

    public KafkaInboxMessage(String topic, int partitionId, long offsetValue, String eventId, String eventType, Integer eventVersion) {
        this.topic = topic;
        this.partitionId = partitionId;
        this.offsetValue = offsetValue;
        this.eventId = eventId;
        this.eventType = eventType;
        this.eventVersion = eventVersion;
    }

    @PrePersist
    @PreUpdate
    void touch() {
        updatedAt = Instant.now();
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

    public String getEventId() {
        return eventId;
    }

    public String getEventType() {
        return eventType;
    }

    public Integer getEventVersion() {
        return eventVersion;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public KafkaInboxStatus getStatus() {
        return status;
    }

    public int getAttempts() {
        return attempts;
    }

    public String getLastError() {
        return lastError;
    }

    public Instant getProcessingStartedAt() {
        return processingStartedAt;
    }

    public Instant getProcessedAt() {
        return processedAt;
    }

    public Instant getFailedAt() {
        return failedAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void markProcessing() {
        status = KafkaInboxStatus.PROCESSING;
        attempts++;
        processingStartedAt = Instant.now();
        failedAt = null;
        lastError = null;
    }

    public void markProcessed() {
        status = KafkaInboxStatus.PROCESSED;
        processedAt = Instant.now();
        lastError = null;
    }

    public void markFailed(Throwable failure) {
        status = KafkaInboxStatus.FAILED;
        failedAt = Instant.now();
        lastError = truncateError(failure);
    }

    private String truncateError(Throwable failure) {
        if (failure == null) {
            return null;
        }

        String message = failure.getClass().getName() + ": " + failure.getMessage();
        if (message.length() <= 2000) {
            return message;
        }
        return message.substring(0, 2000);
    }
}
