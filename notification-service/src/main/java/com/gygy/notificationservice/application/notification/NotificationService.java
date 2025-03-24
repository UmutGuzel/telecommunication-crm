package com.gygy.notificationservice.application.notification;

import com.gygy.notificationservice.entity.Notification;
import com.gygy.notificationservice.persistence.NotificationRepository;
import com.gygy.notificationservice.core.kafka.KafkaProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
/**
 * Notification servisi, iş mantığını içerir.
 * - Yeni bildirim oluşturur ve Kafka'ya gönderir.
 * - Kullanıcının bildirimlerini listeler.
 * - Bildirimleri okundu olarak işaretler.
 */
@Service
public class NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private KafkaProducer kafkaProducer;

    public Notification createNotification(String userId, String message) {
        Notification notification = new Notification(userId, message);
        kafkaProducer.sendMessage("New notification for user " + userId + ": " + message);
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