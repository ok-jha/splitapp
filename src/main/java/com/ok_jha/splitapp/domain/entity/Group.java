package com.ok_jha.splitapp.domain.entity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "app_groups")
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Group name cannot be empty")
    @Size(min = 2, max = 100, message = "Group name must be between 2 and 100 characters")
    @Column(nullable = false, length = 100)
    private String name;

    // --- Relationships ---

    @ManyToOne(fetch = FetchType.LAZY) // Many groups can be created by one user
    @JoinColumn(name = "created_by_user_id", referencedColumnName = "id") // FK column in app_groups table
    private User createdBy; // The user who created the group

    @ManyToMany(fetch = FetchType.LAZY) // Many users can be in many groups
    @JoinTable(
            name = "app_group_members", // Name of the intermediate join table
            joinColumns = @JoinColumn(name = "group_id"), // Column in join table linking to Group
            inverseJoinColumns = @JoinColumn(name = "user_id") // Column in join table linking to User
    )
    @Builder.Default // Initialize the set during build process if using Lombok @Builder
    private Set<User> members = new HashSet<>(); // Users who are members of this group

    // One group can have many expenses (We'll add this later when Expense entity exists)
    // @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    // private Set<Expense> expenses = new HashSet<>();

    // --- Auditing Fields ---
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;


    // --- Utility methods for managing members (optional but helpful) ---

    public void addMember(User user) {
        this.members.add(user);
        // If User side also maintains the relationship (bi-directional)
        // user.getGroupsInternal().add(this); // Need to manage both sides carefully
    }

    public void removeMember(User user) {
        this.members.remove(user);
        // If User side also maintains the relationship (bi-directional)
        // user.getGroupsInternal().remove(this); // Need to manage both sides carefully
    }

    // --- equals() and hashCode() ---
    // Based on ID, similar to User entity
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass() || !(o instanceof Group)) return false;
        Group group = (Group) o;
        // If id is null, objects are equal only if they are the same instance
        // If id is not null, compare by id
        return id != null && Objects.equals(id, group.id);
    }

    @Override
    public int hashCode() {
        // Use a prime number multiplier
        // If id is null, return hash of the object instance (or a constant)
        // If id is not null, return hash of the id
        return id != null ? Objects.hash(id) : getClass().hashCode();
    }

    // --- toString() ---
    // Exclude potentially large collections like members for brevity in logs
    @Override
    public String toString() {
        return "Group{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", createdByUserId=" + (createdBy != null ? createdBy.getId() : "null") +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", memberCount=" + (members != null ? members.size() : 0) +
                '}';
    }
}
