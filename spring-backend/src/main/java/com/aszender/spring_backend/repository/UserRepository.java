package com.aszender.spring_backend.repository;

import com.aszender.spring_backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository for User entity.
 * Spring Data JPA generates the implementation automatically.
 */
public interface UserRepository extends JpaRepository<User, Long> {
    
    /**
     * Find a user by username.
     * Used by UserDetailsService to load user for authentication.
     */
    Optional<User> findByUsername(String username);
    
    /**
     * Check if a username already exists.
     * Used during registration to prevent duplicates.
     */
    boolean existsByUsername(String username);
}
