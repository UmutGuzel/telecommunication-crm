package com.gygy.userservice.application.user.command.CreateUser;

import an.awesome.pipelinr.Command;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import com.gygy.userservice.persistance.UserRepository;
import com.gygy.userservice.application.user.rule.UserRule;
import com.gygy.userservice.entity.User;
import com.gygy.userservice.application.user.mapper.UserMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateUserCommand implements Command<CreateUserResponse> {
    @NotBlank(message = "Name is required")
    private String name;
    @NotBlank(message = "Surname is required")
    private String surname;
    @NotBlank(message = "Email is required")
    @Email(message = "Email must be a valid email address")
    private String email;
    @NotBlank(message = "Password is required")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$", message = "Password must be at least 8 characters long and contain at least one uppercase letter, one lowercase letter, one number, and one special character")
    private String password;
    @NotBlank(message = "Phone number is required")
    private String phoneNumber;
    @NotBlank(message = "Address is required")
    private String address;

    @Component
    @RequiredArgsConstructor
    public static class CreateUserCommandHandler implements Command.Handler<CreateUserCommand, CreateUserResponse> {
        private final UserRepository userRepository;
        private final UserRule userRule;
        private final UserMapper userMapper;
        private final PasswordEncoder passwordEncoder;

        @Override
        public CreateUserResponse handle(CreateUserCommand command) {
            User user = userRepository.findByEmail(command.getEmail()).orElse(null);
            userRule.checkUserNotExists(user);
            command.setPassword(passwordEncoder.encode(command.getPassword()));
            User newUser = userMapper.toEntity(command);
            userRepository.save(newUser);
            return new CreateUserResponse("User created successfully");
        }
    }
}
