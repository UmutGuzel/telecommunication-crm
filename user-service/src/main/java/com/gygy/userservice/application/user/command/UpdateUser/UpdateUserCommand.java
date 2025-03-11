package com.gygy.userservice.application.user.command.UpdateUser;

import org.springframework.stereotype.Component;

import an.awesome.pipelinr.Command;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import com.gygy.userservice.persistance.UserRepository;
import com.gygy.userservice.entity.User;
import com.gygy.userservice.application.user.rule.UserRule;
import com.gygy.userservice.application.user.mapper.UserMapper;
import jakarta.validation.constraints.Email;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserCommand implements Command<UpdateUserResponse> {
    private String name;
    private String surname;
    @Email(message = "Email must be a valid email address")
    private String email;
    private String phoneNumber;
    private String address;

    @Component
    @RequiredArgsConstructor
    public static class UpdateUserCommandHandler implements Command.Handler<UpdateUserCommand, UpdateUserResponse> {
        private final UserRepository userRepository;
        private final UserRule userRule;
        private final UserMapper userMapper;

        @Override
        public UpdateUserResponse handle(UpdateUserCommand command) {
            User user = userRepository.findByEmail(command.getEmail()).orElse(null);
            userRule.checkUserExists(user);
            userMapper.updateEntity(user, command);
            userRepository.save(user);
            return new UpdateUserResponse("User updated successfully");
        }
    }
}
