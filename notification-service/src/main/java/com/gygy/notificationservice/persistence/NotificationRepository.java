package com.gygy.notificationservice.persistence;

import com.gygy.notificationservice.entity.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * MongoDB için Notification repository arayüzü.
 * Kullanıcının bildirimlerini çekmek için özel sorgular içerir.
 */
@Repository
public interface NotificationRepository extends MongoRepository<Notification, String> {
    List<Notification> findByUserId(String userId);
}