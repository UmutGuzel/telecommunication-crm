package com.gygy.notificationservice.application.notification;

public interface EmailService {
    void sendPasswordResetEmail(String email, String resetLink);

    void sendActivationEmail(String email, String name, String activationLink);
}