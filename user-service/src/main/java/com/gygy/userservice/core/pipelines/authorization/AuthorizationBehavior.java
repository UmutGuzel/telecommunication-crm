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
@Order(2) // Execute before validation
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
        return authentication.getAuthorities().stream()
                .anyMatch(authority -> Arrays.stream(permissions)
                        .anyMatch(permission -> permission.equalsIgnoreCase(authority.getAuthority())));
    }

    private boolean hasAnyRole(Authentication authentication, String[] roles) {
        return authentication.getAuthorities().stream()
                .anyMatch(authority -> Arrays.stream(roles)
                        .anyMatch(role -> role.equalsIgnoreCase(authority.getAuthority())));
    }
}