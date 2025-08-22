package com.ok_jha.splitapp.service;

import com.ok_jha.splitapp.domain.entity.Group;
import com.ok_jha.splitapp.domain.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface GroupService {

    /**
     * Creates a new group.
     * The creator is automatically added as the first member.
     *
     * @param groupName The desired name for the group.
     * @param creatorId The ID of the user creating the group.
     * @return The newly created Group object.
     * @throws UserNotFoundException if the creator user doesn't exist.
     * @throws IllegalArgumentException if the group name is invalid.
     */
    Group createGroup(String groupName,Long creatorId);

    /**
     * Finds a group by its unique ID.
     * Might fetch members depending on use case (consider separate methods if needed).
     *
     * @param groupId The ID of the group to find.
     * @return An Optional containing the Group if found, otherwise empty.
     */
    Optional<Group> findGroupById(Long groupId);

    /**
     * Adds a user as a member to an existing group.
     *
     * @param groupId The ID of the group.
     * @param userIdToAdd The ID of the user to add.
     * @param requestingUserId The ID of the user making the request (for permission checks later).
     * @return The updated Group object.
     * @throws GroupNotFoundException if the group doesn't exist.
     * @throws UserNotFoundException if the user to add doesn't exist.
     * @throws UserAlreadyInGroupException if the user is already a member.
     * @throws AccessDeniedException if the requesting user doesn't have permission (to be implemented).
     */
    Group addMemberToGroup(Long groupId, Long userIdToAdd, Long requestingUserId); // Added requesting user for future security

    /**
     * Removes a user from a group.
     *
     * @param groupId The ID of the group.
     * @param userIdToRemove The ID of the user to remove.
     * @param requestingUserId The ID of the user making the request.
     * @return The updated Group object.
     * @throws GroupNotFoundException
     * @throws UserNotFoundException if the user to remove is not found (or not in group).
     * @throws AccessDeniedException
     * @throws CannotRemoveLastMemberException or similar if business logic requires it.
     */
    Group removeMemberFromGroup(Long groupId, Long userIdToRemove, Long requestingUserId);

    /**
     * Retrieves all groups where a specific user is a member.
     *
     * @param userId The ID of the user.
     * @return A List of Groups the user is a member of (can be empty).
     * @throws UserNotFoundException if the user doesn't exist.
     */
    List<Group> findGroupsByMember(Long userId);

    /**
     * Retrieves the set of members for a specific group.
     * Explicitly fetches members which might otherwise be lazy-loaded.
     *
     * @param groupId The ID of the group.
     * @return A Set of User objects who are members.
     * @throws GroupNotFoundException if the group doesn't exist.
     */
    Set<User> getGroupMembers(Long groupId);

    void deleteGroup(Long groupId, Long requestingUserId);
    Group updateGroupDetails(Long groupId, String newName, Long requestingUserId);


}
