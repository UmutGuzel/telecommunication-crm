package com.gygy.userservice.application.user.rule;

import org.springframework.stereotype.Component;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.gygy.userservice.entity.User;
import lombok.RequiredArgsConstructor;

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
}
