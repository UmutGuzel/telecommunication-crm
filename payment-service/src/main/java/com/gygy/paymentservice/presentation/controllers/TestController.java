package com.gygy.paymentservice.presentation.controllers;

import com.gygy.common.constants.KafkaTopics;
import com.gygy.common.events.paymentservice.bill.BillCreatedEvent;
import com.gygy.common.kafka.producer.EventProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
@Slf4j
public class TestController {
    private final EventProducer eventProducer;

    @PostMapping("/kafka")
    public ResponseEntity<String> testKafka() {
        try {
            log.info("Kafka test başlatılıyor");

            BillCreatedEvent event = new BillCreatedEvent();
            event.setBillId(UUID.randomUUID());
            event.setCustomerId(UUID.randomUUID());
            event.setTotalAmount(new BigDecimal("100.00"));
            event.setDueDate(LocalDate.now().plusDays(30));
            event.setServiceName("payment-service");

            log.info("Test eventi hazırlandı: {}", event);
            eventProducer.sendEvent(KafkaTopics.BILL_CREATED, event);
            log.info("Test eventi gönderildi");

            return ResponseEntity.ok("Test event başarıyla gönderildi");
        } catch (Exception e) {
            log.error("Test sırasında hata oluştu: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Hata: " + e.getMessage());
        }
    }
}