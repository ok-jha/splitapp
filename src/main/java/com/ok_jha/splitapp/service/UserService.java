package com.ok_jha.splitapp.service;

import com.ok_jha.splitapp.domain.entity.User;

import java.util.Optional;

/**
 * Service layer interface for managing users.
 * Defines the business operations related to users.
 */
public interface UserService {

    /**
     * Registers a new user in the system.
     * Handles validation, password hashing, and persistence.
     *
     * @param username The desired username (must be unique).
     * @param email The user's email address (must be unique).
     * @param rawPassword The user's chosen plain text password.
     * @return The newly registered User object.
     * @throws IllegalArgumentException if username or email already exists, or input is invalid.
     */
    User registerUser(String username, String email, String rawPassword);

    /**
     * Finds a user by their username.
     *
     * @param username The username to search for.
     * @return An Optional containing the User if found, otherwise empty.
     */
    Optional<User> findByUsername(String username);

    /**
     * Finds a user by their email address.
     *
     * @param email The email to search for.
     * @return An Optional containing the User if found, otherwise empty.
     */
    Optional<User> findByEmail(String email);

    /**
     * Finds a user by their unique ID.
     *
     * @param id The ID of the user.
     * @return An Optional containing the User if found, otherwise empty.
     */
    Optional<User> findById(Long id);

    // We can add more methods later, like:
    // updateUserProfile(...)
    // changePassword(...)
    // etc.
}
