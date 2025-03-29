package com.gygy.notificationservice.application.notification;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Override
    public void sendPasswordResetEmail(String toEmail, String resetLink) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("Password Reset Request");
            message.setText("To reset your password, please click the link below:\n\n" + resetLink +
                    "\n\nThis link will expire in 1 hour.\n\nIf you did not request a password reset, please ignore this email.");

            mailSender.send(message);
            log.info("Password reset email sent successfully to: {}", toEmail);
        } catch (Exception e) {
            log.error("Failed to send password reset email to: {}", toEmail, e);
        }
    }

    @Override
    public void sendActivationEmail(String toEmail, String name, String activationLink) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("Activate Your Account");
            message.setText("Hello " + name + ",\n\n" +
                    "Welcome to our service! Please click the link below to activate your account:\n\n" +
                    activationLink + "\n\n" +
                    "This link will expire in 24 hours.\n\n" +
                    "If you did not create an account, please ignore this email.");

            mailSender.send(message);
            log.info("Activation email sent successfully to: {}", toEmail);
        } catch (Exception e) {
            log.error("Failed to send activation email to: {}", toEmail, e);
        }
    }
}