package com.gygy.userservice.application.role.command.CreateRole;

import an.awesome.pipelinr.Command;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import com.gygy.userservice.persistance.RoleRepository;
import com.gygy.userservice.application.role.mapper.RoleMapper;
import com.gygy.userservice.entity.Role;
import jakarta.validation.constraints.NotBlank;
import com.gygy.userservice.application.role.rule.RoleRule;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateRoleCommand implements Command<CreateRoleResponse> {
    @NotBlank(message = "Name is required")
    private String name;
    private String description;
    private List<UUID> permissionIds;

    @Component
    @RequiredArgsConstructor
    public static class CreateRoleCommandHandler implements Command.Handler<CreateRoleCommand, CreateRoleResponse> {
        private final RoleRepository roleRepository;
        private final RoleMapper roleMapper;
        private final RoleRule roleRule;

        @Override
        public CreateRoleResponse handle(CreateRoleCommand command) {
            Role role = roleRepository.findByName(command.getName()).orElse(null);
            roleRule.checkRoleNotExists(role);

            role = roleRepository.save(roleMapper.toEntity(command));

            return CreateRoleResponse.builder()
                    .id(role.getId())
                    .name(role.getName())
                    .description(role.getDescription())
                    .build();
        }
    }
}
