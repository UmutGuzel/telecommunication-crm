package com.gygy.notificationservice.application.notification;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;

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
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject("Password Reset Request");
            String htmlMsg = "<p>Hello,</p>" +
                    "<p>To reset your password, please click the link below:</p>" +
                    "<p><a href=\"" + resetLink + "\">Reset Password</a></p>" +
                    "<p>This link will expire in 1 hour.</p>" +
                    "<p>If you did not request a password reset, please ignore this email.</p>" +
                    "<br/>" +
                    "<img src='cid:signatureImage' alt='Signature' style='max-width: 100%; max-height: 200px; height: auto;'/>";
            helper.setText(htmlMsg, true);
            ClassPathResource resource = new ClassPathResource(
                    "images/20250413_0725_Logo Text Change_remix_01jrpp7r5ze0wvve8yvq5659b6.png");
            helper.addInline("signatureImage", resource);

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

    @Override
    public void sendGenericEmail(String toEmail, String subject, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject(subject);
            message.setText(body);

            mailSender.send(message);
            log.info("Generic email sent successfully to: {} | Subject: {}", toEmail, subject);
        } catch (Exception e) {
            log.error("Failed to send generic email to: {}", toEmail, e);
        }
    }

}