package com.aszender.orders.dto;

public record OrderItemResponse(
        Long id,
        Long productId,
        Integer quantity,
        Double unitPrice
) {
}
