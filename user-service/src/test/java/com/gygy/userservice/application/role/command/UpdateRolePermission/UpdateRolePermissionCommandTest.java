package com.gygy.userservice.application.role.command.UpdateRolePermission;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.gygy.userservice.persistance.RoleRepository;
import com.gygy.userservice.application.permission.service.PermissionService;
import com.gygy.userservice.application.role.mapper.RoleMapper;
import com.gygy.userservice.application.role.rule.RoleRule;
import com.gygy.userservice.entity.Role;
import com.gygy.userservice.entity.Permission;
import java.util.List;
import java.util.UUID;
import java.util.Optional;
import java.util.Arrays;
import org.junit.jupiter.api.Assertions;

@ExtendWith(MockitoExtension.class)
class UpdateRolePermissionCommandTest {

    @Mock
    private RoleRepository mockRoleRepository;

    @Mock
    private PermissionService mockPermissionService;

    @Mock
    private RoleMapper mockRoleMapper;

    @Mock
    private RoleRule mockRoleRule;

    private RoleMapper roleMapper = new RoleMapper();

    @InjectMocks
    private UpdateRolePermissionCommand.UpdateRolePermissionCommandHandler handler;

    @BeforeEach
    void setUp() {
        handler = new UpdateRolePermissionCommand.UpdateRolePermissionCommandHandler(mockRoleRepository,
                mockPermissionService, mockRoleMapper, mockRoleRule);
    }

    @Test
    void givenValidCommand_whenHandleUpdateRolePermission_thenReturnResponse() {
        // Given
        UpdateRolePermissionCommand command = new UpdateRolePermissionCommand();
        command.setRoleName("ADMIN");
        List<UUID> permissionIds = Arrays.asList(UUID.randomUUID(), UUID.randomUUID());
        command.setPermissionIds(permissionIds);

        Role role = roleMapper.toEntity(command);
        List<Permission> permissions = Arrays.asList(
                Permission.builder().id(permissionIds.get(0)).name("CREATE_USER").build(),
                Permission.builder().id(permissionIds.get(1)).name("DELETE_USER").build());

        when(mockRoleRepository.findByName(command.getRoleName())).thenReturn(Optional.of(role));
        when(mockPermissionService.getPermissionsByPermissionIds(command.getPermissionIds())).thenReturn(permissions);
        when(mockRoleMapper.toResponse(role)).thenReturn(new UpdateRolePermissionResponse(UUID.randomUUID(), "ADMIN",
                "Role with updated permissions", permissions));

        // When
        UpdateRolePermissionResponse response = handler.handle(command);

        // Then
        Assertions.assertNotNull(response);
        Assertions.assertEquals("ADMIN", response.getName());
        Assertions.assertEquals(2, response.getPermissions().size());

        verify(mockRoleRepository).findByName(command.getRoleName());
        verify(mockRoleRule).checkRoleExists(role);
        verify(mockPermissionService).getPermissionsByPermissionIds(command.getPermissionIds());
        verify(mockRoleRepository).save(role);
    }
}