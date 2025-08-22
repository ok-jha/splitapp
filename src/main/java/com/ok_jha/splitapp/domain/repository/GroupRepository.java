package com.ok_jha.splitapp.domain.repository;

import com.ok_jha.splitapp.domain.entity.Group;
import com.ok_jha.splitapp.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {

    // Find a group by its exact name (case-sensitive)
    Optional<Group> findByName(String name);

    // Find all groups where a specific user is a member
    // Spring Data JPA understands 'Members' (the field name) and 'Containing'
    // It will generate a query that joins with the app_group_members table.
    List<Group> findByMembersContaining(User member);

    // Find all groups created by a specific user
    // Spring Data JPA understands 'CreatedBy' (the field name)
    List<Group> findByCreatedBy(User creator);

    // Check if a group exists with a specific name (more efficient than findByName if only existence is needed)
    boolean existsByName(String name);

}
