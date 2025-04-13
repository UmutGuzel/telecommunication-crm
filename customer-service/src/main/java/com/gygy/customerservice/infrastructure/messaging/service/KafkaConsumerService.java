package com.gygy.customerservice.infrastructure.messaging.service;

import com.gygy.customerservice.application.customer.mapper.CustomerMapper;
import com.gygy.customerservice.application.customer.rule.CustomerRule;
import com.gygy.customerservice.domain.entity.Customer;
import com.gygy.customerservice.domain.entity.CustomerReadEntity;
import com.gygy.customerservice.infrastructure.messaging.event.NotificationPreferencesChangedEvent;
import com.gygy.customerservice.infrastructure.messaging.event.db.CreatedIndividualCustomerReadEvent;
import com.gygy.customerservice.infrastructure.persistence.repository.CustomerReadRepository;
import com.gygy.customerservice.infrastructure.persistence.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaConsumerService {
    private final CustomerRepository customerRepository;
    private final CustomerReadRepository customerReadRepository;
    private final CustomerRule customerRule;
    private final CustomerMapper customerMapper;

    @KafkaListener(topics = "notification-preferences-changed-topic", groupId = "notification-group")
    public void consumePaymentSuccessNotification(NotificationPreferencesChangedEvent event) {
        log.info("Consuming the message from notification-preferences-changed-topic: {}", event);
        Customer customer = customerRepository.findById(event.getId()).orElse(null);
        customerRule.checkCustomerExists(customer);
        customerMapper.updateCustomerNotificationPreferences(customer, event);
    }
    
    @KafkaListener(topics = "individual-customer-read-created-topic", groupId = "customer-read-group")
    public void consumeIndividualCustomerReadCreatedEvent(CreatedIndividualCustomerReadEvent event) {
        log.info("Consuming the message from individual-customer-read-created-topic: {}", event);
        
        CustomerReadEntity customerReadEntity = CustomerReadEntity.builder()
                .id(event.getId())
                .email(event.getEmail())
                .phoneNumber(event.getPhoneNumber())
                .type(event.getType())
                .name(event.getName())
                .surname(event.getSurname())
                .gender(event.getGender())
                .birthDate(event.getBirthDate())
                .build();
        
        customerReadRepository.save(customerReadEntity);
    }
}
