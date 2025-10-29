package com.etsia.user.infrastructure.controller;


import com.etsia.common.domain.model.UserDto;
import com.etsia.user.application.service.*;
import com.etsia.user.domain.model.dto.request.user.CreateUserDto;
import com.etsia.user.domain.model.dto.request.user.UserUpdateDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RestController
@RequestMapping("/api/user")
@CrossOrigin("*")
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

    @GetMapping("/{id}")
    public ResponseEntity<Optional<UserDto>> FindById(@PathVariable Integer id) {
        Optional<UserDto> userDto = userFindByIdService.exec(id);
        return ResponseEntity.ok(userDto);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<Optional<UserDto>> FindByEmail(@PathVariable String email) {
        Optional<UserDto> userDto = userFindByEmailService.exec(email);
        return ResponseEntity.ok(userDto);
    }

    @GetMapping("/email/{email}/exists")
    public ResponseEntity<Boolean> existsByEmail(@PathVariable String email) {
        Boolean exists = userexistsByEmailService.exec(email);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/auth")
    public ResponseEntity<Optional<UserDto>> FindByEmailAndPassword(
            @RequestParam String email,
            @RequestParam String password) {
        Optional<UserDto> userDto = userFindByEmailAndPasswordService.exec(email, password);
        return ResponseEntity.ok(userDto);
    }

    @PostMapping
    public ResponseEntity<UserDto> Save(@RequestBody CreateUserDto user) {
        UserDto savedUser = userSaveService.execute(user);
        return ResponseEntity.ok(savedUser);
    }

    @PutMapping
    public ResponseEntity<UserDto> update(@RequestBody UserUpdateDto user) {
        UserDto updatedUser = userupdateService.exec(user);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> Delete(@PathVariable Integer id) {
        userDeleteService.execute(id);
        return ResponseEntity.noContent().build();
    }
}