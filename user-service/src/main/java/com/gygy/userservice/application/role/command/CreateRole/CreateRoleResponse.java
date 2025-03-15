package com.gygy.userservice.application.role.command.CreateRole;

import lombok.Getter;
import lombok.Setter;
import java.util.UUID;
import lombok.Builder;

@Getter
@Setter
@Builder
public class CreateRoleResponse {
    private UUID id;
    private String name;
    private String description;
}
