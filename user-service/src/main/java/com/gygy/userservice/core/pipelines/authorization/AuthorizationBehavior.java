package com.gygy.userservice.core.pipelines.authorization;

import an.awesome.pipelinr.Command;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

@Component
@Order(1) // Execute before validation
@RequiredArgsConstructor
public class AuthorizationBehavior implements Command.Middleware {

    @Override
    public <R, C extends Command<R>> R invoke(C command, Next<R> next) {
        Authentication authentication = null;

        // Check if command requires authorization
        if (command instanceof RequiresPermission || command instanceof RequiresRole) {
            authentication = SecurityContextHolder.getContext().getAuthentication();

            // Throw exception if not authenticated
            if (authentication == null || !authentication.isAuthenticated()) {
                throw new AccessDeniedException("Authentication required");
            }
        }

        // Check permission requirements
        if (command instanceof RequiresPermission) {
            String[] requiredPermissions = ((RequiresPermission) command).getRequiredPermissions();
            boolean hasPermission = hasAnyPermission(authentication, requiredPermissions);
            if (!hasPermission) {
                throw new AccessDeniedException("Required permission not found: " +
                        Arrays.toString(requiredPermissions));
            }
        }

        // Check role requirements
        if (command instanceof RequiresRole) {
            String[] requiredRoles = ((RequiresRole) command).getRequiredRoles();
            boolean hasRole = hasAnyRole(authentication, requiredRoles);
            if (!hasRole) {
                throw new AccessDeniedException("Required role not found: " +
                        Arrays.toString(requiredRoles));
            }
        }

        return next.invoke();
    }

    private boolean hasAnyPermission(Authentication authentication, String[] permissions) {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Collection<String> userPermissions = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .filter(authority -> !authority.startsWith("ROLE_"))
                .collect(Collectors.toList());

        return Arrays.stream(permissions)
                .anyMatch(userPermissions::contains);
    }

    private boolean hasAnyRole(Authentication authentication, String[] roles) {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Collection<String> userRoles = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .filter(authority -> authority.startsWith("ROLE_"))
                .map(authority -> authority.substring(5)) // Remove "ROLE_" prefix
                .collect(Collectors.toList());

        return Arrays.stream(roles)
                .anyMatch(userRoles::contains);
    }
}