package com.gygy.userservice.application.user.command.ForgotPassword;

import an.awesome.pipelinr.Command;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.gygy.userservice.entity.User;
import com.gygy.userservice.persistance.UserRepository;
import org.springframework.stereotype.Component;
import java.util.UUID;
import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ForgotPasswordCommand implements Command<ForgotPasswordResponse> {
    private String email;

    @Component
    @AllArgsConstructor
    public static class ForgotPasswordHandler
            implements Command.Handler<ForgotPasswordCommand, ForgotPasswordResponse> {
        private final UserRepository userRepository;
        // TODO: private final EmailService emailService;

        @Override
        public ForgotPasswordResponse handle(ForgotPasswordCommand command) {
            Optional<User> userOptional = userRepository.findByEmail(command.getEmail());

            if (userOptional.isEmpty()) {
                return new ForgotPasswordResponse(
                        "If the email exists in our system, you will receive a password reset link.", true);
            }

            User user = userOptional.get();
            // Generate a password reset token
            String resetToken = UUID.randomUUID().toString();
            user.setResetToken(resetToken);
            user.setResetTokenExpiry(System.currentTimeMillis() + 3600000); // Token valid for 1 hour
            userRepository.save(user);

            // TODO: send email with reset token

            return new ForgotPasswordResponse(
                    "If the email exists in our system, you will receive a password reset link.",
                    true);
        }
    }
}