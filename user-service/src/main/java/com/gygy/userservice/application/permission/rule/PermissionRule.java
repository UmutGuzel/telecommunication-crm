package com.gygy.userservice.application.permission.rule;

import org.springframework.stereotype.Component;
import com.gygy.userservice.entity.Permission;

@Component
public class PermissionRule {
    public void checkPermissionExist(Permission permission) {
        if (permission != null) {
            throw new RuntimeException("Permission already exists");
        }
    }

    public void checkPermissionNotExist(Permission permission) {
        if (permission == null) {
            throw new RuntimeException("Permission not found");
        }
    }
}
