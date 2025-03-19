package com.gygy.userservice.application.permission.command.CreatePermission;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import an.awesome.pipelinr.Command;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import com.gygy.userservice.persistance.PermissionRepository;
import com.gygy.userservice.application.permission.rule.PermissionRule;
import com.gygy.userservice.application.permission.mapper.PermissionMapper;
import com.gygy.userservice.entity.Permission;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreatePermissionCommand implements Command<CreatePermissionResponse> {
    private String name;
    private String description;

    @Component
    @RequiredArgsConstructor
    public static class CreatePermissionCommandHandler
            implements Command.Handler<CreatePermissionCommand, CreatePermissionResponse> {
        private final PermissionRepository permissionRepository;
        private final PermissionRule permissionRule;
        private final PermissionMapper permissionMapper;

        @Override
        public CreatePermissionResponse handle(CreatePermissionCommand command) {
            Permission permission = permissionRepository.findByName(command.getName()).orElse(null);
            permissionRule.checkPermissionExist(permission);
            permissionRepository.save(permissionMapper.toEntity(command));
            return new CreatePermissionResponse("Permission created successfully");
        }
    }
}
