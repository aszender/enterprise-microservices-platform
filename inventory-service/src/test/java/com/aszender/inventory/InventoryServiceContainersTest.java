package com.aszender.inventory;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.kafka.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

@SpringBootTest(properties = "app.grpc.server.enabled=false")
@Testcontainers(disabledWithoutDocker = true)
@ActiveProfiles({"test", "kafka"})
class InventoryServiceContainersTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    @Container
    static KafkaContainer kafka = new KafkaContainer(DockerImageName.parse("apache/kafka:3.8.0"));

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.flyway.enabled", () -> true);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "validate");
        registry.add("INVENTORY_DATASOURCE_URL", postgres::getJdbcUrl);
        registry.add("INVENTORY_DATASOURCE_USERNAME", postgres::getUsername);
        registry.add("INVENTORY_DATASOURCE_PASSWORD", postgres::getPassword);
        registry.add("KAFKA_BOOTSTRAP_SERVERS", kafka::getBootstrapServers);
    }

    @Test
    void contextLoadsWithPostgresAndKafkaContainers() {
    }
}
