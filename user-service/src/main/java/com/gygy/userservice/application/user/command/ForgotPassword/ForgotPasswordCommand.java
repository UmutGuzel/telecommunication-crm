package com.gygy.userservice.application.user.command.ForgotPassword;

import an.awesome.pipelinr.Command;

import com.gygy.userservice.core.configration.ApplicationConfig;
import com.gygy.userservice.core.event.PasswordResetEvent;
import com.gygy.userservice.entity.User;
import com.gygy.userservice.persistance.UserRepository;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
// TODO: Uncomment when implementing Kafka
// import org.springframework.kafka.core.KafkaTemplate;
// import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
// TODO: Uncomment when implementing Kafka
// import java.util.concurrent.CompletableFuture;

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
        // TODO: Uncomment when implementing Kafka
        // private final KafkaTemplate<String, PasswordResetEvent> kafkaTemplate;
        private final ApplicationConfig applicationConfig;

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

            // Create reset link using UriComponentsBuilder for proper URL encoding
            String resetLink = UriComponentsBuilder
                    .fromUriString(applicationConfig.getGatewayUrl())
                    .path(applicationConfig.getResetPasswordPath())
                    .queryParam("token", resetToken)
                    .build()
                    .toUriString();

            // TODO: Implement Kafka event sending
            // Create PasswordResetEvent
            PasswordResetEvent event = new PasswordResetEvent(
                    user.getId().toString(),
                    user.getEmail(),
                    resetToken,
                    expiryDate,
                    resetLink);

            // TODO: Send event to Kafka
            // CompletableFuture<SendResult<String, PasswordResetEvent>> future =
            // kafkaTemplate.send(TOPIC_NAME,
            // user.getId().toString(), event);
            //
            // future.whenComplete((result, ex) -> {
            // if (ex == null) {
            // log.info("Password reset event sent successfully for user: {}",
            // user.getId());
            // } else {
            // log.error("Failed to send password reset event for user: {}", user.getId(),
            // ex);
            // }
            // });

            log.info("TODO: Password reset event needs to be sent for user: {}", user.getId());

            return new ForgotPasswordResponse(
                    "If the email exists in our system, you will receive a password reset link.",
                    true);
        }
    }
}