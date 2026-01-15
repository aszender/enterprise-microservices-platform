package com.aszender.spring_backend.dto;

import java.time.Instant;

public record StockStatusDTO(
        Long productId,
        Integer available,
        Integer threshold,
        boolean lowStock,
        Instant updatedAt
) {
}
