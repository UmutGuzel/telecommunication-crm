package com.gygy.userservice.application.role.command.CreateRole;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import com.gygy.userservice.persistance.RoleRepository;
import com.gygy.userservice.application.role.mapper.RoleMapper;
import com.gygy.userservice.application.role.rule.RoleRule;
import com.gygy.userservice.entity.Role;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.Assertions;

@ExtendWith(MockitoExtension.class)
class CreateRoleCommandTest {
    @Mock
    private RoleRepository mockRoleRepository;
    @Mock
    private RoleMapper mockRoleMapper;
    @Mock
    private RoleRule mockRoleRule;

    @InjectMocks
    private CreateRoleCommand.CreateRoleCommandHandler handler;

    private RoleMapper roleMapper = new RoleMapper();

    @Test
    void givenValidCommand_whenHandleCreateRole_thenReturnRoleResponse() {
        // Given
        CreateRoleCommand command = new CreateRoleCommand();
        command.setName("testRole");
        command.setDescription("testDescription");
        List<UUID> permissionIds = Arrays.asList(UUID.randomUUID(), UUID.randomUUID());
        command.setPermissionIds(permissionIds);

        Role mappedRole = roleMapper.toEntity(command);

        Role savedRole = roleMapper.toEntity(command);
        savedRole.setId(UUID.randomUUID());

        when(mockRoleRepository.findByName(command.getName())).thenReturn(Optional.empty());
        when(mockRoleMapper.toEntity(command)).thenReturn(mappedRole);
        when(mockRoleRepository.save(mappedRole)).thenReturn(savedRole);
        doNothing().when(mockRoleRule).checkRoleNotExists(null);

        // When
        CreateRoleResponse response = handler.handle(command);

        // Then
        Assertions.assertNotNull(response);
        Assertions.assertEquals(savedRole.getId(), response.getId());
        Assertions.assertEquals(savedRole.getName(), response.getName());
        Assertions.assertEquals(savedRole.getDescription(), response.getDescription());

        verify(mockRoleRepository).findByName(command.getName());
        verify(mockRoleRule).checkRoleNotExists(null);
        verify(mockRoleMapper).toEntity(command);
        verify(mockRoleRepository).save(mappedRole);
    }

    @Test
    void givenRoleExists_whenHandleCreateRole_thenThrowException() {
        // Given
        CreateRoleCommand command = new CreateRoleCommand();
        command.setName("existingRole");
        command.setDescription("testDescription");

        Role existingRole = Role.builder()
                .id(UUID.randomUUID())
                .name(command.getName())
                .description(command.getDescription())
                .build();

        when(mockRoleRepository.findByName(command.getName())).thenReturn(Optional.of(existingRole));
        doThrow(new RuntimeException("Role already exists")).when(mockRoleRule).checkRoleNotExists(existingRole);

        // When
        assertThrows(RuntimeException.class, () -> handler.handle(command));

        // Then
        verify(mockRoleRepository).findByName(command.getName());
        verify(mockRoleRule).checkRoleNotExists(existingRole);
        verify(mockRoleRepository, never()).save(any());
    }

}