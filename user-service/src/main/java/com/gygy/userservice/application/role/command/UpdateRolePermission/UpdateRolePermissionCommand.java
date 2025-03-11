package com.gygy.userservice.application.role.command.UpdateRolePermission;

import java.util.List;
import java.util.UUID;

import an.awesome.pipelinr.Command;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import com.gygy.userservice.persistance.RoleRepository;
import com.gygy.userservice.application.permission.service.PermissionService;
import com.gygy.userservice.entity.Role;
import com.gygy.userservice.entity.Permission;
import com.gygy.userservice.application.role.mapper.RoleMapper;
import com.gygy.userservice.application.role.rule.RoleRule;

@Getter
@Setter
public class UpdateRolePermissionCommand implements Command<UpdateRolePermissionResponse> {
    private String roleName;
    private List<UUID> permissionIds;

    @Component
    @RequiredArgsConstructor
    public static class UpdateRolePermissionCommandHandler
            implements Command.Handler<UpdateRolePermissionCommand, UpdateRolePermissionResponse> {
        private final RoleRepository roleRepository;
        private final PermissionService permissionService;
        private final RoleMapper roleMapper;
        private final RoleRule roleRule;

        @Override
        public UpdateRolePermissionResponse handle(UpdateRolePermissionCommand command) {
            Role role = roleRepository.findByName(command.getRoleName()).orElse(null);
            roleRule.checkRoleExists(role);
            List<Permission> permissions = permissionService.getPermissionsByPermissionIds(command.getPermissionIds());
            role.setPermissions(permissions);
            roleRepository.save(role);
            return roleMapper.toResponse(role);
        }
    }
}
