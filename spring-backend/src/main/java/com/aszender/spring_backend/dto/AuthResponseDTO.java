package com.aszender.spring_backend.dto;

/**
 * Response DTO for successful authentication.
 * 
 * Returned by: POST /api/auth/login
 * 
 * Example JSON:
 * {
 *   "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
 *   "type": "Bearer",
 *   "username": "admin",
 *   "role": "ROLE_ADMIN"
 * }
 */
public record AuthResponseDTO(
        String token,
        String type,
        String username,
        String role
) {
    // Convenience constructor with default token type
    public AuthResponseDTO(String token, String username, String role) {
        this(token, "Bearer", username, role);
    }
}
