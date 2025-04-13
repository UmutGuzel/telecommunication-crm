package com.gygy.userservice.application.user.command.UpdateUserRole;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.util.List;
import an.awesome.pipelinr.Command;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import com.gygy.userservice.persistance.UserRepository;
import com.gygy.userservice.application.role.service.RoleService;
import com.gygy.userservice.entity.User;
import com.gygy.userservice.application.user.rule.UserRule;
import com.gygy.userservice.core.pipelines.authorization.RequiresAuthorization;
import com.gygy.common.events.userservice.UserRoleChangedEvent;
import org.springframework.context.ApplicationEventPublisher;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserRoleCommand implements Command<UpdateUserRoleResponse>, RequiresAuthorization {
    private String email;
    private List<String> roleIds;

    @Override
    public List<String> getRequiredAuthorizations() {
        return List.of("UPDATE_USER_ROLE", "ADMIN");
    }

    @Component
    @RequiredArgsConstructor
    public static class UpdateUserRoleCommandHandler
            implements Command.Handler<UpdateUserRoleCommand, UpdateUserRoleResponse> {
        private final UserRepository userRepository;
        private final RoleService roleService;
        private final UserRule userRule;
        private final ApplicationEventPublisher eventPublisher;

        @Override
        public UpdateUserRoleResponse handle(UpdateUserRoleCommand command) {
            User user = userRepository.findByEmail(command.getEmail()).orElse(null);
            userRule.checkUserExists(user);
            user.setRoles(roleService.getRolesByNames(command.getRoleIds()));
            userRepository.save(user);

            // Publish event for role change
            List<String> roleNames = roleService.getRolesByNames(command.getRoleIds()).stream()
                    .map(role -> role.getName())
                    .toList();
            eventPublisher.publishEvent(new UserRoleChangedEvent(
                    command.getEmail(),
                    roleNames));

            return new UpdateUserRoleResponse("User roles updated successfully");
        }
    }
}
