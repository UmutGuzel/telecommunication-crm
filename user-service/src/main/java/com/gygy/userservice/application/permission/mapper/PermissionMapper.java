package com.gygy.userservice.application.permission.mapper;

import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import com.gygy.userservice.application.permission.command.CreatePermission.CreatePermissionCommand;
import com.gygy.userservice.entity.Permission;
import java.time.LocalDateTime;
import com.gygy.userservice.application.permission.query.GetPermissionList.GetPermissionListDto;
import com.gygy.userservice.application.permission.query.GetPermissionById.GetPermissionByIdResponse;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PermissionMapper {
    public Permission toEntity(CreatePermissionCommand command) {
        return Permission.builder()
                .name(command.getName())
                .description(command.getDescription())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public GetPermissionListDto mapToGetPermissionListDto(Permission permission) {
        if (permission == null) {
            return null;
        }

        return new GetPermissionListDto(
                permission.getId(),
                permission.getName(),
                permission.getDescription(),
                permission.getCreatedAt(),
                permission.getUpdatedAt());
    }

    public List<GetPermissionListDto> mapToGetPermissionListDtoList(List<Permission> permissions) {
        if (permissions == null) {
            return null;
        }

        return permissions.stream()
                .map(this::mapToGetPermissionListDto)
                .collect(Collectors.toList());
    }

    public GetPermissionByIdResponse mapToGetPermissionByIdResponse(Permission permission) {
        if (permission == null) {
            return null;
        }

        return new GetPermissionByIdResponse(
                permission.getId(),
                permission.getName(),
                permission.getDescription(),
                permission.getCreatedAt(),
                permission.getUpdatedAt());
    }
}
