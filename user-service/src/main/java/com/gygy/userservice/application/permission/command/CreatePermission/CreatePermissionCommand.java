package com.gygy.userservice.application.permission.command.CreatePermission;

import lombok.Getter;
import lombok.Setter;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import an.awesome.pipelinr.Command;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import com.gygy.userservice.persistance.PermissionRepository;
import com.gygy.userservice.application.permission.rule.PermissionRule;
import com.gygy.userservice.application.permission.mapper.PermissionMapper;
import com.gygy.userservice.entity.Permission;
import com.gygy.userservice.core.pipelines.authorization.RequiresAuthorization;

@Getter
@Setter
public class CreatePermissionCommand implements Command<CreatePermissionResponse>, RequiresAuthorization {
    private String name;
    private String description;

    @Override
    public List<String> getRequiredAuthorizations() {
        return new ArrayList<>(Arrays.asList("CREATE_PERMISSION", "ADMIN"));
    }

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
