package com.gygy.userservice.application.permission.service;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;
import com.gygy.userservice.entity.Permission;
import com.gygy.userservice.persistance.PermissionRepository;
import lombok.RequiredArgsConstructor;
import com.gygy.userservice.application.permission.rule.PermissionRule;

@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {
    private final PermissionRepository permissionRepository;
    private final PermissionRule permissionRule;

    @Override
    public Permission getPermissionById(UUID id) {
        Permission permission = permissionRepository.findById(id).orElse(null);
        permissionRule.checkPermissionNotExist(permission);
        return permission;
    }

    @Override
    public List<Permission> getPermissionsByPermissionIds(List<UUID> permissionIds) {
        List<Permission> permissions = permissionRepository.findAllById(permissionIds);
        permissions.forEach(permissionRule::checkPermissionNotExist);
        return permissions;
    }

}
