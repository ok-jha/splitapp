package com.ok_jha.splitapp.domain.repository;
import com.ok_jha.splitapp.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository // Spring: Marks this interface as a Repository bean (component scanning finds it)
// Extends JpaRepository for CRUD methods
public interface UserRepository extends JpaRepository<User, Long> {

    // --- Custom Query Methods --- will have later...
    // Spring Data JPA will automatically generate the implementation for these methods
    // based on their names.

    /**
     * Finds a user by their username.
     * Why Optional?: The user might not exist, Optional cleanly handles this possibility
     *              without returning null.
     * @param username The username to search for.
     * @return An Optional containing the User if found, otherwise an empty Optional.
     */
    Optional<User> findByUsername(String username);

    /**
     * Finds a user by their email address.
     * Why Optional?: Same reason as findByUsername.
     * @param email The email address to search for.
     * @return An Optional containing the User if found, otherwise an empty Optional.
     */
    Optional<User> findByEmail(String email);

    /**
     * Checks if a user exists with the given username.
     * Why boolean?: More efficient than fetching the whole User object if we only
     *               need to know about existence (e.g., during registration validation).
     * @param username The username to check.
     * @return true if a user with this username exists, false otherwise.
     */
    boolean existsByUsername(String username);

    /**
     * Checks if a user exists with the given email address.
     * Why boolean?: Same reason as existsByUsername.
     * @param email The email address to check.
     * @return true if a user with this email exists, false otherwise.
     */
    boolean existsByEmail(String email);

}
