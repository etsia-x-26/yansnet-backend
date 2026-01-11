package com.etsia.user.infrastructure.controller;


import com.etsia.common.domain.model.UserDto;
import com.etsia.user.application.service.*;
import com.etsia.user.domain.model.dto.request.user.CreateUserDto;
import com.etsia.user.domain.model.dto.request.user.UserUpdateDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RestController
@RequestMapping("/api/user")
@Tag(name = "User Management", description = "Endpoints for managing user profiles and information")
public class UserController {

    private final UserDeleteService userDeleteService;
    private final UserexistsByEmailService userexistsByEmailService;
    private final UserFindByEmailAndPasswordService userFindByEmailAndPasswordService;
    private final UserFindByEmailService userFindByEmailService;
    private final UserFindByIdService userFindByIdService;
    private final UserSaveService userSaveService;
    private final UserupdateService userupdateService;

    public UserController(UserDeleteService userDeleteService, UserexistsByEmailService userexistsByEmailService, UserFindByEmailAndPasswordService userFindByEmailAndPasswordService, UserFindByEmailService userFindByEmailService, UserFindByIdService userFindByIdService, UserSaveService userSaveService, UserupdateService userupdateService) {
        this.userDeleteService = userDeleteService;
        this.userexistsByEmailService = userexistsByEmailService;
        this.userFindByEmailAndPasswordService = userFindByEmailAndPasswordService;
        this.userFindByEmailService = userFindByEmailService;
        this.userFindByIdService = userFindByIdService;
        this.userSaveService = userSaveService;
        this.userupdateService = userupdateService;
    }

    @Operation(summary = "Get user by ID", description = "Retrieves a user's details using their unique ID")
    @ApiResponse(responseCode = "200", description = "User found")
    @ApiResponse(responseCode = "404", description = "User not found")
    @GetMapping("/{id}")
    public ResponseEntity<Optional<UserDto>> FindById(@PathVariable Integer id) {
        Optional<UserDto> userDto = userFindByIdService.exec(id);
        return ResponseEntity.ok(userDto);
    }

    @Operation(summary = "Get user by email", description = "Retrieves a user's details using their email address")
    @ApiResponse(responseCode = "200", description = "User found")
    @ApiResponse(responseCode = "404", description = "User not found")
    @GetMapping("/email/{email}")
    public ResponseEntity<Optional<UserDto>> FindByEmail(@PathVariable String email) {
        Optional<UserDto> userDto = userFindByEmailService.exec(email);
        return ResponseEntity.ok(userDto);
    }

    @Operation(summary = "Check if email exists", description = "Checks if a user with the given email already exists")
    @ApiResponse(responseCode = "200", description = "Existence check successful")
    @GetMapping("/email/{email}/exists")
    public ResponseEntity<Boolean> existsByEmail(@PathVariable String email) {
        Boolean exists = userexistsByEmailService.exec(email);
        return ResponseEntity.ok(exists);
    }

    @Operation(summary = "Authenticate user (legacy)", description = "Authenticates using email and password. Use /auth/login instead.")
    @ApiResponse(responseCode = "200", description = "Authentication successful")
    @GetMapping("/auth")
    public ResponseEntity<Optional<UserDto>> FindByEmailAndPassword(
            @RequestParam String email,
            @RequestParam String password) {
        Optional<UserDto> userDto = userFindByEmailAndPasswordService.exec(email, password);
        return ResponseEntity.ok(userDto);
    }

    @Operation(summary = "Create user", description = "Creates a new user manually (internal use)")
    @ApiResponse(responseCode = "200", description = "User created successfully")
    @PostMapping
    public ResponseEntity<UserDto> Save(@RequestBody CreateUserDto user) {
        UserDto savedUser = userSaveService.execute(user);
        return ResponseEntity.ok(savedUser);
    }

    @Operation(summary = "Update user", description = "Updates an existing user's profile information")
    @ApiResponse(responseCode = "200", description = "User updated successfully")
    @PutMapping
    public ResponseEntity<UserDto> update(@RequestBody UserUpdateDto user) {
        UserDto updatedUser = userupdateService.exec(user);
        return ResponseEntity.ok(updatedUser);
    }

    @Operation(summary = "Delete user", description = "Removes a user account permanently")
    @ApiResponse(responseCode = "204", description = "User deleted successfully")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> Delete(@PathVariable Integer id) {
        userDeleteService.execute(id);
        return ResponseEntity.noContent().build();
    }
}