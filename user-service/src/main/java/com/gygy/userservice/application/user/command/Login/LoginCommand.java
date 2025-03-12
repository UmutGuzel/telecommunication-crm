package com.gygy.userservice.application.user.command.Login;

import an.awesome.pipelinr.Command;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import com.gygy.userservice.persistance.UserRepository;
import com.gygy.userservice.core.jwt.JwtService;
import com.gygy.userservice.application.user.rule.UserRule;
import com.gygy.userservice.entity.User;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginCommand implements Command<LoginResponse> {
    @Email(message = "Email must be a valid email address")
    @NotBlank(message = "Email is required")
    private String email;
    @NotBlank(message = "Password is required")
    private String password;

    @Component
    @RequiredArgsConstructor
    public static class LoginCommandHandler implements Command.Handler<LoginCommand, LoginResponse> {
        private final UserRepository userRepository;
        private final UserRule userRule;
        private final JwtService jwtService;

        @Override
        public LoginResponse handle(LoginCommand command) {
            User user = userRepository.findByEmail(command.getEmail()).orElse(null);
            userRule.checkUserExists(user);
            userRule.checkPassword(user, command.getPassword());

            // Generate JWT token
            String token = jwtService.generateToken(user);

            return LoginResponse.builder()
                    .token(token)
                    .userId(user.getId())
                    .email(user.getEmail())
                    .build();
        }
    }
}
