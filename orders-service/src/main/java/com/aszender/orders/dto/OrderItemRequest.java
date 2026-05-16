package com.aszender.orders.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record OrderItemRequest(
        @NotNull Long productId,
        @NotNull @Min(1) Integer quantity,
        @NotNull @DecimalMin(value = "0.00") @Digits(integer = 15, fraction = 4) BigDecimal unitPrice
) {
}
