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

@RestController
@RequestMapping("api/v1/role")
@Data
public class RoleController {
    private final Pipeline pipeline;

    // @GetMapping
    // @ResponseStatus(HttpStatus.OK)
    // public List<GetRoleListDto> getAllRoles(@RequestBody GetRoleListQuery query)
    // {
    // return query.execute(pipeline);
    // }

    // @GetMapping("/{id}")
    // @ResponseStatus(HttpStatus.OK)
    // public RoleByIdResponse getRoleById(@RequestBody GetRoleByIdQuery query) {
    // return query.execute(pipeline);
    // }

    // @PostMapping
    // @ResponseStatus(HttpStatus.CREATED)
    // public CreateRoleResponse createRole(@RequestBody CreateRoleCommand command)
    // {
    // return command.execute(pipeline);
    // }

    // @PutMapping("/{id}")
    // @ResponseStatus(HttpStatus.OK)
    // public UpdateRoleResponse updateRole(@RequestBody UpdateRoleCommand command)
    // {
    // return command.execute(pipeline);
    // }

    // @DeleteMapping("/{id}")
    // @ResponseStatus(HttpStatus.NO_CONTENT)
    // public void deleteRole(@RequestBody DeleteRoleCommand command) {
    // command.execute(pipeline);
    // }
}
