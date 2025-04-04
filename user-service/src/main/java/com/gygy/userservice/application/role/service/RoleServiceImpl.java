
package com.gygy.userservice.application.role.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import com.gygy.userservice.persistance.RoleRepository;
import com.gygy.userservice.entity.Role;
import java.util.List;
import com.gygy.userservice.application.role.rule.RoleRule;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final RoleRule roleRule;

    @Override
    public Role getRoleByName(String name) {
        Role role = roleRepository.findByName(name).orElse(null);
        log.error("Role: {}", role);
        roleRule.checkRoleExists(role);
        return role;
    }

    @Override
    public List<Role> getRolesByNames(List<String> names) {
        List<Role> roles = roleRepository.findAllByNameIn(names);
        roleRule.checkRolesExists(roles);
        return roles;
    }

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }
}
