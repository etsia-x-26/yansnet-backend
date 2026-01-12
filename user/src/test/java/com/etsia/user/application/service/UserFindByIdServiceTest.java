package com.etsia.user.application.service;

import com.etsia.common.domain.model.UserDto;
import com.etsia.user.domain.repository.UserRepository;
import com.etsia.user.domain.service.UUserDomainService;
import com.etsia.user.infrastructure.exception.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserFindByIdServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UUserDomainService uUserDomainService;

    @InjectMocks
    private UserFindByIdService userFindByIdService;

    private UserDto testUser;

    @BeforeEach
    void setUp() {
        testUser = UserDto.builder()
                .id(1)
                .name("Test User")
                .username("testuser")
                .isActive(true)
                .build();
    }

    @Test
    void exec_shouldReturnUser_whenUserExists() {
        // Arrange
        Integer userId = 1;
        when(uUserDomainService.FindById(userId)).thenReturn(Optional.of(testUser));
        when(userRepository.FindById(userId)).thenReturn(Optional.of(testUser));

        // Act
        Optional<UserDto> result = userFindByIdService.exec(userId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(testUser.getId(), result.get().getId());
        assertEquals(testUser.getName(), result.get().getName());
        verify(uUserDomainService, times(1)).FindById(userId);
        verify(userRepository, times(1)).FindById(userId);
    }

    @Test
    void exec_shouldThrowException_whenUserNotFound() {
        // Arrange
        Integer userId = 999;
        when(uUserDomainService.FindById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        UserNotFoundException exception = assertThrows(
                UserNotFoundException.class,
                () -> userFindByIdService.exec(userId)
        );

        assertEquals("User not found", exception.getMessage());
        verify(uUserDomainService, times(1)).FindById(userId);
        verify(userRepository, never()).FindById(anyInt());
    }
}
