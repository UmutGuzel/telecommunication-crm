package com.gygy.userservice.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import java.util.List;
import java.util.UUID;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;
import an.awesome.pipelinr.Pipeline;
import com.gygy.userservice.application.role.command.CreateRole.CreateRoleCommand;
import com.gygy.userservice.application.role.command.CreateRole.CreateRoleResponse;
import com.gygy.userservice.application.role.command.UpdateRolePermission.UpdateRolePermissionCommand;
import com.gygy.userservice.application.role.command.UpdateRolePermission.UpdateRolePermissionResponse;
import com.gygy.userservice.application.role.query.GetRoleList.GetRoleListQuery;
import com.gygy.userservice.application.role.query.GetRoleList.GetRoleListDto;
import com.gygy.userservice.application.role.query.GetRoleById.GetRoleByIdQuery;
import com.gygy.userservice.application.role.query.GetRoleById.GetRoleByIdResponse;

@RestController
@RequestMapping("api/v1/role")
@Data
public class RoleController {
    private final Pipeline pipeline;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<GetRoleListDto> getAllRoles() {
        GetRoleListQuery query = new GetRoleListQuery();
        return query.execute(pipeline);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public GetRoleByIdResponse getRoleById(@PathVariable UUID id) {
        GetRoleByIdQuery query = new GetRoleByIdQuery(id);
        return query.execute(pipeline);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CreateRoleResponse createRole(@RequestBody CreateRoleCommand command) {
        return command.execute(pipeline);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public UpdateRolePermissionResponse updateRolePermission(@RequestBody UpdateRolePermissionCommand command) {
        return command.execute(pipeline);
    }

    // @DeleteMapping("/{id}")
    // @ResponseStatus(HttpStatus.NO_CONTENT)
    // public void deleteRole(@RequestBody DeleteRoleCommand command) {
    // command.execute(pipeline);
    // }
}
