package com.gygy.userservice.application.user.command.ForgotPassword;

import an.awesome.pipelinr.Command;
import com.gygy.userservice.model.event.PasswordResetEvent;
import com.gygy.userservice.entity.User;
import com.gygy.userservice.persistance.UserRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ForgotPasswordCommand implements Command<ForgotPasswordResponse> {
    private String email;

    @Component
    @AllArgsConstructor
    @Slf4j
    public static class ForgotPasswordHandler
            implements Command.Handler<ForgotPasswordCommand, ForgotPasswordResponse> {

        private static final String TOPIC_NAME = "password-reset-events";
        private final UserRepository userRepository;
        private final KafkaTemplate<String, PasswordResetEvent> kafkaTemplate;

        @Value("${server.port}")
        private int serverPort;

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
            LocalDateTime expiryDate = LocalDateTime.now().plusHours(1); // Token valid for 1 hour

            user.setResetToken(resetToken);
            user.setResetTokenExpiry(expiryDate);
            userRepository.save(user);

            // Create reset link
            String resetLink = String.format("http://localhost:%d/api/v1/users/reset-password?token=%s",
                    serverPort, resetToken);

            // Create and send Kafka event
            PasswordResetEvent event = new PasswordResetEvent(
                    user.getId().toString(),
                    user.getEmail(),
                    resetToken,
                    expiryDate,
                    resetLink);

            // Send event to Kafka
            CompletableFuture<SendResult<String, PasswordResetEvent>> future = kafkaTemplate.send(TOPIC_NAME,
                    user.getId().toString(), event);

            future.whenComplete((result, ex) -> {
                if (ex == null) {
                    log.info("Password reset event sent successfully for user: {}", user.getId());
                } else {
                    log.error("Failed to send password reset event for user: {}", user.getId(), ex);
                }
            });

            return new ForgotPasswordResponse(
                    "If the email exists in our system, you will receive a password reset link.",
                    true);
        }
    }
}