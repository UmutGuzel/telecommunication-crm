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
import com.gygy.userservice.application.permission.command.CreatePermission.CreatePermissionCommand;
import com.gygy.userservice.application.permission.command.CreatePermission.CreatePermissionResponse;

@RestController
@RequestMapping("api/v1/permission")
@Data
public class PermissionController {
    private final Pipeline pipeline;

    // @GetMapping
    // @ResponseStatus(HttpStatus.OK)
    // public List<GetPermissionListDto> getAllPermissions(@RequestBody
    // GetPermissionListQuery query) {
    // return query.execute(pipeline);
    // }

    // @GetMapping("/{id}")
    // @ResponseStatus(HttpStatus.OK)
    // public PermissionByIdResponse getPermissionById(@RequestBody
    // GetPermissionByIdQuery query) {
    // return query.execute(pipeline);
    // }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CreatePermissionResponse createPermission(@RequestBody CreatePermissionCommand command) {
        return command.execute(pipeline);
    }

    // @PutMapping("/{id}")
    // @ResponseStatus(HttpStatus.OK)
    // public UpdatePermissionResponse updatePermission(@RequestBody
    // UpdatePermissionCommand command) {
    // return command.execute(pipeline);
    // }

    // @DeleteMapping("/{id}")
    // @ResponseStatus(HttpStatus.NO_CONTENT)
    // public void deletePermission(@RequestBody DeletePermissionCommand command) {
    // command.execute(pipeline);
    // }
}
