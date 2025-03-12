package com.gygy.userservice.core.pipelines.authorization;

public interface RequiresRole {
    String[] getRequiredRoles();
}