package com.gygy.common.kafka.producer;

import com.gygy.common.events.base.BaseEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventProducer {

    private final StreamBridge streamBridge;

    @Retryable(value = { Exception.class }, // Hangi hatalarda retry yapacağını belirtiyor.
            maxAttempts = 3, // Maksimum kaç kez tekrar deneyecek.
            backoff = @Backoff(delay = 2000) // 2 saniye bekleyerek tekrar deniyor.
    )

    // baseevent kullanarak polymorphizm sağladım, bu sayede yeni event geldiğinde
    // sadece event ekleyip extend etmek yeterli olacaktır.
    public void sendEvent(String topic, BaseEvent event) {
        try {
            log.debug("Event gönderme denemesi başladı - Topic: {}", topic);

            // Topic adını function adına çevir
            String functionName = topic.replace("-topic", "Function-out-0");

            boolean sent = streamBridge.send(functionName, event);

            if (sent) {
                log.info("Event başarıyla gönderildi - Topic: {}, Function: {}, Event: {}",
                        topic, functionName, event);
            } else {
                log.error("Event gönderilemedi - Topic: {}, Function: {}", topic, functionName);
                throw new RuntimeException("Event gönderilemedi");
            }
        } catch (Exception e) {
            log.error("Event gönderilirken hata oluştu - Topic: {}, Event: {}, Hata: {}",
                    topic, event, e.getMessage(), e);
            throw new RuntimeException("Kafka event gönderimi başarısız", e);
        }
    }}