package com.aszender.spring_backend.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Request DTO for login/authentication.
 * 
 * Used by: POST /api/auth/login
 * 
 * Example JSON:
 * {
 *   "username": "admin",
 *   "password": "admin123"
 * }
 */
public record AuthRequestDTO(
        @NotBlank(message = "Username is required")
        String username,

        @NotBlank(message = "Password is required")
        String password
) {}
