package com.gygy.notificationservice.controller;

import com.gygy.notificationservice.application.notification.NotificationService;
import com.gygy.notificationservice.entity.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * REST API üzerinden bildirimleri yönetmek için kullanılan controller.
 */
@RestController
@RequestMapping("/notifications")
public class NotificationController {
    @Autowired
    private NotificationService notificationService;

    @PostMapping("/send")
    public Notification sendNotification(@RequestParam String userId, @RequestParam String message) {
        return notificationService.createNotification(userId, message);
    }

    @GetMapping("/user/{userId}")
    public List<Notification> getUserNotifications(@PathVariable String userId) {
        return notificationService.getUserNotifications(userId);
    }

    @PostMapping("/read/{id}")
    public void markAsRead(@PathVariable String id) {
        notificationService.markAsRead(id);
    }
}