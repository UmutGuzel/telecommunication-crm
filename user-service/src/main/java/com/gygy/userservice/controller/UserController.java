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
import com.gygy.userservice.application.user.command.CreateUser.CreateUserCommand;
import com.gygy.userservice.application.user.command.CreateUser.CreateUserResponse;
import com.gygy.userservice.application.user.command.UpdateUser.UpdateUserCommand;
import com.gygy.userservice.application.user.command.UpdateUser.UpdateUserResponse;
import com.gygy.userservice.application.user.command.Login.LoginCommand;
import com.gygy.userservice.application.user.command.Login.LoginResponse;
import com.gygy.userservice.application.user.command.UpdateUserRole.UpdateUserRoleCommand;
import com.gygy.userservice.application.user.command.UpdateUserRole.UpdateUserRoleResponse;
import com.gygy.userservice.application.user.command.ForgotPassword.ForgotPasswordCommand;
import com.gygy.userservice.application.user.command.ForgotPassword.ForgotPasswordResponse;
import com.gygy.userservice.application.user.command.ResetPassword.ResetPasswordCommand;
import com.gygy.userservice.application.user.command.ResetPassword.ResetPasswordResponse;
import com.gygy.userservice.application.user.command.ActivateUser.ActivateUserCommand;
import com.gygy.userservice.application.user.command.ActivateUser.ActivateUserResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestParam;
import com.gygy.userservice.persistance.UserRepository;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;
import java.util.Optional;
import com.gygy.userservice.core.configuration.ApplicationConfig;

@RestController
@RequestMapping("api/auth")
@Data
public class UserController {
    private final Pipeline pipeline;

    // @GetMapping("/list")
    // @ResponseStatus(HttpStatus.OK)
    // public List<GetUserListDto> getAllUsers(@RequestBody GetUserListQuery query)
    // {
    // return query.execute(pipeline);
    // }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public LoginResponse login(@RequestBody LoginCommand command) {
        return command.execute(pipeline);
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public CreateUserResponse createUser(@RequestBody CreateUserCommand command) {
        return command.execute(pipeline);
    }

    @GetMapping("/activate")
    @ResponseStatus(HttpStatus.OK)
    public ActivateUserResponse activateUser(@RequestParam String token) {
        ActivateUserCommand command = new ActivateUserCommand(token);
        return command.execute(pipeline);
    }

    @PutMapping("/user")
    @ResponseStatus(HttpStatus.OK)
    public UpdateUserResponse updateUser(@RequestBody UpdateUserCommand command) {
        return command.execute(pipeline);
    }

    @PutMapping("/role")
    @ResponseStatus(HttpStatus.OK)
    public UpdateUserRoleResponse updateUserRole(@RequestBody UpdateUserRoleCommand command) {
        return command.execute(pipeline);
    }

    @PostMapping("/forgot-password")
    @ResponseStatus(HttpStatus.OK)
    public ForgotPasswordResponse forgotPassword(@RequestBody ForgotPasswordCommand command) {
        return command.execute(pipeline);
    }

    @PostMapping("/reset-password")
    @ResponseStatus(HttpStatus.OK)
    public ResetPasswordResponse resetPassword(@RequestParam String token, @RequestBody ResetPasswordCommand command) {
        command.setToken(token);
        return command.execute(pipeline);
    }

    // @DeleteMapping
    // @ResponseStatus(HttpStatus.NO_CONTENT)
    // public void deleteUser(@RequestBody DeleteUserCommand command) {
    // command.execute(pipeline);
    // }

}
