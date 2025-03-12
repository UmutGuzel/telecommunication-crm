package com.gygy.userservice.application.user.command.CreateUser;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateUserResponse {
    private String message;
}
