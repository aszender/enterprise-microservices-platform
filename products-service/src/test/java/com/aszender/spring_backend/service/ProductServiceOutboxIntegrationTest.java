package com.aszender.spring_backend.service;

import com.aszender.spring_backend.kafka.outbox.model.OutboxEventStatus;
import com.aszender.spring_backend.kafka.outbox.repository.OutboxEventRepository;
import com.aszender.spring_backend.model.Product;
import com.aszender.spring_backend.repository.ProductRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class ProductServiceOutboxIntegrationTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OutboxEventRepository outboxEventRepository;

    @AfterEach
    void cleanup() {
        outboxEventRepository.deleteAll();
        productRepository.deleteAll();
    }

    @Test
    void saveNewProduct_createsOutboxEventAtomically() {
        Product product = new Product("Gadget", "A cool gadget", BigDecimal.valueOf(49.99));

        productService.save(product);

        List<com.aszender.spring_backend.kafka.outbox.model.OutboxEvent> pending =
                outboxEventRepository.findTop50ByStatusOrderByCreatedAtAsc(OutboxEventStatus.PENDING);

        assertThat(pending).hasSize(1);
        assertThat(pending.get(0).getAggregateType()).isEqualTo("PRODUCT");
        assertThat(pending.get(0).getEventType()).isEqualTo("products.product-created.v1");
        assertThat(pending.get(0).getStatus()).isEqualTo(OutboxEventStatus.PENDING);
    }

    @Test
    void updateExistingProduct_doesNotCreateOutboxEvent() {
        Product product = new Product("Gadget", "A cool gadget", BigDecimal.valueOf(49.99));
        Product saved = productService.save(product);
        outboxEventRepository.deleteAll();

        saved.setName("Updated Gadget");
        productService.save(saved);

        assertThat(outboxEventRepository.findTop50ByStatusOrderByCreatedAtAsc(OutboxEventStatus.PENDING)).isEmpty();
    }
}
