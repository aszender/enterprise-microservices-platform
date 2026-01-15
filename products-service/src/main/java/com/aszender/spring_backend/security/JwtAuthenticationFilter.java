package com.aszender.spring_backend.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWT Authentication Filter (Cookie-first)
 *
 * REAL FLOW (HttpOnly cookie JWT):
 *
 * 1) LOGIN
 *    Frontend calls: POST /api/auth/login
 *    Backend responds with:
 *      Set-Cookie: access_token=<JWT>; HttpOnly; Path=/; SameSite=Lax; Max-Age=...
 *
 *    Browser stores this cookie.
 *    Because it's HttpOnly, JavaScript cannot read the token.
 *
 * 2) NEXT REQUESTS (protected endpoints)
 *    Browser automatically sends the cookie back to the backend:
 *      Cookie: access_token=<JWT>
 *
 *    Notes:
 *    - For cross-origin requests, the frontend must use credentials/include and
 *      the backend must allow credentials via CORS.
 *
 * 3) THIS FILTER
 *    This filter intercepts EVERY HTTP request and:
 *    1. Resolves the JWT from either:
 *       - Cookie: access_token=<token> (primary)
 *       - Authorization: Bearer <token> (optional fallback for tools like Postman)
 *    2. Validates the token (signature + expiration)
 *    3. Sets the Authentication in Spring Security context
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String AUTH_COOKIE_NAME = "access_token";

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        // 1. Extract JWT from either:
        // - Authorization: Bearer <token>
        // - Cookie: access_token=<token> (HttpOnly)
        final String jwt = resolveToken(request);

        if (jwt == null || jwt.isBlank()) {
            filterChain.doFilter(request, response);
            return;
        }

        // 4. Extract username from token
        final String username = jwtService.extractUsername(jwt);

        // 5. If username exists and user is not already authenticated
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // 6. Load user from database
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            // 7. Validate token
            if (jwtService.isTokenValid(jwt, userDetails)) {

                // 8. Create authentication token
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,              // Principal (the user)
                        null,                     // Credentials (not needed, token is valid)
                        userDetails.getAuthorities()  // Authorities (roles)
                );

                // 9. Set request details
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // 10. Set authentication in SecurityContext
                // Now Spring Security knows this user is authenticated
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // 11. Continue filter chain
        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        final String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }

        Cookie[] cookies = request.getCookies();
        if (cookies == null) return null;

        for (Cookie cookie : cookies) {
            if (AUTH_COOKIE_NAME.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }
}
