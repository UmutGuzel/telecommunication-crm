package com.gygy.contractservice.core.configuration;

import com.gygy.common.security.BaseSecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityService {
    private final BaseSecurityService baseSecurityService;

    private static final String[] USER_SERVICE_WHITELIST = {
            "/api/v1/auth/**",
            "/actuator/**"
    };

    public SecurityService(BaseSecurityService baseSecurityService) {
        this.baseSecurityService = baseSecurityService;
    }

    /*
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

     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        HttpSecurity securityConfig = baseSecurityService.configureCoreSecurity(http);

        securityConfig
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(req -> req
                        .requestMatchers("/").authenticated().anyRequest().permitAll());
        return http.build();
    }



}
