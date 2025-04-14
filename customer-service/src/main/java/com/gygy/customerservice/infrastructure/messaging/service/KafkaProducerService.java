package com.gygy.customerservice.infrastructure.messaging.service;

import com.gygy.customerservice.infrastructure.messaging.event.db.CreatedCorporateCustomerReadEvent;
import com.gygy.customerservice.infrastructure.messaging.event.db.UpdatedCorporateCustomerReadEvent;
import com.gygy.customerservice.infrastructure.messaging.event.db.UpdatedIndividualCustomerReadEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import com.gygy.customerservice.infrastructure.messaging.event.CreatedCorporateCustomerEvent;
import com.gygy.customerservice.infrastructure.messaging.event.CreatedIndividualCustomerEvent;
import com.gygy.customerservice.infrastructure.messaging.event.UpdatedCorporateCustomerEvent;
import com.gygy.customerservice.infrastructure.messaging.event.UpdatedIndividualCustomerEvent;
import com.gygy.customerservice.infrastructure.messaging.event.db.CreatedIndividualCustomerReadEvent;
import com.gygy.customerservice.infrastructure.outbox.OutboxService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaProducerService {

    private final KafkaTemplate<String, CreatedIndividualCustomerEvent> individualCustomerKafkaTemplate;
    private final KafkaTemplate<String, CreatedCorporateCustomerEvent> corporateCustomerKafkaTemplate;
    private final KafkaTemplate<String, UpdatedIndividualCustomerEvent> updatedIndividualCustomerKafkaTemplate;
    private final KafkaTemplate<String, UpdatedCorporateCustomerEvent> updatedCorporateCustomerKafkaTemplate;
    private final OutboxService outboxService;

    public void sendCreatedIndividualCustomerEvent(CreatedIndividualCustomerEvent createdIndividualCustomerEvent) {
        log.info("Sending individual customer created event: {}", createdIndividualCustomerEvent);
        Message<CreatedIndividualCustomerEvent> message = MessageBuilder
                .withPayload(createdIndividualCustomerEvent)
                .setHeader(KafkaHeaders.TOPIC, "individual-customer-created-topic")
                .build();
        individualCustomerKafkaTemplate.send(message);
    }

    public void sendCreatedCorporateCustomerEvent(CreatedCorporateCustomerEvent createdCorporateCustomerEvent) {
        log.info("Sending corporate customer created event: {}", createdCorporateCustomerEvent);
        Message<CreatedCorporateCustomerEvent> message = MessageBuilder
                .withPayload(createdCorporateCustomerEvent)
                .setHeader(KafkaHeaders.TOPIC, "corporate-customer-created-topic")
                .build();
        corporateCustomerKafkaTemplate.send(message);
    }

    public void sendUpdatedIndividualCustomerEvent(UpdatedIndividualCustomerEvent updatedIndividualCustomerEvent) {
        log.info("Sending individual customer updated event: {}", updatedIndividualCustomerEvent);
        Message<UpdatedIndividualCustomerEvent> message = MessageBuilder
                .withPayload(updatedIndividualCustomerEvent)
                .setHeader(KafkaHeaders.TOPIC, "individual-customer-updated-topic")
                .build();
        updatedIndividualCustomerKafkaTemplate.send(message);
    }

    public void sendUpdatedCorporateCustomerEvent(UpdatedCorporateCustomerEvent updatedCorporateCustomerEvent) {
        log.info("Sending corporate customer updated event: {}", updatedCorporateCustomerEvent);
        Message<UpdatedCorporateCustomerEvent> message = MessageBuilder
                .withPayload(updatedCorporateCustomerEvent)
                .setHeader(KafkaHeaders.TOPIC, "corporate-customer-updated-topic")
                .build();
        updatedCorporateCustomerKafkaTemplate.send(message);
    }
    
    public void sendCreatedIndividualCustomerReadEvent(CreatedIndividualCustomerReadEvent createdIndividualCustomerReadEvent) {
        log.info("Saving individual customer read created event to outbox: {}", createdIndividualCustomerReadEvent);
        outboxService.saveOutbox(
                "INDIVIDUAL_CUSTOMER",
                createdIndividualCustomerReadEvent.getId().toString(),
                "INDIVIDUAL_CUSTOMER_READ_CREATED",
                createdIndividualCustomerReadEvent,
                "individual-customer-read-created-topic"
        );
    }

    public void sendCreatedCorporateCustomerReadEvent(CreatedCorporateCustomerReadEvent createdCorporateCustomerReadEvent) {
        log.info("Saving corporate customer read created event to outbox: {}", createdCorporateCustomerReadEvent);
        outboxService.saveOutbox(
                "CORPORATE_CUSTOMER",
                createdCorporateCustomerReadEvent.getId().toString(),
                "CORPORATE_CUSTOMER_READ_CREATED",
                createdCorporateCustomerReadEvent,
                "corporate-customer-read-created-topic"
        );
    }

    public void sendUpdatedIndividualCustomerReadEvent(UpdatedIndividualCustomerReadEvent updatedIndividualCustomerReadEvent) {
        log.info("Saving individual customer read updated event to outbox: {}", updatedIndividualCustomerReadEvent);
        outboxService.saveOutbox(
                "INDIVIDUAL_CUSTOMER",
                updatedIndividualCustomerReadEvent.getId().toString(),
                "INDIVIDUAL_CUSTOMER_READ_UPDATED",
                updatedIndividualCustomerReadEvent,
                "individual-customer-read-updated-topic"
        );
    }

    public void sendUpdatedCorporateCustomerReadEvent(UpdatedCorporateCustomerReadEvent updatedCorporateCustomerReadEvent) {
        log.info("Saving corporate customer read updated event to outbox: {}", updatedCorporateCustomerReadEvent);
        outboxService.saveOutbox(
                "CORPORATE_CUSTOMER",
                updatedCorporateCustomerReadEvent.getId().toString(),
                "CORPORATE_CUSTOMER_READ_UPDATED",
                updatedCorporateCustomerReadEvent,
                "corporate-customer-read-updated-topic"
        );
    }
}
