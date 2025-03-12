package com.gygy.userservice.application.permission.mapper;

import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import com.gygy.userservice.application.permission.command.CreatePermission.CreatePermissionCommand;
import com.gygy.userservice.entity.Permission;
import java.time.LocalDateTime;

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
}
