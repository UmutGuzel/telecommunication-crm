package com.gygy.userservice.application.role.rule;

import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import com.gygy.userservice.entity.Role;

@Component
@RequiredArgsConstructor
public class RoleRule {
    public void checkRoleExists(Role role) {
        if (role == null) {
            throw new RuntimeException("Role not found");
        }
    }

    public void checkRoleNotExists(Role role) {
        if (role != null) {
            throw new RuntimeException("Role already exists");
        }
    }

    public void checkRoleName(String name) {
        if (name == null || name.isEmpty()) {
            throw new RuntimeException("Role name is required");
        }
    }
}
