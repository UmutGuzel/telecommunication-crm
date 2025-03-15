package com.gygy.userservice.core.pipelines.authorization;

public interface RequiresPermission {
    String[] getRequiredPermissions();
}