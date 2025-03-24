package com.gygy.customerservice.infrastructure.messaging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaProducerService {

    private final KafkaTemplate<String, CreatedIndividualCustomerEvent> kafkaTemplate;

    public void sendCreatedIndividualCustomerEvent(CreatedIndividualCustomerEvent createdIndividualCustomerEvent) {
        log.info("Sending customer created event: {}", createdIndividualCustomerEvent);
        Message<CreatedIndividualCustomerEvent> message = MessageBuilder
                .withPayload(createdIndividualCustomerEvent)
                .setHeader(KafkaHeaders.TOPIC, "customer-created-topic")
                .build();
        kafkaTemplate.send(message);
    }
}
