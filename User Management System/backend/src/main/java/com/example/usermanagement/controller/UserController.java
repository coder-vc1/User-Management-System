package com.example.usermanagement.controller;

import com.example.usermanagement.dto.UserResponseDto;
import com.example.usermanagement.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User Management", description = "APIs for managing users")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001"})
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @Operation(summary = "Get all users", description = "Retrieve all users from the database")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved users",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class)))
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        logger.info("Request received to get all users");
        List<UserResponseDto> users = userService.getAllUsers();
        logger.info("Returning {} users", users.size());
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID", description = "Retrieve a specific user by their ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<UserResponseDto> getUserById(
            @Parameter(description = "User ID", required = true) @PathVariable Long id) {
        logger.info("Request received to get user by id: {}", id);
        UserResponseDto user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/email/{email}")
    @Operation(summary = "Get user by email", description = "Retrieve a specific user by their email address")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<UserResponseDto> getUserByEmail(
            @Parameter(description = "User email", required = true) @PathVariable String email) {
        logger.info("Request received to get user by email: {}", email);
        UserResponseDto user = userService.getUserByEmail(email);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/search")
    @Operation(summary = "Search users", 
               description = "Search users by ID, email, firstName, lastName, or SSN. Supports exact ID/email matching and partial matching for names/SSN. Minimum 3 characters for full-text search.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Search completed successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid search parameters")
    })
    public ResponseEntity<List<UserResponseDto>> searchUsers(
            @Parameter(description = "Search term for ID, email, firstName, lastName, or SSN", required = false)
            @RequestParam(value = "q", required = false) String searchTerm) {
        
        logger.info("Request received to search users with term: '{}'", searchTerm);
        List<UserResponseDto> users = userService.searchUsers(searchTerm);
        logger.info("Search returned {} users", users.size());
        
        return ResponseEntity.ok(users);
    }
}