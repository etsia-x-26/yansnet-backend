package com.etsia.user.application.service;

import com.etsia.common.domain.model.UserDto;
import com.etsia.user.domain.model.dto.request.user.CreateUserDto;
import com.etsia.user.domain.repository.UserRepository;
import com.etsia.user.domain.service.UUserDomainService;
import com.etsia.user.infrastructure.exception.EmailNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserSaveServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UUserDomainService uUserDomainService;

    @InjectMocks
    private UserSaveService userSaveService;

    private CreateUserDto createUserDto;
    private UserDto savedUserDto;

    @BeforeEach
    void setUp() {
        createUserDto = CreateUserDto.builder()
                .email("test@example.com")
                .password("password123")
                .name("Test User")
                .username("testuser")
                .isActive(true)
                .build();

        savedUserDto = UserDto.builder()
                .id(1)
                .name("Test User")
                .username("testuser")
                .isActive(true)
                .build();
    }

    @Test
    void execute_shouldSaveUser_whenEmailIsUnique() {
        // Arrange
        when(uUserDomainService.IsEmailUnique("test@example.com")).thenReturn(true);
        when(userRepository.Save(any(CreateUserDto.class))).thenReturn(savedUserDto);

        // Act
        UserDto result = userSaveService.execute(createUserDto);

        // Assert
        assertNotNull(result);
        assertEquals(savedUserDto.getId(), result.getId());
        assertEquals(savedUserDto.getName(), result.getName());
        verify(uUserDomainService, times(1)).IsEmailUnique("test@example.com");
        verify(userRepository, times(1)).Save(createUserDto);
    }

    @Test
    void execute_shouldThrowException_whenEmailAlreadyExists() {
        // Arrange
        when(uUserDomainService.IsEmailUnique("test@example.com")).thenReturn(false);

        // Act & Assert
        EmailNotFoundException exception = assertThrows(
                EmailNotFoundException.class,
                () -> userSaveService.execute(createUserDto)
        );

        assertEquals("Email already used", exception.getMessage());
        verify(uUserDomainService, times(1)).IsEmailUnique("test@example.com");
        verify(userRepository, never()).Save(any(CreateUserDto.class));
    }
}
