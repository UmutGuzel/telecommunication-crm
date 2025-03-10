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
import com.gygy.userservice.entity.User;
import an.awesome.pipelinr.Pipeline;

@RestController
@RequestMapping("api/v1/user")
@Data
public class UserController {
    private final Pipeline pipeline;

    // @GetMapping("/list")
    // @ResponseStatus(HttpStatus.OK)
    // public List<GetUserListDto> getAllUsers(@RequestBody GetUserListQuery query)
    // {
    // return query.execute(pipeline);
    // }

    // @GetMapping
    // @ResponseStatus(HttpStatus.OK)
    // public UserByIdResponse getUserById(@RequestBody GetUserByIdQuery query) {
    // return query.execute(pipeline);
    // }

    // @PostMapping
    // @ResponseStatus(HttpStatus.CREATED)
    // public CreateUserResponse createUser(@RequestBody CreateUserCommand command)
    // {
    // return command.execute(pipeline);
    // }

    // @PutMapping
    // @ResponseStatus(HttpStatus.OK)
    // public UpdateUserResponse updateUser(@RequestBody UpdateUserCommand command)
    // {
    // return command.execute(pipeline);
    // }

    // @DeleteMapping
    // @ResponseStatus(HttpStatus.NO_CONTENT)
    // public void deleteUser(@RequestBody DeleteUserCommand command) {
    // command.execute(pipeline);
    // }

}
