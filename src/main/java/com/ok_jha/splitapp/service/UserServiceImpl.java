package com.ok_jha.splitapp.service;

import com.ok_jha.splitapp.domain.entity.User;
import com.ok_jha.splitapp.domain.repository.UserRepository;
import jakarta.validation.Validator; // Optional: For programmatic validation if needed
import lombok.RequiredArgsConstructor; // Lombok: Generates constructor for final fields
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // For managing DB transactions

import java.util.Optional;

@Service // Spring: Marks this class as a Service bean (component scanning finds it)
@RequiredArgsConstructor // Lombok: Generates a constructor injecting final fields (userRepository, passwordEncoder)
public class UserServiceImpl implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    // Dependencies are injected via constructor (thanks to @RequiredArgsConstructor)
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    // private final Validator validator; // Optional: Inject if programmatic validation needed

    @Override
    @Transactional // Spring: Ensures this whole method runs within a single database transaction
    // If any part fails (exception thrown), DB changes are rolled back.
    public User registerUser(String username, String email, String rawPassword) {
        log.info("Attempting to register user with username: {} and email: {}", username, email);

        // --- Basic Input Validation (More comprehensive validation often happens at Controller level) ---
        if (username == null || username.isBlank() || email == null || email.isBlank() || rawPassword == null || rawPassword.isEmpty()) {
            throw new IllegalArgumentException("Username, email, and password cannot be empty.");
        }
        // Basic length/format checks could go here, but @Valid on Controller DTOs is better.

        // --- Check for Existing User ---
        // Why check both?: Ensures data integrity based on our unique constraints.
        if (userRepository.existsByUsername(username)) {
            log.warn("Registration failed: Username '{}' already exists.", username);
            throw new IllegalArgumentException("Username already taken.");
        }
        if (userRepository.existsByEmail(email)) {
            log.warn("Registration failed: Email '{}' already exists.", email);
            throw new IllegalArgumentException("Email address already registered.");
        }

        // --- Hash Password ---
        // Why?: NEVER store plain text passwords!
        String hashedPassword = passwordEncoder.encode(rawPassword);

        // --- Create User Entity ---
        // Using the Builder pattern generated by Lombok for cleaner object creation
        User newUser = User.builder()
                .username(username.trim()) // Trim whitespace
                .email(email.trim().toLowerCase()) // Trim whitespace and store lowercase email consistently
                .password(hashedPassword) // Store the HASHED password
                // createdAt and updatedAt will be handled by @CreationTimestamp / @UpdateTimestamp
                .build();

        // --- Persist User ---
        User savedUser = userRepository.save(newUser);
        log.info("Successfully registered user with ID: {}", savedUser.getId());

        // Important: Return the saved user (which now has an ID and timestamps)
        // WARNING: This User object still contains the password HASH.
        // In a real API, you'd typically convert this to a UserDTO (Data Transfer Object)
        // that *excludes* the password before returning it from the Controller.
        return savedUser;
    }

    @Override
    @Transactional(readOnly = true) // Optimization: Marks transaction as read-only (DB might optimize)
    public Optional<User> findByUsername(String username) {
        log.debug("Finding user by username: {}", username);
        return userRepository.findByUsername(username);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByEmail(String email) {
        log.debug("Finding user by email: {}", email);
        // Consider searching by lowercase email if you store it that way
        return userRepository.findByEmail(email.toLowerCase());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findById(Long id) {
        log.debug("Finding user by ID: {}", id);
        return userRepository.findById(id);
    }
}
