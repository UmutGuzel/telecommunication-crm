package com.gygy.userservice.application.user.command.ForgotPassword;

import an.awesome.pipelinr.Command;

import com.gygy.userservice.core.configuration.ApplicationConfig;
import com.gygy.common.events.PasswordResetEvent;
import com.gygy.userservice.entity.User;
import com.gygy.userservice.persistance.UserRepository;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

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

                private final UserRepository userRepository;
                private final KafkaTemplate<String, PasswordResetEvent> kafkaTemplate;
                private final ApplicationConfig applicationConfig;

                @Override
                public ForgotPasswordResponse handle(ForgotPasswordCommand command) {
                        Optional<User> userOptional = userRepository.findByEmail(command.getEmail());

                        if (userOptional.isEmpty()) {
                                return new ForgotPasswordResponse(
                                                "If the email exists in our system, you will receive a password reset link.",
                                                true);
                        }

                        User user = userOptional.get();

                        // Generate a password reset token
                        String resetToken = UUID.randomUUID().toString();
                        LocalDateTime expiryDate = LocalDateTime.now().plusHours(1);

                        user.setResetToken(resetToken);
                        user.setResetTokenExpiry(expiryDate);
                        userRepository.save(user);

                        String resetLink = UriComponentsBuilder
                                        .fromUriString(applicationConfig.getGatewayUrl())
                                        .path(applicationConfig.getResetPasswordPath())
                                        .queryParam("token", resetToken)
                                        .build()
                                        .toUriString();

                        PasswordResetEvent event = new PasswordResetEvent(
                                        user.getId().toString(),
                                        user.getEmail(),
                                        resetToken,
                                        expiryDate,
                                        resetLink);

                        kafkaTemplate.send(applicationConfig.getPasswordResetEvents(), user.getId().toString(),
                                        event);

                        log.info("Password reset event sent successfully for user: {}", user.getId());

                        return new ForgotPasswordResponse(
                                        "If the email exists in our system, you will receive a password reset link.",
                                        true);
                }
        }
}