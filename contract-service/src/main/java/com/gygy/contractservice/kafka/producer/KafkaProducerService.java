package com.gygy.contractservice.kafka.producer;

import com.gygy.common.events.contractservice.ContractDetailEvent;
import io.micrometer.observation.annotation.Observed;
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

    private final KafkaTemplate<String, ContractDetailEvent> kafkaTemplate;

    @Observed(name = "kafka.produce.contractDetailEvent")
    public void sendCreatedContractDetailEvent(ContractDetailEvent contractDetailEvent) {
        log.info("Sending contract detail  event: {}", contractDetailEvent);
        Message<ContractDetailEvent> message = MessageBuilder
                .withPayload(contractDetailEvent)
                .setHeader(KafkaHeaders.TOPIC, "contract_detail_created_topic")
                .build();
        kafkaTemplate.send(message);
    }


}
