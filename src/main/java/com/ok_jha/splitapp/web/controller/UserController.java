package com.ok_jha.splitapp.web.controller;

import com.ok_jha.splitapp.domain.entity.User;
import com.ok_jha.splitapp.service.UserService;
import com.ok_jha.splitapp.web.dto.RegisterUserRequest;
import com.ok_jha.splitapp.web.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*; // Spring Web annotations

import java.util.Optional;

@RestController // Spring: Combination of @Controller and @ResponseBody. Returns JSON/XML directly.
@RequestMapping("/api/v1/users") // Spring: Base path for all endpoints in this controller
@RequiredArgsConstructor // Lombok: For injecting final fields (UserService)
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserService userService; // Inject the service

    /**
     * Endpoint for registering a new user.
     * Handles POST requests to /api/v1/users/register
     */
    @PostMapping("/register") // Maps HTTP POST requests for the "/register" sub-path
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterUserRequest requestDto) {
        // @Valid: Triggers bean validation on the requestDto based on annotations (@NotBlank, etc.)
        // @RequestBody: Tells Spring to deserialize the JSON request body into the requestDto object
        log.info("Received registration request for username: {}", requestDto.getUsername());

        try {
            User newUser = userService.registerUser(
                    requestDto.getUsername(),
                    requestDto.getEmail(),
                    requestDto.getPassword()
            );

            // Convert the User entity to a safe UserResponse DTO
            UserResponse responseDto = convertToResponseDto(newUser);

            // Return HTTP 201 Created status with the user details (without password)
            return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);

        } catch (IllegalArgumentException e) {
            // Handle specific exceptions from the service (e.g., user already exists)
            log.warn("Registration failed: {}", e.getMessage());
            // Return HTTP 400 Bad Request with the error message
            // TODO: Implement global exception handling (@RestControllerAdvice) later for cleaner code
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            // Catch unexpected errors
            log.error("Unexpected error during registration for user {}", requestDto.getUsername(), e);
            // Return HTTP 500 Internal Server Error
            // TODO: Implement global exception handling
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }

    /**
     * Endpoint for retrieving a user by username.
     * Handles GET requests to /api/v1/users/username/{username}
     */
    @GetMapping("/username/{username}") // Maps HTTP GET requests with a path variable
    public ResponseEntity<UserResponse> getUserByUsername(@PathVariable String username) {
        // @PathVariable: Extracts the value from the URL path
        log.debug("Received request to find user by username: {}", username);
        Optional<User> userOptional = userService.findByUsername(username);

        return userOptional
                .map(this::convertToResponseDto) // Convert User to UserResponse if present
                .map(ResponseEntity::ok) // If present, wrap in ResponseEntity with status 200 OK
                .orElseGet(() -> ResponseEntity.notFound().build()); // If not present, return 404 Not Found
    }

    /**
     * Endpoint for retrieving a user by ID.
     * Handles GET requests to /api/v1/users/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        log.debug("Received request to find user by ID: {}", id);
        Optional<User> userOptional = userService.findById(id);
        return userOptional
                .map(this::convertToResponseDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    // --- Private Helper Method for DTO Conversion ---
    // TODO: Consider using a mapping library like MapStruct for more complex scenarios
    private UserResponse convertToResponseDto(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

}
