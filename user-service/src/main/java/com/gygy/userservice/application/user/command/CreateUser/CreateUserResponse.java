package com.gygy.userservice.application.user.command.CreateUser;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserResponse {
    private UUID id;
    private String message;
}
