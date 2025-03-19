package com.gygy.userservice.application.role.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.Assertions;
import com.gygy.userservice.persistance.RoleRepository;
import com.gygy.userservice.application.role.rule.RoleRule;
import com.gygy.userservice.entity.Role;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.Arrays;
import org.mockito.Spy;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class RoleServiceImplTest {

    @Mock
    private RoleRepository mockRoleRepository;

    @Spy
    private RoleRule mockRoleRule;

    @InjectMocks
    private RoleServiceImpl roleService;

    @Test
    void givenValidRoleName_whenGetRoleByName_thenReturnRole() {
        // Given
        String roleName = "Admin";
        Role role = Role.builder().id(UUID.randomUUID()).name(roleName).build();

        when(mockRoleRepository.findByName(roleName)).thenReturn(Optional.of(role));

        // When
        Role result = roleService.getRoleByName(roleName);

        // Then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(roleName, result.getName());
        verify(mockRoleRepository).findByName(roleName);
        verify(mockRoleRule).checkRoleExists(role);
    }

    @Test
    void givenInvalidRoleName_whenGetRoleByName_thenThrowsException() {
        // Given
        String roleName = "NonExistentRole";

        when(mockRoleRepository.findByName(roleName)).thenReturn(Optional.empty());

        // When & Then
        Assertions.assertThrows(RuntimeException.class, () -> roleService.getRoleByName(roleName));
        verify(mockRoleRepository).findByName(roleName);
    }

    @Test
    void givenValidRoleNames_whenGetRolesByNames_thenReturnRoles() {
        // Given
        List<String> roleNames = Arrays.asList("Admin", "User");
        Role role1 = Role.builder().id(UUID.randomUUID()).name("Admin").build();
        Role role2 = Role.builder().id(UUID.randomUUID()).name("User").build();
        List<Role> roles = Arrays.asList(role1, role2);

        when(mockRoleRepository.findAllByNameIn(roleNames)).thenReturn(roles);

        // When
        List<Role> result = roleService.getRolesByNames(roleNames);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(mockRoleRepository).findAllByNameIn(roleNames);
        verify(mockRoleRule).checkRolesExists(roles);
    }

    @Test
    void givenInvalidRoleNames_whenGetRolesByNames_thenThrowsException() {
        // Given
        List<String> roleNames = Arrays.asList("NonExistentRole1", "NonExistentRole2");

        when(mockRoleRepository.findAllByNameIn(roleNames)).thenReturn(Arrays.asList());

        // When & Then
        Assertions.assertThrows(RuntimeException.class, () -> roleService.getRolesByNames(roleNames));
        verify(mockRoleRepository).findAllByNameIn(roleNames);
    }

    @Test
    void whenGetAllRoles_thenReturnAllRoles() {
        // Given
        Role role1 = Role.builder().id(UUID.randomUUID()).name("Admin").build();
        Role role2 = Role.builder().id(UUID.randomUUID()).name("User").build();
        List<Role> roles = Arrays.asList(role1, role2);

        when(mockRoleRepository.findAll()).thenReturn(roles);

        // When
        List<Role> result = roleService.getAllRoles();

        // Then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(2, result.size());
        verify(mockRoleRepository).findAll();
    }
}