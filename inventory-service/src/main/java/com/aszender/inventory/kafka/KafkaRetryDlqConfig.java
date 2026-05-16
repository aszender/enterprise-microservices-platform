package com.aszender.inventory.kafka;

import org.apache.kafka.common.TopicPartition;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

@Configuration
@Profile("kafka")
public class KafkaRetryDlqConfig {

    @Bean
    public DeadLetterPublishingRecoverer kafkaDeadLetterPublishingRecoverer(
            KafkaTemplate<String, Object> kafkaTemplate,
            @Value("${app.kafka.dlq.suffix:.DLQ}") String dlqSuffix
    ) {
        return new DeadLetterPublishingRecoverer(
                kafkaTemplate,
                (record, exception) -> new TopicPartition(dlqTopic(record.topic(), dlqSuffix), record.partition())
        );
    }

    @Bean
    public DefaultErrorHandler kafkaDefaultErrorHandler(
            DeadLetterPublishingRecoverer recoverer,
            @Value("${app.kafka.retry.interval-ms:1000}") long retryIntervalMs,
            @Value("${app.kafka.retry.max-attempts:3}") long maxAttempts
    ) {
        long retryAttemptsAfterFirstDelivery = Math.max(0, maxAttempts - 1);
        return new DefaultErrorHandler(recoverer, new FixedBackOff(retryIntervalMs, retryAttemptsAfterFirstDelivery));
    }

    static String dlqTopic(String sourceTopic, String suffix) {
        return sourceTopic + suffix;
    }
}
