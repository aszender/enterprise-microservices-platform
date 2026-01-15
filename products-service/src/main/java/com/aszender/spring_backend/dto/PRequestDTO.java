package com.aszender.spring_backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record PRequestDTO(
        @NotBlank String name,
        String description,
        @NotNull @Positive Double price
) {
}
