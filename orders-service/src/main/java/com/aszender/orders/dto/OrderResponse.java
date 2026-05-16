package com.aszender.orders.dto;

import com.aszender.orders.model.OrderStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record OrderResponse(
        Long id,
        String customerName,
        OrderStatus status,
        BigDecimal total,
        Instant createdAt,
        List<OrderItemResponse> items
) {
}
