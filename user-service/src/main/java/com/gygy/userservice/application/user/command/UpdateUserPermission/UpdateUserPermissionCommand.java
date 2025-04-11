package com.gygy.userservice.application.user.command.UpdateUserPermission;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.UUID;
import an.awesome.pipelinr.Command;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import com.gygy.userservice.persistance.UserRepository;
import com.gygy.userservice.application.permission.service.PermissionService;
import com.gygy.userservice.entity.User;
import com.gygy.userservice.application.user.rule.UserRule;
import com.gygy.userservice.core.pipelines.authorization.RequiresAuthorization;
import com.gygy.common.events.userservice.UserPermissionChangedEvent;
import org.springframework.context.ApplicationEventPublisher;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserPermissionCommand implements Command<UpdateUserPermissionResponse>, RequiresAuthorization {
    private String email;
    private List<String> permissionIds;

    @Override
    public List<String> getRequiredAuthorizations() {
        return List.of("UPDATE_USER_PERMISSION", "ADMIN");
    }

    @Component
    @RequiredArgsConstructor
    public static class UpdateUserPermissionCommandHandler
            implements Command.Handler<UpdateUserPermissionCommand, UpdateUserPermissionResponse> {
        private final UserRepository userRepository;
        private final PermissionService permissionService;
        private final UserRule userRule;
        private final ApplicationEventPublisher eventPublisher;

        @Override
        public UpdateUserPermissionResponse handle(UpdateUserPermissionCommand command) {
            User user = userRepository.findByEmail(command.getEmail()).orElse(null);
            userRule.checkUserExists(user);

            List<UUID> permissionUUIDs = command.getPermissionIds().stream()
                    .map(UUID::fromString)
                    .toList();

            user.setPermissions(permissionService.getPermissionsByPermissionIds(permissionUUIDs));
            userRepository.save(user);

            List<String> permissionNames = permissionService.getPermissionsByPermissionIds(permissionUUIDs).stream()
                    .map(permission -> permission.getName())
                    .toList();
            eventPublisher.publishEvent(new UserPermissionChangedEvent(
                    command.getEmail(),
                    permissionNames));

            return new UpdateUserPermissionResponse("User permissions updated successfully");
        }
    }
}