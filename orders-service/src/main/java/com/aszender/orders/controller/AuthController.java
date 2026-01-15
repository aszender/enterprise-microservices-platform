package com.aszender.orders.controller;

import com.aszender.orders.dto.AuthMeResponseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final String AUTH_COOKIE_NAME = "access_token";

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    /**
        * This service does not issue JWTs. Use products-service /api/auth/login.
     * This endpoint just confirms the current authenticated identity.
     */
    @GetMapping("/me")
    public ResponseEntity<AuthMeResponseDTO> me(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }

        return ResponseEntity.ok(new AuthMeResponseDTO(authentication.getName()));
    }

    /**
     * Clears the shared auth cookie.
     */
    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        ResponseCookie cookie = ResponseCookie.from(AUTH_COOKIE_NAME, "")
                .httpOnly(true)
                .secure(false)
                .path("/")
                .sameSite("Lax")
                .maxAge(Duration.ZERO)
                .build();

        return ResponseEntity.noContent()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .build();
    }
}
