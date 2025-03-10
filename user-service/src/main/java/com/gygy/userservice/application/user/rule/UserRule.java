package com.gygy.userservice.application.user.rule;

import org.springframework.stereotype.Component;
import com.gygy.userservice.entity.User;

@Component
public class UserRule {
    public void checkUserNotExists(User user) {
        if (user != null) {
            throw new RuntimeException("User already exists");
        }
    }
}
