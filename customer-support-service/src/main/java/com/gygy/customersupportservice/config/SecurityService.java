package com.gygy.customersupportservice.config;

import com.gygy.common.security.BaseSecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityService {

    private final BaseSecurityService baseSecurityService;

    private static final String[] USER_SERVICE_WHITELIST = {
            "/actuator/**",
            "/swagger-ui.html",
            "/api-docs/**"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        HttpSecurity securityConfig = baseSecurityService.configureCoreSecurity(http);

        securityConfig.authorizeHttpRequests(authorize -> authorize
                .requestMatchers(USER_SERVICE_WHITELIST).permitAll()

                .anyRequest().permitAll());

        return securityConfig.build();
    }

}