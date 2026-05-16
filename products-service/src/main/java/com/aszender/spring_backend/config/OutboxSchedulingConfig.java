package com.aszender.spring_backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@Profile("kafka")
@EnableScheduling
public class OutboxSchedulingConfig {
}
