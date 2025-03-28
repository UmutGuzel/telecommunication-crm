package com.gygy.userservice.application.user.command.ActivateUser;

import an.awesome.pipelinr.Command;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.gygy.common.events.UserActivationEvent;
import com.gygy.userservice.core.configuration.ApplicationConfig;
import com.gygy.userservice.entity.User;
import com.gygy.userservice.persistance.UserRepository;
import com.gygy.userservice.application.user.rule.UserRule;
import com.gygy.userservice.application.user.mapper.UserMapper;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ActivateUserCommand implements Command<ActivateUserResponse> {
    private String token;

    @Component
    @RequiredArgsConstructor
    public static class ActivateUserCommandHandler
            implements Command.Handler<ActivateUserCommand, ActivateUserResponse> {
        private final UserRepository userRepository;
        private final UserRule userRule;
        private final UserMapper userMapper;
        private final KafkaTemplate<String, UserActivationEvent> userActivationKafkaTemplate;
        private final ApplicationConfig applicationConfig;

        @Override
        public ActivateUserResponse handle(ActivateUserCommand command) {
            // Find user by activation token
            User user = userRepository.findByActivationToken(command.getToken())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid activation token"));

            // Check token expiry using rule class
            userRule.checkActivationTokenNotExpired(user);

            // Activate the user using mapper
            user = userMapper.activateUser(user);
            userRepository.save(user);

            // Publish user activation event
            UserActivationEvent event = UserActivationEvent.builder()
                    .email(user.getEmail())
                    .name(user.getName())
                    .activationDate(LocalDateTime.now())
                    .build();

            userActivationKafkaTemplate.send(applicationConfig.getUserActivationTopic(), event);

            return new ActivateUserResponse("User activated successfully");
        }
    }
}