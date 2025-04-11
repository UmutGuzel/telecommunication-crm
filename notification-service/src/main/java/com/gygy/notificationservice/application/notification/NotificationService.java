package com.gygy.notificationservice.application.notification;

import com.gygy.notificationservice.entity.Notification;
import com.gygy.notificationservice.persistence.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Notification servisi, iş mantığını içerir.
 * - Yeni bildirim oluşturur ve Kafka'ya gönderir.
 * - Kullanıcının bildirimlerini listeler.
 * - Bildirimleri okundu olarak işaretler.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;
    // private final KafkaProducer kafkaProducer;

    public Notification createNotification(String userId, String message) {
        Notification notification = new Notification(userId, message);
        // kafkaProducer.sendMessage("New notification for user " + userId + ": " +
        // message);
        return notificationRepository.save(notification);
    }

    public List<Notification> getUserNotifications(String userId) {
        return notificationRepository.findByUserId(userId);
    }

    public void markAsRead(String notificationId) {
        notificationRepository.findById(notificationId).ifPresent(notification -> {
            notification.setRead(true);
            notificationRepository.save(notification);
        });
    }
}