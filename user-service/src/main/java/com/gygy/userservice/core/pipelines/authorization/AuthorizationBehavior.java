package com.gygy.userservice.core.pipelines.authorization;

import an.awesome.pipelinr.Command;
import com.gygy.common.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Order(2) // Execute before validation
@RequiredArgsConstructor
public class AuthorizationBehavior implements Command.Middleware {

    @Override
    public <R, C extends Command<R>> R invoke(C command, Next<R> next) {
        Authentication authentication = null;

        // Check if command requires authorization
        if (command instanceof RequiresAuthorization || command instanceof RequiresAuthentication) {
            authentication = SecurityContextHolder.getContext().getAuthentication();

            // Throw exception if not authenticated
            if (authentication == null || !authentication.isAuthenticated()) {
                throw new UnauthorizedException("Authentication required");
            }

            // Check permission requirements
            if (command instanceof RequiresAuthorization) {
                List<String> requiredAuthorizations = ((RequiresAuthorization) command).getRequiredAuthorizations();
                boolean hasAuthorization = hasAnyAuthorization(authentication, requiredAuthorizations);
                if (!hasAuthorization) {
                    throw new UnauthorizedException("Required authorization not found: " +
                            requiredAuthorizations.toString());
                }
            }
        }

        return next.invoke();
    }

    private boolean hasAnyAuthorization(Authentication authentication, List<String> authorizations) {
        return authentication.getAuthorities().stream()
                .anyMatch(authority -> authorizations.stream()
                        .anyMatch(authorization -> authorization.equalsIgnoreCase(authority.getAuthority())));
    }
}