package com.gygy.userservice.core.configration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app-config")
@Data
public class ApplicationConfig {
    private String gatewayUrl = "http://localhost:8080"; // Default value
    private String resetPasswordPath = "/api/auth/reset-password";
}