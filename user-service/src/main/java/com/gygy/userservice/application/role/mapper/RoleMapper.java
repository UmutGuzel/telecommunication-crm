package com.gygy.userservice.application.role.mapper;

import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import com.gygy.userservice.entity.Role;
import com.gygy.userservice.application.role.command.CreateRole.CreateRoleCommand;
import com.gygy.userservice.application.role.command.UpdateRolePermission.UpdateRolePermissionCommand;
import com.gygy.userservice.application.role.command.UpdateRolePermission.UpdateRolePermissionResponse;
import com.gygy.userservice.application.role.query.GetRoleList.GetRoleListDto;
import com.gygy.userservice.application.role.query.GetRoleById.GetRoleByIdResponse;
import java.util.List;
import java.util.stream.Collectors;

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

    /**
     * Maps a Role entity to a GetRoleListDto
     * 
     * @param role The Role entity to map
     * @return The mapped GetRoleListDto
     */
    public GetRoleListDto mapToGetRoleListDto(Role role) {
        if (role == null) {
            return null;
        }

        return new GetRoleListDto(
                role.getId(),
                role.getName(),
                role.getDescription(),
                role.getCreatedAt(),
                role.getUpdatedAt(),
                mapPermissionNames(role));
    }

    public List<GetRoleListDto> mapToGetRoleListDtoList(List<Role> roles) {
        if (roles == null) {
            return null;
        }

        return roles.stream()
                .map(this::mapToGetRoleListDto)
                .collect(Collectors.toList());
    }

    public GetRoleByIdResponse mapToGetRoleByIdResponse(Role role) {
        if (role == null) {
            return null;
        }

        return new GetRoleByIdResponse(
                role.getId(),
                role.getName(),
                role.getDescription(),
                role.getCreatedAt(),
                role.getUpdatedAt(),
                mapPermissionNames(role));
    }

    private List<String> mapPermissionNames(Role role) {
        return role.getPermissions() != null
                ? role.getPermissions().stream()
                        .map(permission -> permission.getName())
                        .collect(Collectors.toList())
                : null;
    }
}
