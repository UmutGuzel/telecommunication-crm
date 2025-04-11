package com.gygy.customerservice.infrastructure.messaging.service;

import com.gygy.customerservice.application.customer.mapper.CustomerMapper;
import com.gygy.customerservice.application.customer.rule.CustomerRule;
import com.gygy.customerservice.domain.entity.Customer;
import com.gygy.customerservice.infrastructure.messaging.event.NotificationPreferencesChangedEvent;
import com.gygy.customerservice.persistance.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaConsumerService {
    private final CustomerRepository customerRepository;
    private final CustomerRule customerRule;
    private final CustomerMapper customerMapper;

    @KafkaListener(topics = "notification-preferences-changed-topic")
    public void consumePaymentSuccessNotification(NotificationPreferencesChangedEvent event) {
        log.info("Consuming the message from notification-preferences-changed-topic: {}", event);
        Customer customer = customerRepository.findById(event.getId()).orElse(null);
        customerRule.checkCustomerExists(customer);
        customerMapper.updateCustomerNotificationPreferences(customer, event);
    }
}
