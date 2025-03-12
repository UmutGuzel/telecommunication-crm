package com.gygy.userservice.application.role.mapper;

import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import com.gygy.userservice.entity.Role;
import com.gygy.userservice.application.role.command.CreateRole.CreateRoleCommand;
import com.gygy.userservice.application.role.command.UpdateRolePermission.UpdateRolePermissionCommand;
import com.gygy.userservice.application.role.command.UpdateRolePermission.UpdateRolePermissionResponse;

@Component
@RequiredArgsConstructor
public class RoleMapper {
    public Role toEntity(CreateRoleCommand command) {
        return Role.builder()
                .name(command.getName())
                .description(command.getDescription())
                .build();
    }

    public Role toEntity(UpdateRolePermissionCommand command) {
        return Role.builder()
                .name(command.getRoleName())
                .build();
    }

    public UpdateRolePermissionResponse toResponse(Role role) {
        return UpdateRolePermissionResponse.builder()
                .id(role.getId())
                .name(role.getName())
                .description(role.getDescription())
                .permissions(role.getPermissions())
                .build();
    }
}
