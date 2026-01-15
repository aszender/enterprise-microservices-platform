package com.aszender.spring_backend.config;

import com.aszender.spring_backend.model.User;
import com.aszender.spring_backend.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Data initializer that runs on application startup.
 * Creates a default admin user if it doesn't exist.
 */
@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // Create default admin user if not exists
            if (!userRepository.existsByUsername("admin")) {
                User admin = new User(
                        "admin",
                        passwordEncoder.encode("admin123"),  // Password is encoded!
                        "ROLE_ADMIN"
                );
                userRepository.save(admin);
                System.out.println("✅ Default admin user created: admin / admin123");
            }

            // Create a regular user for testing
            if (!userRepository.existsByUsername("user")) {
                User user = new User(
                        "user",
                        passwordEncoder.encode("user123"),
                        "ROLE_USER"
                );
                userRepository.save(user);
                System.out.println("✅ Default user created: user / user123");
            }
        };
    }
}
