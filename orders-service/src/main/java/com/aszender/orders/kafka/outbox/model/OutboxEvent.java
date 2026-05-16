package com.aszender.orders.kafka.outbox.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
        name = "outbox_messages",
        indexes = {
                @Index(name = "idx_outbox_messages_status_created_at", columnList = "status, created_at"),
                @Index(name = "idx_outbox_messages_aggregate", columnList = "aggregate_type, aggregate_id")
        }
)
public class OutboxEvent {

    private static final int LAST_ERROR_MAX_LENGTH = 2_000;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, updatable = false, unique = true)
    private String eventId = UUID.randomUUID().toString();

    @Column(nullable = false, updatable = false, name = "aggregate_type")
    private String aggregateType;

    @Column(nullable = false, updatable = false, name = "aggregate_id")
    private String aggregateId;

    @Column(nullable = false, updatable = false, name = "event_type")
    private String eventType;

    @Column(nullable = false, updatable = false)
    private String topic;

    @Column(nullable = false, updatable = false, name = "event_key")
    private String eventKey;

    @Lob
    @Column(nullable = false, updatable = false)
    private String payload;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OutboxEventStatus status = OutboxEventStatus.PENDING;

    @Column(nullable = false)
    private int attempts;

    @Column(length = LAST_ERROR_MAX_LENGTH, name = "last_error")
    private String lastError;

    @Column(nullable = false, updatable = false, name = "created_at")
    private Instant createdAt = Instant.now();

    @Column(name = "published_at")
    private Instant publishedAt;

    protected OutboxEvent() {
    }

    private OutboxEvent(
            String aggregateType,
            Long aggregateId,
            String eventType,
            String topic,
            String eventKey,
            String payload
    ) {
        this.aggregateType = aggregateType;
        this.aggregateId = String.valueOf(aggregateId);
        this.eventType = eventType;
        this.topic = topic;
        this.eventKey = eventKey;
        this.payload = payload;
    }

    public static OutboxEvent pending(
            String aggregateType,
            Long aggregateId,
            String eventType,
            String topic,
            String eventKey,
            String payload
    ) {
        return new OutboxEvent(aggregateType, aggregateId, eventType, topic, eventKey, payload);
    }

    public boolean isPublished() {
        return status == OutboxEventStatus.PUBLISHED;
    }

    public void markPublished(Instant publishedAt) {
        this.status = OutboxEventStatus.PUBLISHED;
        this.publishedAt = publishedAt;
        this.lastError = null;
    }

    public void recordFailure(Throwable error) {
        this.status = OutboxEventStatus.PENDING;
        this.attempts++;
        this.lastError = truncate(error.getMessage() == null ? error.toString() : error.getMessage());
    }

    private String truncate(String value) {
        if (value.length() <= LAST_ERROR_MAX_LENGTH) {
            return value;
        }
        return value.substring(0, LAST_ERROR_MAX_LENGTH);
    }

    public Long getId() {
        return id;
    }

    public String getEventId() {
        return eventId;
    }

    public String getAggregateType() {
        return aggregateType;
    }

    public String getAggregateId() {
        return aggregateId;
    }

    public String getEventType() {
        return eventType;
    }

    public String getTopic() {
        return topic;
    }

    public String getEventKey() {
        return eventKey;
    }

    public String getPayload() {
        return payload;
    }

    public OutboxEventStatus getStatus() {
        return status;
    }

    public int getAttempts() {
        return attempts;
    }

    public String getLastError() {
        return lastError;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getPublishedAt() {
        return publishedAt;
    }
}
