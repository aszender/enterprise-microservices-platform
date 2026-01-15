package com.aszender.orders.dto;

import com.aszender.orders.model.OrderStatus;

import java.time.Instant;
import java.util.List;

public record OrderResponse(
        Long id,
        String customerName,
        OrderStatus status,
        Double total,
        Instant createdAt,
        List<OrderItemResponse> items
) {
}
