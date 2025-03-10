package com.gygy.userservice.application.user.mapper;

import org.springframework.stereotype.Component;
import com.gygy.userservice.entity.User;
import com.gygy.userservice.application.user.command.CreateUser.CreateUserCommand;
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

}
