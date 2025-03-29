package com.gygy.userservice.core.configuration;

import com.gygy.common.security.BaseSecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityService {

    private final BaseSecurityService baseSecurityService;

    private static final String[] USER_SERVICE_WHITELIST = {
            "/api/v1/auth/**",
            "/actuator/**"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Configure with base security service first
        HttpSecurity securityConfig = baseSecurityService.configureCoreSecurity(http);

        // Add user-service specific configuration
        securityConfig.authorizeHttpRequests(authorize -> authorize
                .requestMatchers(USER_SERVICE_WHITELIST).permitAll()
                .anyRequest().authenticated());

        return securityConfig.build();
    }
}