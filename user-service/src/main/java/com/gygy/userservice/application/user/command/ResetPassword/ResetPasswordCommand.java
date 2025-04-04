package com.gygy.userservice.application.user.command.ResetPassword;

import an.awesome.pipelinr.Command;
import com.gygy.userservice.entity.User;
import com.gygy.userservice.persistance.UserRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResetPasswordCommand implements Command<ResetPasswordResponse> {
    private String token;

    @NotBlank(message = "New password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$", message = "Password must contain at least one uppercase letter, one lowercase letter, one number, and one special character")
    private String newPassword;

    @Component
    @AllArgsConstructor
    @Slf4j
    public static class ResetPasswordHandler implements Command.Handler<ResetPasswordCommand, ResetPasswordResponse> {
        private final UserRepository userRepository;
        private final PasswordEncoder passwordEncoder;

        @Override
        public ResetPasswordResponse handle(ResetPasswordCommand command) {
            User user = userRepository.findByResetToken(command.getToken())
                    .orElse(null);

            if (user == null) {
                return new ResetPasswordResponse("Invalid or expired reset token", false);
            }

            // Check if token is expired
            if (user.getResetTokenExpiry().isBefore(LocalDateTime.now())) {
                return new ResetPasswordResponse("Reset token has expired", false);
            }

            // Update password
            user.setPassword(passwordEncoder.encode(command.getNewPassword()));

            // Clear reset token
            user.setResetToken(null);
            user.setResetTokenExpiry(null);

            userRepository.save(user);

            log.info("Password successfully reset for user: {}", user.getId());

            return new ResetPasswordResponse("Password has been reset successfully", true);
        }
    }
}