package com.gygy.userservice.application.user.rule;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.gygy.userservice.entity.User;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserRuleTest {

    @Mock
    private PasswordEncoder mockPasswordEncoder;

    @InjectMocks
    private UserRule userRule;

    @Test
    void givenNonNullUser_whenCheckUserNotExists_thenThrowsException() {
        // Given
        User user = User.builder().email("test@example.com").build();

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> userRule.checkUserNotExists(user));
        assertEquals("User already exists", exception.getMessage());
    }

    @Test
    void givenNullUser_whenCheckUserNotExists_thenDoesNotThrowException() {
        // Given
        User user = null;

        // When & Then
        assertDoesNotThrow(() -> userRule.checkUserNotExists(user));
    }

    @Test
    void givenNullUser_whenCheckUserExists_thenThrowsException() {
        // Given
        User user = null;

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> userRule.checkUserExists(user));
        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void givenNonNullUser_whenCheckUserExists_thenDoesNotThrowException() {
        // Given
        User user = User.builder().email("test@example.com").build();

        // When & Then
        assertDoesNotThrow(() -> userRule.checkUserExists(user));
    }

    @Test
    void givenIncorrectPassword_whenCheckPassword_thenThrowsException() {
        // Given
        User user = User.builder().email("test@example.com").password("encodedPassword").build();
        String password = "wrongPassword";

        when(mockPasswordEncoder.matches(password, user.getPassword())).thenReturn(false);

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> userRule.checkPassword(user, password));
        assertEquals("Invalid password", exception.getMessage());
    }

    @Test
    void givenCorrectPassword_whenCheckPassword_thenDoesNotThrowException() {
        // Given
        User user = User.builder().email("test@example.com").password("encodedPassword").build();
        String password = "correctPassword";

        when(mockPasswordEncoder.matches(password, user.getPassword())).thenReturn(true);

        // When & Then
        assertDoesNotThrow(() -> userRule.checkPassword(user, password));
    }
}