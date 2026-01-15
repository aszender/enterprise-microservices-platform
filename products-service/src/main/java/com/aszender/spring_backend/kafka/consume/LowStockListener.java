package com.aszender.spring_backend.kafka.consume;

import com.aszender.spring_backend.kafka.events.LowStockEvent;
import com.aszender.spring_backend.kafka.inbox.KafkaInboxService;
import com.aszender.spring_backend.model.ProductStockStatus;
import com.aszender.spring_backend.repository.ProductStockStatusRepository;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@Profile("kafka")
public class LowStockListener {

    private static final Logger log = LoggerFactory.getLogger(LowStockListener.class);

    private final KafkaInboxService inboxService;
    private final ProductStockStatusRepository stockStatusRepository;

    public LowStockListener(KafkaInboxService inboxService, ProductStockStatusRepository stockStatusRepository) {
        this.inboxService = inboxService;
        this.stockStatusRepository = stockStatusRepository;
    }

    @KafkaListener(
            topics = "${app.kafka.topics.low-stock}",
            groupId = "products-service",
            properties = {
                    "spring.json.value.default.type=com.aszender.spring_backend.kafka.events.LowStockEvent"
            }
    )
    public void onLowStock(LowStockEvent event, ConsumerRecord<String, LowStockEvent> record) {
        if (!inboxService.tryConsume(record)) {
            return;
        }

        if (event == null || event.productId() == null) {
            return;
        }

        boolean lowStock = event.available() <= event.threshold();

        ProductStockStatus status = stockStatusRepository.findById(event.productId())
                .orElseGet(() -> new ProductStockStatus(event.productId(), event.available(), event.threshold(), lowStock, Instant.now()));

        status.setAvailable(event.available());
        status.setThreshold(event.threshold());
        status.setLowStock(lowStock);
        status.setUpdatedAt(event.detectedAt() != null ? event.detectedAt() : Instant.now());

        stockStatusRepository.save(status);

        log.info("Low stock projection updated: productId={}, available={}, threshold={}",
                event.productId(), event.available(), event.threshold());
    }
}
