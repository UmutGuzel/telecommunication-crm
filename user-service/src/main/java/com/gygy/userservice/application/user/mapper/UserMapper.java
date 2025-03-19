package com.gygy.userservice.application.user.mapper;

import org.springframework.stereotype.Component;
import com.gygy.userservice.entity.User;
import com.gygy.userservice.application.user.command.CreateUser.CreateUserCommand;
import com.gygy.userservice.application.user.command.UpdateUser.UpdateUserCommand;
import com.gygy.userservice.application.user.command.ChangePassword.ChangePasswordCommand;
import lombok.Data;

@Component
@Data
public class UserMapper {
    public User toEntity(CreateUserCommand command) {
        return User.builder()
                .name(command.getName())
                .surname(command.getSurname())
                .email(command.getEmail())
                .password(command.getPassword())
                .phoneNumber(command.getPhoneNumber())
                .address(command.getAddress())
                .build();
    }

    public User toEntity(ChangePasswordCommand command) {
        return User.builder()
                .email(command.getEmail())
                .password(command.getNewPassword())
                .build();
    }

    public void updateEntity(User user, UpdateUserCommand command) {
        if (command.getName() != null && !command.getName().isEmpty()) {
            user.setName(command.getName());
        }
        if (command.getSurname() != null && !command.getSurname().isEmpty()) {
            user.setSurname(command.getSurname());
        }
        if (command.getEmail() != null && !command.getEmail().isEmpty()) {
            user.setEmail(command.getEmail());
        }
        if (command.getPhoneNumber() != null && !command.getPhoneNumber().isEmpty()) {
            user.setPhoneNumber(command.getPhoneNumber());
        }
        if (command.getAddress() != null && !command.getAddress().isEmpty()) {
            user.setAddress(command.getAddress());
        }
    }

}
