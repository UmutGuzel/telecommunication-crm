package com.gygy.userservice.core.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PasswordResetEvent {
    private String userId;
    private String email;
    private String resetToken;
    private LocalDateTime expiryDate;
    private String resetLink;
}