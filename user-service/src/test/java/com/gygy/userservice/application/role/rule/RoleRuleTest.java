package com.gygy.userservice.application.role.rule;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Assertions;
import com.gygy.userservice.entity.Role;
import java.util.List;
import java.util.UUID;
import java.util.Arrays;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RoleRuleTest {

    private final RoleRule roleRule = new RoleRule();

    @Test
    void givenNullRole_whenCheckRoleExists_thenThrowsException() {
        // Given
        Role role = null;

        // When & Then
        Exception exception = assertThrows(RuntimeException.class, () -> roleRule.checkRoleExists(role));
        assertEquals("Role not found", exception.getMessage());
    }

    @Test
    void givenNonNullRole_whenCheckRoleExists_thenDoesNotThrowException() {
        // Given
        Role role = Role.builder().id(UUID.randomUUID()).name("Admin").build();

        // When & Then
        Assertions.assertDoesNotThrow(() -> roleRule.checkRoleExists(role));

    }

    @Test
    void givenNonNullRole_whenCheckRoleNotExists_thenThrowsException() {
        // Given
        Role role = Role.builder().id(UUID.randomUUID()).name("Admin").build();

        // When & Then
        Assertions.assertThrows(RuntimeException.class, () -> roleRule.checkRoleNotExists(role));
    }

    @Test
    void givenNullRole_whenCheckRoleNotExists_thenDoesNotThrowException() {
        // Given
        Role role = null;

        // When & Then
        Assertions.assertDoesNotThrow(() -> roleRule.checkRoleNotExists(role));
    }

    @Test
    void givenNullRoleName_whenCheckRoleName_thenThrowsException() {
        // Given
        String roleName = null;

        // When & Then
        Assertions.assertThrows(RuntimeException.class, () -> roleRule.checkRoleName(roleName));
    }

    @Test
    void givenEmptyRoleName_whenCheckRoleName_thenThrowsException() {
        // Given
        String roleName = "";

        // When & Then
        Assertions.assertThrows(RuntimeException.class, () -> roleRule.checkRoleName(roleName));
    }

    @Test
    void givenValidRoleName_whenCheckRoleName_thenDoesNotThrowException() {
        // Given
        String roleName = "Admin";

        // When & Then
        Assertions.assertDoesNotThrow(() -> roleRule.checkRoleName(roleName));
    }

    @Test
    void givenNullRolesList_whenCheckRolesExists_thenThrowsException() {
        // Given
        List<Role> roles = null;

        // When & Then
        Assertions.assertThrows(RuntimeException.class, () -> roleRule.checkRolesExists(roles));
    }

    @Test
    void givenEmptyRolesList_whenCheckRolesExists_thenThrowsException() {
        // Given
        List<Role> roles = Arrays.asList();

        // When & Then
        Assertions.assertThrows(RuntimeException.class, () -> roleRule.checkRolesExists(roles));
    }

    @Test
    void givenValidRolesList_whenCheckRolesExists_thenDoesNotThrowException() {
        // Given
        Role role1 = Role.builder().id(UUID.randomUUID()).name("Admin").build();
        Role role2 = Role.builder().id(UUID.randomUUID()).name("User").build();
        List<Role> roles = Arrays.asList(role1, role2);

        // When & Then
        Assertions.assertDoesNotThrow(() -> roleRule.checkRolesExists(roles));
    }
}