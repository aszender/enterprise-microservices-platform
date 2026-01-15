package com.aszender.spring_backend.dto;

public record PResponseDTO(
        Long id,
        String name,
        String description,
        Double price
) {
}
