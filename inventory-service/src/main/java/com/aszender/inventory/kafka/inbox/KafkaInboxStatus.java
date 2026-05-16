package com.aszender.inventory.kafka.inbox;

public enum KafkaInboxStatus {
    RECEIVED,
    PROCESSING,
    PROCESSED,
    FAILED
}
