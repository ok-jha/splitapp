package com.ok_jha.splitapp.domain.entity;

import jakarta.persistence.*; // JPA standard annotations
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*; // Lombok annotations for boilerplate reduction
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter // Lombok: Generate getters for all fields
@Setter // Lombok: Generate setters for all fields
@Builder // Lombok: Generate builder pattern for object creation
@NoArgsConstructor // Lombok: Generate no-args constructor (required by JPA)
@AllArgsConstructor // Lombok: Generate all-args constructor
@Entity // JPA: Marks this class as a database entity
@Table(name = "app_users", // JPA: Specifies the table name in the database
        uniqueConstraints = { // JPA: Define database-level uniqueness constraints
                @UniqueConstraint(columnNames = "username", name = "uk_user_username"),
                @UniqueConstraint(columnNames = "email", name = "uk_user_email")
        })
public class User {

    @Id // JPA: Marks this field as the primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // JPA: Configures auto-generation of the ID
    // DB will handle generating unique IDs (suitable for Postgres)
    private Long id;

    @NotBlank // Validation: Ensure username is not null and not just whitespace
    @Size(min = 3, max = 50) // Validation: Ensure username length is within limits
    @Column(nullable = false, unique = true, length = 50) // JPA: Maps to a DB column, not nullable, unique
    private String username;

    @NotBlank // Validation: Ensure email is not null/whitespace
    @Email // Validation: Ensure email format is valid
    @Size(max = 100) // Validation: Set max length
    @Column(nullable = false, unique = true, length = 100) // JPA: Maps to a DB column, not nullable, unique
    private String email;

    @NotBlank // Validation: Ensure password is not null/whitespace (stores the HASH)
    @Size(min = 8, max = 255) // Validation: Min length applies before hashing, max length for hash storage
    @Column(nullable = false) // JPA: Maps to a DB column, not nullable
    private String password; // IMPORTANT: This will store the HASHED password, not plain text!

    // --- Auditing Fields ---
    @CreationTimestamp // Hibernate: Automatically set timestamp when entity is first created
    @Column(name = "created_at", nullable = false, updatable = false) // JPA: Column details
    private LocalDateTime createdAt;

    @UpdateTimestamp // Hibernate: Automatically set timestamp when entity is updated
    @Column(name = "updated_at", nullable = false) // JPA: Column details
    private LocalDateTime updatedAt;

    // --- Relationships (Add later) ---
    // @ManyToMany(...)
    // private Set<Group> groups = new HashSet<>();

    // --- equals() and hashCode() ---
    // Important for JPA entities, especially when dealing with collections/sets
    // Base implementation on the 'id' field once it's assigned.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        // Check if the object is null or if the proxy class is different
        if (o == null || getClass() != o.getClass() || !(o instanceof User)) return false;
        User user = (User) o;
        // If id is null, objects are equal only if they are the same instance
        // If id is not null, compare by id
        return id != null && Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        // Use a prime number multiplier
        // If id is null, return hash of the object instance (or a constant)
        // If id is not null, return hash of the id
        return id != null ? Objects.hash(id) : getClass().hashCode();
    }

    // --- toString() ---
    // Useful for logging, excluding sensitive fields like password
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                // DO NOT include password in toString()!
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
