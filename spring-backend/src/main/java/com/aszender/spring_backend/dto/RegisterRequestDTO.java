package com.aszender.spring_backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Request DTO for user registration.
 * 
 * Used by: POST /api/auth/register
 * 
 * Example JSON:
 * {
 *   "username": "newuser",
 *   "password": "securePass123"
 * }
 */
public record RegisterRequestDTO(
        @NotBlank(message = "Username is required")
        @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
        String username,

        @NotBlank(message = "Password is required")
        @Size(min = 6, message = "Password must be at least 6 characters")
        String password
) {}
