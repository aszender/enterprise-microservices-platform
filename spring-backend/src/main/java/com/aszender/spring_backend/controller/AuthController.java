package com.aszender.spring_backend.controller;

import com.aszender.spring_backend.dto.AuthRequestDTO;
import com.aszender.spring_backend.dto.AuthResponseDTO;
import com.aszender.spring_backend.dto.RegisterRequestDTO;
import com.aszender.spring_backend.model.User;
import com.aszender.spring_backend.repository.UserRepository;
import com.aszender.spring_backend.security.JwtService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.net.URI;

/**
 * Authentication Controller
 * 
 * Handles:
 * - POST /api/auth/login    → Authenticate user, set HttpOnly cookie with JWT
 * - POST /api/auth/register → Create new user account
 * - POST /api/auth/logout   → Clear auth cookie
 * - GET  /api/auth/me       → Get current user info (from SecurityContext)
 * 
 * AUTHENTICATION FLOW:
 * ┌────────────────────────────────────────────────────────────────────┐
 * │  1. Client sends POST /api/auth/login                              │
 * │     Body: { "username": "admin", "password": "admin123" }          │
 * └────────────────────────────────────────────────────────────────────┘
 *                              ↓
 * ┌────────────────────────────────────────────────────────────────────┐
 * │  2. AuthController.login()                                         │
 * │     - AuthenticationManager validates credentials                  │
 * │     - JwtService generates token                                   │
 * │     - Server returns Set-Cookie: access_token=<JWT>; HttpOnly ...  │
 * └────────────────────────────────────────────────────────────────────┘
 *                              ↓
 * ┌────────────────────────────────────────────────────────────────────┐
 * │  3. Browser stores the cookie                                      │
 * │     (Because it's HttpOnly, JS cannot read the JWT)                │
 * └────────────────────────────────────────────────────────────────────┘
 *                              ↓
 * ┌────────────────────────────────────────────────────────────────────┐
 * │  4. Browser automatically sends the cookie on next requests        │
 * │     Cookie: access_token=<JWT>                                    │
 * └────────────────────────────────────────────────────────────────────┘
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final String AUTH_COOKIE_NAME = "access_token";

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    public AuthController(
            AuthenticationManager authenticationManager,
            JwtService jwtService,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
        * Login endpoint - authenticates user and sets an HttpOnly cookie with JWT.
     * 
     * POST /api/auth/login
     * Body: { "username": "admin", "password": "admin123" }
     * 
     * Returns:
        * - 200 OK + Set-Cookie (HttpOnly JWT) if credentials are valid
     * - 401 Unauthorized if credentials are invalid
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody AuthRequestDTO request) {
        try {
            // 1. Authenticate using Spring Security's AuthenticationManager
            // This calls CustomUserDetailsService.loadUserByUsername()
            // and compares passwords using PasswordEncoder
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.username(),
                            request.password()
                    )
            );

            // 2. Get the authenticated user
            User user = (User) authentication.getPrincipal();

            // 3. Generate JWT token
            String token = jwtService.generateToken(user);

                // 4. Set token as HttpOnly cookie (safer than localStorage)
                ResponseCookie cookie = ResponseCookie.from(AUTH_COOKIE_NAME, token)
                    .httpOnly(true)
                    .secure(false) // localhost dev; set true behind HTTPS
                    .path("/")
                    .sameSite("Lax")
                    .maxAge(Duration.ofMillis(jwtExpiration))
                    .build();

                // 5. Return user info (token stays in cookie)
                return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, cookie.toString())
                    .body(new AuthResponseDTO(
                        null,
                        user.getUsername(),
                        user.getRole()
                    ));

        } catch (AuthenticationException e) {
            // Invalid credentials
            return ResponseEntity.status(401).build();
        }
    }

    /**
     * Register endpoint - creates a new user account.
     * 
     * POST /api/auth/register
     * Body: { "username": "newuser", "password": "password123" }
     * 
     * Returns:
        * - 201 Created + Set-Cookie (HttpOnly JWT) for the new user
     * - 409 Conflict if username already exists
     */
    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(@Valid @RequestBody RegisterRequestDTO request) {
        // 1. Check if username already exists
        if (userRepository.existsByUsername(request.username())) {
            return ResponseEntity.status(409).build();  // Conflict
        }

        // 2. Create new user with encoded password
        User newUser = new User(
                request.username(),
                passwordEncoder.encode(request.password()),  // BCrypt encode!
                "ROLE_USER"  // Default role for new users
        );

        // 3. Save to database
        User savedUser = userRepository.save(newUser);

        // 4. Generate JWT token for immediate login
        String token = jwtService.generateToken(savedUser);

        // 5. Set token as HttpOnly cookie
        ResponseCookie cookie = ResponseCookie.from(AUTH_COOKIE_NAME, token)
            .httpOnly(true)
            .secure(false) // localhost dev; set true behind HTTPS
            .path("/")
            .sameSite("Lax")
            .maxAge(Duration.ofMillis(jwtExpiration))
            .build();

        // 6. Return 201 Created with user info
        URI location = URI.create("/api/auth/user/" + savedUser.getId());
        return ResponseEntity.created(location)
            .header(HttpHeaders.SET_COOKIE, cookie.toString())
            .body(new AuthResponseDTO(
                null,
                savedUser.getUsername(),
                savedUser.getRole()
            ));
    }

    /**
     * Get current user info (requires authentication).
     * 
     * GET /api/auth/me
        * Auth is derived from the HttpOnly cookie (access_token).
        * (Authorization: Bearer <token> may work as a fallback for tooling.)
     */
    @GetMapping("/me")
    public ResponseEntity<AuthResponseDTO> getCurrentUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }

        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(new AuthResponseDTO(
            null,
                user.getUsername(),
                user.getRole()
        ));
    }

        /**
         * Logout endpoint - clears the auth cookie.
         *
         * POST /api/auth/logout
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
