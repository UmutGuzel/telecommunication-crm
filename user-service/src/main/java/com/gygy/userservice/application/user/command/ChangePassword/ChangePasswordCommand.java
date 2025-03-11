package com.gygy.userservice.application.user.command.ChangePassword;

import an.awesome.pipelinr.Command;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import com.gygy.userservice.persistance.UserRepository;
import com.gygy.userservice.application.user.rule.UserRule;
import com.gygy.userservice.entity.User;
import com.gygy.userservice.application.user.mapper.UserMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordCommand implements Command<ChangePasswordResponse> {
    @NotBlank
    private String email;
    @NotBlank(message = "Password is required")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$", message = "Password must be at least 8 characters long and contain at least one uppercase letter, one lowercase letter, one number, and one special character")
    private String oldPassword;

    @NotBlank
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$", message = "Password must be at least 8 characters long and contain at least one uppercase letter, one lowercase letter, one number, and one special character")
    private String newPassword;

    @Component
    @RequiredArgsConstructor
    public static class ChangePasswordCommandHandler
            implements Command.Handler<ChangePasswordCommand, ChangePasswordResponse> {
        private final UserRepository userRepository;
        private final UserRule userRule;
        private final UserMapper userMapper;
        private final PasswordEncoder passwordEncoder;

        @Override
        public ChangePasswordResponse handle(ChangePasswordCommand command) {
            User user = userRepository.findByEmail(command.getEmail()).orElse(null);
            userRule.checkUserExists(user);
            userRule.checkPassword(user, command.getOldPassword());
            user.setPassword(passwordEncoder.encode(command.getNewPassword()));
            userRepository.save(user);
            return new ChangePasswordResponse("Password changed successfully");
        }
    }
}
