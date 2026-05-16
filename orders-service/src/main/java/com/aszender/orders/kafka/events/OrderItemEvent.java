package com.aszender.orders.kafka.events;

import java.math.BigDecimal;

public record OrderItemEvent(
        Long productId,
        Integer quantity,
        BigDecimal unitPrice
) {
}
