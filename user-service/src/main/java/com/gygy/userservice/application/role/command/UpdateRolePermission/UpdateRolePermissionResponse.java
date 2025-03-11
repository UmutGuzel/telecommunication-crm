package com.gygy.userservice.application.role.command.UpdateRolePermission;

import java.util.List;
import java.util.UUID;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import com.gygy.userservice.entity.Permission;

@Builder
@Getter
@Setter
public class UpdateRolePermissionResponse {

    private UUID id;
    private String name;
    private String description;
    private List<Permission> permissions;

}
