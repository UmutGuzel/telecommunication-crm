package com.gygy.userservice.application.permission.service;

import java.util.List;
import java.util.UUID;

import com.gygy.userservice.entity.Permission;

public interface PermissionService {
    Permission getPermissionById(UUID id);

    List<Permission> getPermissionsByPermissionIds(List<UUID> permissionIds);
}
