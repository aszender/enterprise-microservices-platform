package com.aszender.inventory.kafka.consume;

import com.aszender.inventory.kafka.events.ProductCreatedEvent;
import com.aszender.inventory.kafka.inbox.KafkaInboxService;
import com.aszender.inventory.service.InventoryService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ProductCreatedListenerTest {

    private InventoryService inventoryService;
    private KafkaInboxService inboxService;
    private ProductCreatedListener listener;

    @BeforeEach
    void setUp() {
        inventoryService = Mockito.mock(InventoryService.class);
        inboxService = Mockito.mock(KafkaInboxService.class);
        listener = new ProductCreatedListener(inventoryService, inboxService);
    }

    @Test
    void whenInboxReturnsFalse_shouldNotCallInventoryService() {
        // ProductCreatedEvent(productId: Long, name: String, price: Double, createdAt: String)
        ProductCreatedEvent event = new ProductCreatedEvent(1L, "name", 9.99, "2026-01-01T00:00:00Z");
        ConsumerRecord<String, ProductCreatedEvent> record = new ConsumerRecord<>("topic", 0, 100L, "key", event);

        when(inboxService.tryConsume(record)).thenReturn(false);

        listener.onProductCreated(event, record);

        verify(inventoryService, never()).ensureStockItemExists(1L);
    }
}
