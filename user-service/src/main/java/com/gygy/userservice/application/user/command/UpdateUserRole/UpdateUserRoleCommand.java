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

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserRoleCommand implements Command<UpdateUserRoleResponse> {
    private String email;
    private List<String> roleIds;

    @Component
    @RequiredArgsConstructor
    public static class UpdateUserRoleCommandHandler
            implements Command.Handler<UpdateUserRoleCommand, UpdateUserRoleResponse> {
        private final UserRepository userRepository;
        private final RoleService roleService;
        private final UserRule userRule;

        @Override
        public UpdateUserRoleResponse handle(UpdateUserRoleCommand command) {
            User user = userRepository.findByEmail(command.getEmail()).orElse(null);
            userRule.checkUserExists(user);
            user.setRoles(roleService.getRolesByNames(command.getRoleIds()));
            userRepository.save(user);
            return new UpdateUserRoleResponse("User roles updated successfully");
        }
    }
}
