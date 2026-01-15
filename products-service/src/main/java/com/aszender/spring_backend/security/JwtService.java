package com.aszender.spring_backend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * JWT Service - Handles all JWT token operations.
 * 
 * RESPONSIBILITIES:
 * 1. Generate tokens (when user logs in)
 * 2. Validate tokens (on every request)
 * 3. Extract claims (username, expiration, etc.)
 * 
 * JWT STRUCTURE:
 * ┌─────────────────────────────────────────────────────────────┐
 * │  Header.Payload.Signature                                   │
 * │                                                             │
 * │  Header:    {"alg": "HS256", "typ": "JWT"}                  │
 * │  Payload:   {"sub": "admin", "iat": 1234, "exp": 5678}      │
 * │  Signature: HMACSHA256(header + "." + payload, SECRET_KEY)  │
 * └─────────────────────────────────────────────────────────────┘
 */
@Service
public class JwtService {

    // Secret key for signing tokens (from application.properties)
    @Value("${jwt.secret}")
    private String secretKey;

    // Token expiration time in milliseconds (from application.properties)
    @Value("${jwt.expiration}")
    private long jwtExpiration;

    // ═══════════════════════════════════════════════════════════
    // TOKEN GENERATION
    // ═══════════════════════════════════════════════════════════

    /**
     * Generate a JWT token for a user.
     * Called after successful authentication (login).
     */
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    /**
     * Generate a JWT token with extra claims.
     * Extra claims can include roles, permissions, user ID, etc.
     */
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts.builder()
                .claims(extraClaims)                              // Custom data (roles, etc.)
                .subject(userDetails.getUsername())               // Who this token is for
                .issuedAt(new Date(System.currentTimeMillis()))   // When token was created
                .expiration(new Date(System.currentTimeMillis() + jwtExpiration))  // When it expires
                .signWith(getSigningKey())                        // Sign with secret key
                .compact();                                       // Build the token string
    }

    // ═══════════════════════════════════════════════════════════
    // TOKEN VALIDATION
    // ═══════════════════════════════════════════════════════════

    /**
     * Validate a JWT token.
     * Checks if token is for the right user and not expired.
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    /**
     * Check if token is expired.
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // ═══════════════════════════════════════════════════════════
    // CLAIM EXTRACTION
    // ═══════════════════════════════════════════════════════════

    /**
     * Extract username from token.
     * The "subject" claim contains the username.
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extract expiration date from token.
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Generic method to extract any claim from token.
     * Uses a function to specify which claim to extract.
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Parse the token and extract all claims.
     * This verifies the signature automatically.
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())   // Verify signature with our secret
                .build()
                .parseSignedClaims(token)      // Parse the token
                .getPayload();                 // Get the claims (payload)
    }

    // ═══════════════════════════════════════════════════════════
    // KEY MANAGEMENT
    // ═══════════════════════════════════════════════════════════

    /**
     * Get the signing key from the secret string.
     * The secret is Base64 encoded in application.properties.
     */
    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
