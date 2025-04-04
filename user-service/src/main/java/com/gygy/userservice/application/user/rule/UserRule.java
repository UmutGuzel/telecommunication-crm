package com.gygy.userservice.application.user.rule;

import org.springframework.stereotype.Component;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.gygy.common.exception.BadRequestException;
import com.gygy.common.exception.ConflictException;
import com.gygy.common.exception.NotFoundException;
import com.gygy.userservice.core.exception.UserServiceException;
import com.gygy.userservice.entity.User;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class UserRule {
    private final PasswordEncoder passwordEncoder;

    public void checkUserNotExists(User user) {
        if (user != null) {
            throw ConflictException.duplicateValue("User", "email", user.getEmail());
        }
    }

    public void checkUserExists(User user) {
        if (user == null) {
            throw NotFoundException.forResource("User", "id");
        }
    }

    public void checkPassword(User user, String password) {
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadRequestException("Invalid password");
        }
    }

    public void checkActivationTokenNotExpired(User user) {
        if (user.getActivationTokenExpiry() == null) {
            throw UserServiceException.invalidActivationToken("token not found");
        }

        if (user.getActivationTokenExpiry().isBefore(LocalDateTime.now())) {
            throw UserServiceException.invalidActivationToken("expired");
        }
    }

    public void checkUserActive(User user) {
        if (!user.isActive()) {
            throw UserServiceException.accountInactive(user.getEmail());
        }
    }
}
