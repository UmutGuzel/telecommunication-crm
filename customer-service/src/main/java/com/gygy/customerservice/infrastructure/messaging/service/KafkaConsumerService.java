package com.gygy.customerservice.infrastructure.messaging.service;

import com.gygy.customerservice.application.customer.mapper.CustomerMapper;
import com.gygy.customerservice.application.customer.rule.CustomerRule;
import com.gygy.customerservice.domain.entity.Customer;
import com.gygy.customerservice.domain.entity.CustomerReadEntity;
import com.gygy.customerservice.infrastructure.messaging.event.NotificationPreferencesChangedEvent;
import com.gygy.customerservice.infrastructure.messaging.event.db.CreatedCorporateCustomerReadEvent;
import com.gygy.customerservice.infrastructure.messaging.event.db.CreatedIndividualCustomerReadEvent;
import com.gygy.customerservice.infrastructure.persistence.repository.CustomerReadRepository;
import com.gygy.customerservice.infrastructure.persistence.repository.CustomerRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.kafka.annotation.KafkaListener;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gygy.customerservice.infrastructure.messaging.event.db.UpdatedCorporateCustomerReadEvent;
import com.gygy.customerservice.infrastructure.messaging.event.db.UpdatedIndividualCustomerReadEvent;

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
    @Transactional
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

    @KafkaListener(topics = "corporate-customer-read-created-topic", groupId = "customer-read-group")
    @Transactional
    public void consumeCorporateCustomerReadCreatedEvent(CreatedCorporateCustomerReadEvent event) {
        log.info("Consuming the message from corporate-customer-read-created-topic: {}", event);

        CustomerReadEntity customerReadEntity = CustomerReadEntity.builder()
                .id(event.getId())
                .email(event.getEmail())
                .phoneNumber(event.getPhoneNumber())
                .type(event.getType())
                .companyName(event.getCompanyName())
                .contactPersonName(event.getContactPersonName())
                .contactPersonSurname(event.getContactPersonSurname())
                .build();

        customerReadRepository.save(customerReadEntity);
    }

    @KafkaListener(topics = "individual-customer-read-updated-topic", groupId = "customer-read-group")
    @Transactional
    public void consumeIndividualCustomerReadUpdatedEvent(UpdatedIndividualCustomerReadEvent event) {
        log.info("Consuming the message from individual-customer-read-updated-topic: {}", event);

        CustomerReadEntity existingEntity = customerReadRepository.findById(event.getId())
                .orElseThrow(() -> new RuntimeException("Customer read entity not found with id: " + event.getId()));

        if (event.getEmail() != null) {
            existingEntity.setEmail(event.getEmail());
        }
        if (event.getPhoneNumber() != null) {
            existingEntity.setPhoneNumber(event.getPhoneNumber());
        }
        if (event.getName() != null) {
            existingEntity.setName(event.getName());
        }
        if (event.getSurname() != null) {
            existingEntity.setSurname(event.getSurname());
        }
        if (event.getGender() != null) {
            existingEntity.setGender(event.getGender());
        }
        if (event.getBirthDate() != null) {
            existingEntity.setBirthDate(event.getBirthDate());
        }
        customerReadRepository.save(existingEntity);
    }

    @KafkaListener(topics = "corporate-customer-read-updated-topic", groupId = "customer-read-group")
    @Transactional
    public void consumeCorporateCustomerReadUpdatedEvent(UpdatedCorporateCustomerReadEvent event) {
        log.info("Consuming the message from corporate-customer-read-updated-topic: {}", event);

        CustomerReadEntity existingEntity = customerReadRepository.findById(event.getId())
                .orElseThrow(() -> new RuntimeException("Customer read entity not found with id: " + event.getId()));

        if (event.getEmail() != null) {
            existingEntity.setEmail(event.getEmail());
        }
        if (event.getPhoneNumber() != null) {
            existingEntity.setPhoneNumber(event.getPhoneNumber());
        }
        if (event.getCompanyName() != null) {
            existingEntity.setCompanyName(event.getCompanyName());
        }
        if (event.getContactPersonName() != null) {
            existingEntity.setContactPersonName(event.getContactPersonName());
        }
        if (event.getContactPersonSurname() != null) {
            existingEntity.setContactPersonSurname(event.getContactPersonSurname());
        }

        customerReadRepository.save(existingEntity);
    }
}
