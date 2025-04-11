package com.gygy.planservice.config;

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
            "/api/v1/auth/**",
            "/actuator/**"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        HttpSecurity securityConfig = baseSecurityService.configureCoreSecurity(http);

        securityConfig.authorizeHttpRequests(authorize -> authorize
                .requestMatchers(USER_SERVICE_WHITELIST).permitAll()

                // Plan Controller endpoints
                .requestMatchers(HttpMethod.POST, "/api/plans").hasAnyAuthority("PLAN_CREATE", "ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/plans", "/api/plans/active")
                .permitAll()
                .requestMatchers(HttpMethod.GET, "/api/plans/{id}")
                .permitAll()
                .requestMatchers(HttpMethod.PUT, "/api/plans/{id}")
                .hasAnyAuthority("PLAN_UPDATE", "ADMIN", "MANAGER")
                .requestMatchers(HttpMethod.DELETE, "/api/plans/{id}").hasAnyAuthority("PLAN_DELETE", "ADMIN")

                // Category Controller endpoints
                .requestMatchers(HttpMethod.POST, "/api/plan-categories")
                .hasAnyAuthority("CATEGORY_CREATE", "ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/plan-categories", "/api/plan-categories/active")
                .permitAll()
                .requestMatchers(HttpMethod.GET, "/api/plan-categories/{id}")
                .permitAll()
                .requestMatchers(HttpMethod.PUT, "/api/plan-categories/{id}")
                .hasAnyAuthority("CATEGORY_UPDATE", "ADMIN", "MANAGER")
                .requestMatchers(HttpMethod.DELETE, "/api/plan-categories/{id}")
                .hasAnyAuthority("CATEGORY_DELETE", "ADMIN")

                // Contract Controller endpoints
                .requestMatchers(HttpMethod.POST, "/api/plan-contracts")
                .hasAnyAuthority("CONTRACT_CREATE", "ADMIN", "MANAGER")
                .requestMatchers(HttpMethod.GET, "/api/plan-contracts", "/api/plan-contracts/active")
                .permitAll()
                .requestMatchers(HttpMethod.GET, "/api/plan-contracts/{id}")
                .permitAll()
                .requestMatchers(HttpMethod.PUT, "/api/plan-contracts/{id}")
                .hasAnyAuthority("CONTRACT_UPDATE", "ADMIN", "MANAGER")
                .requestMatchers(HttpMethod.DELETE, "/api/plan-contracts/{id}")
                .hasAnyAuthority("CONTRACT_DELETE", "ADMIN")

                .anyRequest().authenticated());

        return securityConfig.build();
    }

}