package com.todolist.service;

import com.todolist.model.User;
import com.todolist.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Service for User registration and Spring Security authentication.
 * Implements UserDetailsService so Spring Security can load users from the DB.
 */
@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository  = userRepository;
    }

    /**
     * Register a new user with a plain-text password for the assignment demo.
     *
     * @throws IllegalArgumentException if username or email already taken
     */
    public User register(String username, String email, String password) {
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Username '" + username + "' is already taken.");
        }
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email '" + email + "' is already registered.");
        }

        User newUser = User.builder()
                .username(username)
                .email(email)
                .password(password)
                .build();

        return userRepository.save(newUser);
    }

    /**
     * Load user by username for Spring Security authentication.
     * Called automatically during the login process.
     */
    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        User user = findByUsernameOrEmail(login)
                .orElseThrow(() -> new UsernameNotFoundException(
                    "User not found: " + login));

        // Return a Spring Security User object with one simple authority.
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                java.util.List.of(new org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_USER"))
        );
    }

    /**
     * Find a user entity by username (used internally after authentication).
     */
    public User findByUsername(String username) {
        return findByUsernameOrEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

    private java.util.Optional<User> findByUsernameOrEmail(String login) {
        java.util.Optional<User> user = userRepository.findByUsername(login);
        if (user.isPresent()) {
            return user;
        }
        return userRepository.findByEmail(login);
    }
}
