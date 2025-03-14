package com.gygy.notificationservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * Notification Service uygulaması için ana giriş noktasıdır.
 * Spring Boot kullanarak bir mikroservis olarak çalışır.
 * MongoDB ve Kafka entegrasyonuna sahiptir.
 */
@SpringBootApplication
@EnableMongoRepositories
public class NotificationServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(NotificationServiceApplication.class, args);
    }
}