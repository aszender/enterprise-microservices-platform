package com.aszender.orders.kafka.consume;

import com.aszender.orders.kafka.events.StockReservedEvent;
import com.aszender.orders.model.OrderStatus;
import com.aszender.orders.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Profile("kafka")
public class StockReservedListener {

    private static final Logger log = LoggerFactory.getLogger(StockReservedListener.class);

    private final OrderService orderService;

    public StockReservedListener(OrderService orderService) {
        this.orderService = orderService;
    }

    @KafkaListener(
            topics = "${app.kafka.topics.stock-reserved}",
            groupId = "orders-service",
            properties = {
                    "spring.json.value.default.type=com.aszender.orders.kafka.events.StockReservedEvent"
            }
    )
    public void onStockReserved(StockReservedEvent event) {
        log.info("Received StockReservedEvent: {}", event);
        if (event == null || event.orderId() == null) {
            return;
        }
        orderService.updateStatus(event.orderId(), OrderStatus.RESERVED);
    }
}
