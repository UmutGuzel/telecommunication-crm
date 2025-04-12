// ================== DOCKERFILE ==================
// Docker, Java uygulamasını çalıştırmak için OpenJDK kullanıyor.
// Önce bağımlılıkları yükleyip, uygulamayı çalıştıracak.

/**
 * Dockerfile, Notification Service uygulamasını bir Docker konteyneri içinde çalıştırmak için gereklidir.
 * - OpenJDK 17 kullanılmaktadır.
 * - Uygulama, "notification-service.jar" olarak çalıştırılır.
 */
FROM openjdk:21-jdk-slim
WORKDIR /app
COPY target/notification-service.jar notification-service.jar
EXPOSE 9080
CMD ["java", "-jar", "notification-service.jar"]
