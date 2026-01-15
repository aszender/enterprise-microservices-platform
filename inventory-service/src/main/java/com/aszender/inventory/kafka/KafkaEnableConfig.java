package com.aszender.inventory.kafka;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.EnableKafka;

@EnableKafka
@Configuration
@Profile("kafka")
public class KafkaEnableConfig {
}
