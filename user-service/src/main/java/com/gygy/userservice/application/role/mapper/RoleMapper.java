package com.gygy.userservice.application.role.mapper;

import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import com.gygy.userservice.entity.Role;
import com.gygy.userservice.application.role.command.CreateRole.CreateRoleCommand;

@Component
@RequiredArgsConstructor
public class RoleMapper {
    public Role toEntity(CreateRoleCommand command) {
        return Role.builder()
                .name(command.getName())
                .description(command.getDescription())
                .build();
    }

}
