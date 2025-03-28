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
import com.gygy.userservice.core.configuration.ApplicationConfig;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.kafka.core.KafkaTemplate;
import com.gygy.common.events.UserActivationEvent;
import java.time.LocalDateTime;
import java.util.UUID;

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
        private final KafkaTemplate<String, UserActivationEvent> userActivationKafkaTemplate;
        private final ApplicationConfig applicationConfig;

        @Override
        public CreateUserResponse handle(CreateUserCommand command) {
            User user = userRepository.findByEmail(command.getEmail()).orElse(null);
            userRule.checkUserNotExists(user);
            command.setPassword(passwordEncoder.encode(command.getPassword()));

            // User mapper now handles all activation details
            User newUser = userMapper.toEntity(command);
            userRepository.save(newUser);

            // Get activation link from mapper
            String activationLink = userMapper.generateActivationLink(newUser);

            // Publish user activation event
            UserActivationEvent event = UserActivationEvent.builder()
                    .email(newUser.getEmail())
                    .name(newUser.getName())
                    .activationDate(LocalDateTime.now())
                    .activationLink(activationLink)
                    .build();

            userActivationKafkaTemplate.send(applicationConfig.getUserActivationTopic(), event);

            return new CreateUserResponse(newUser.getId(),
                    "User created successfully. Please check your email to activate your account.");
        }
    }
}
