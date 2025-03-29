package com.gygy.userservice.application.user.rule;

import org.springframework.stereotype.Component;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.gygy.userservice.entity.User;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class UserRule {
    private final PasswordEncoder passwordEncoder;

    public void checkUserNotExists(User user) {
        if (user != null) {
            throw new RuntimeException("User already exists");
        }
    }

    public void checkUserExists(User user) {
        if (user == null) {
            throw new RuntimeException("User not found");
        }
    }

    public void checkPassword(User user, String password) {
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }
    }

    public void checkActivationTokenNotExpired(User user) {
        if (user.getActivationTokenExpiry() == null) {
            throw new IllegalArgumentException("Activation token not found");
        }

        if (user.getActivationTokenExpiry().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Activation token has expired");
        }
    }
}
