package com.gygy.userservice.application.user.command.ChangePassword;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.gygy.userservice.persistance.UserRepository;
import com.gygy.userservice.application.user.rule.UserRule;
import com.gygy.userservice.application.user.mapper.UserMapper;
import com.gygy.userservice.entity.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class ChangePasswordCommandTest {

    @Mock
    private UserRepository mockUserRepository;

    @Mock
    private UserRule mockUserRule;

    @Mock
    private UserMapper mockUserMapper;

    @Mock
    private PasswordEncoder mockPasswordEncoder;

    @InjectMocks
    private ChangePasswordCommand.ChangePasswordCommandHandler handler;

    @BeforeEach
    void setUp() {
        handler = new ChangePasswordCommand.ChangePasswordCommandHandler(mockUserRepository, mockUserRule,
                mockUserMapper, mockPasswordEncoder);
    }

    @Test
    void givenValidCommand_whenHandleChangePassword_thenReturnResponse() {
        // Given
        String email = "test@example.com";
        String oldPassword = "OldPassword1!";
        String newPassword = "NewPassword1!";
        ChangePasswordCommand command = new ChangePasswordCommand(email, oldPassword, newPassword);

        User user = User.builder()
                .email(email)
                .password("encodedOldPassword")
                .build();

        when(mockUserRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(mockPasswordEncoder.encode(newPassword)).thenReturn("encodedNewPassword");

        // When
        ChangePasswordResponse response = handler.handle(command);

        // Then
        assertNotNull(response);
        assertEquals("Password changed successfully", response.getMessage());

        verify(mockUserRepository).findByEmail(email);
        verify(mockUserRule).checkUserExists(user);
        verify(mockUserRule).checkPassword(user, oldPassword);
        verify(mockUserRepository).save(user);
    }
}