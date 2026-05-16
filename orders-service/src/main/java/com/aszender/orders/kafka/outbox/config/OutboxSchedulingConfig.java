package com.aszender.orders.kafka.outbox.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@Profile("kafka")
public class OutboxSchedulingConfig {
}
