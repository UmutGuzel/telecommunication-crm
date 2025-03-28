package com.gygy.userservice.core.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import lombok.Getter;

@Configuration
@Getter
public class ApplicationConfig {

    @Value("${app-config.gateway-url}")
    private String gatewayUrl;

    @Value("${app-config.activation-path:/api/users/activate}")
    private String activationPath;

    @Value("${app-config.activation-token-expiry-hours:24}")
    private int activationTokenExpiryHours;

    @Value("${app-config.kafka.topics.user-activation-events}")
    private String userActivationTopic;

    @Value("${app-config.reset-password-path}")
    private String resetPasswordPath;

    @Value("${app-config.kafka.topics.password-reset-events}")
    private String passwordResetEvents;
}