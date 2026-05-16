package com.aszender.spring_backend.dto;

import java.math.BigDecimal;

public record PResponseDTO(
        Long id,
        String name,
        String description,
        BigDecimal price
) {
}
