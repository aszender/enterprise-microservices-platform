package com.aszender.spring_backend.security;

import com.aszender.spring_backend.model.User;
import com.aszender.spring_backend.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Custom UserDetailsService implementation.
 * 
 * UserDetailsService is a core Spring Security interface used to retrieve
 * user-related data. It has one method: loadUserByUsername().
 * 
 * Spring Security calls this during authentication to:
 * 1. Load the user from the database
 * 2. Compare the provided password with the stored (encoded) password
 * 3. Check if the account is enabled, not locked, etc.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    // Constructor injection (recommended over @Autowired)
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Loads a user by username for authentication.
     * 
     * Called by Spring Security's AuthenticationManager when:
     * - User tries to log in
     * - Token/session needs to be validated
     * 
     * @param username the username to search for
     * @return UserDetails object (our User entity implements this)
     * @throws UsernameNotFoundException if user not found
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Find user in database
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "User not found with username: " + username
                ));

        // Our User entity implements UserDetails, so we can return it directly
        // If User didn't implement UserDetails, we'd wrap it like this:
        // return new org.springframework.security.core.userdetails.User(
        //     user.getUsername(),
        //     user.getPassword(),
        //     user.getAuthorities()
        // );

        return user;
    }
}
