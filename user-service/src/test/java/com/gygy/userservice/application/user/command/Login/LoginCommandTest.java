package com.gygy.userservice.application.user.command.Login;

import com.gygy.common.security.JwtService;
import com.gygy.userservice.application.user.mapper.UserMapper;
import com.gygy.userservice.persistance.UserRepository;
import com.gygy.userservice.application.user.rule.UserRule;
import com.gygy.userservice.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class LoginCommandTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserRule userRule;

    @Mock
    private JwtService jwtService;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private LoginCommand.LoginCommandHandler handler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void handle_ValidCredentials_ReturnsToken() {
        // Arrange
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setEmail("test@example.com");
        user.setPassword("hashedPassword");

        LoginCommand command = new LoginCommand("test@example.com", "password");

        when(userRepository.findByEmail(any())).thenReturn(Optional.of(user));
        doNothing().when(userRule).checkUserExists(any());
        doNothing().when(userRule).checkPassword(any(), any());
        when(jwtService.generateToken(any())).thenReturn("token123");

        // Act
        LoginResponse response = handler.handle(command);

        // Assert
        assertNotNull(response);
        assertEquals("token123", response.getToken());
        verify(userRepository).findByEmail("test@example.com");
        verify(userRule).checkPassword(user, "password");
        verify(jwtService).generateToken(any());
    }
}