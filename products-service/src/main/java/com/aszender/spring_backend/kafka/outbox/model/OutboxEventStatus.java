package com.aszender.spring_backend.kafka.outbox.model;

public enum OutboxEventStatus {
    PENDING, PUBLISHED, FAILED
}
