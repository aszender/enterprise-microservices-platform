package com.aszender.orders.kafka;

import org.junit.jupiter.api.Test;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

class KafkaRetryDlqConfigTest {

    @Test
    void dlqTopicUsesConfiguredSuffix() {
        assertEquals("orders.order-created.v1.DLQ", KafkaRetryDlqConfig.dlqTopic("orders.order-created.v1", ".DLQ"));
    }

    @Test
    void defaultErrorHandlerBeanCanBeCreated() {
        KafkaRetryDlqConfig config = new KafkaRetryDlqConfig();
        DeadLetterPublishingRecoverer recoverer = mock(DeadLetterPublishingRecoverer.class);

        DefaultErrorHandler errorHandler = config.kafkaDefaultErrorHandler(recoverer, 100L, 3L);

        assertNotNull(errorHandler);
    }
}
