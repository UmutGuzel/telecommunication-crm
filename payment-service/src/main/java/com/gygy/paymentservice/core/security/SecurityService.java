package com.gygy.paymentservice.core.security;

import com.gygy.common.security.BaseSecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Import(com.gygy.common.security.BaseSecurityService.class)
public class SecurityService {

    private final BaseSecurityService baseSecurityService;
    //TODO: hatayı çöz - DONE

    private static final String[] USER_SERVICE_WHITELIST = {
            "/api/v1/auth/**",
            "/actuator/**",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/swagger-resources/**",
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

