package com.gygy.userservice.application.user.command.Login;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.Assertions;

import com.gygy.userservice.persistance.UserRepository;
import com.gygy.userservice.application.user.rule.UserRule;
import com.gygy.userservice.core.jwt.JwtService;
import com.gygy.userservice.entity.User;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class LoginCommandTest {

    @Mock
    private UserRepository mockUserRepository;

    @Mock
    private UserRule mockUserRule;

    @Mock
    private JwtService mockJwtService;

    @InjectMocks
    private LoginCommand.LoginCommandHandler handler;

    @Test
    void givenValidCredentials_whenHandleLogin_thenReturnToken() {
        // Given
        String email = "john.doe@example.com";
        String password = "Password1!";
        LoginCommand command = new LoginCommand(email, password);

        User user = User.builder().id(UUID.randomUUID()).email(email).build();

        when(mockUserRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(mockJwtService.generateToken(user)).thenReturn("token123");

        // When
        LoginResponse response = handler.handle(command);

        // Then
        Assertions.assertNotNull(response);
        Assertions.assertEquals("token123", response.getToken());
        Assertions.assertEquals(email, response.getEmail());

        verify(mockUserRepository).findByEmail(email);
        verify(mockUserRule).checkUserExists(user);
        verify(mockUserRule).checkPassword(user, password);
    }
}