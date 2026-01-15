package com.aszender.spring_backend.model;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * User entity implementing UserDetails for Spring Security.
 * 
 * UserDetails is the core interface that Spring Security uses to represent
 * a user. By implementing it directly on our entity, we avoid creating
 * a separate wrapper class.
 */
@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String role;  // e.g., "ROLE_USER", "ROLE_ADMIN"

    @Column(nullable = false)
    private boolean enabled = true;

    // ═══════════════════════════════════════════════
    // CONSTRUCTORS
    // ═══════════════════════════════════════════════
    
    public User() {}

    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.enabled = true;
    }

    // ═══════════════════════════════════════════════
    // UserDetails INTERFACE METHODS
    // Spring Security calls these to check authentication
    // ═══════════════════════════════════════════════

    /**
     * Returns the authorities (roles/permissions) granted to the user.
     * Spring Security uses this to check authorization.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Convert our role string to a GrantedAuthority
        // e.g., "ROLE_ADMIN" → SimpleGrantedAuthority("ROLE_ADMIN")
        return List.of(new SimpleGrantedAuthority(role));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    /**
     * Indicates whether the user's account has expired.
     * An expired account cannot be authenticated.
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;  // We don't track account expiration
    }

    /**
     * Indicates whether the user is locked or unlocked.
     * A locked user cannot be authenticated.
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;  // We don't track account locking
    }

    /**
     * Indicates whether the user's credentials (password) has expired.
     * Expired credentials prevent authentication.
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;  // We don't track credential expiration
    }

    /**
     * Indicates whether the user is enabled or disabled.
     * A disabled user cannot be authenticated.
     */
    @Override
    public boolean isEnabled() {
        return enabled;
    }

    // ═══════════════════════════════════════════════
    // GETTERS AND SETTERS
    // ═══════════════════════════════════════════════

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
