package com.gygy.paymentservice.core.consumer;

import an.awesome.pipelinr.Pipeline;
import com.gygy.common.events.contractservice.ContractCreatedEvent;
import com.gygy.paymentservice.application.bill.command.create.CreateBillCommand;
import com.gygy.paymentservice.application.bill.service.PaymentScheduleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;


@Component
@RequiredArgsConstructor
@Slf4j
public class ContractEventConsumer {
    private final Pipeline pipeline;
    private final PaymentScheduleService paymentScheduleService;

    @Bean
    public Consumer<ContractCreatedEvent> contractCreatedEventConsumer(){ //TODO: customerId ve totalAmount içeren event oluşturulmalı
        return event -> {
            log.info("Contract detail event received: {}", event);
            try {
                //eventten command oluşturdum
                CreateBillCommand command = new CreateBillCommand();
                command.setCustomerId(event.getCustomerId());
                command.setTotalAmount(event.getTotalAmount());
                command.setContractId(event.getContractId());

                pipeline.send(command);
                log.info("Initial bill created for contract");

                paymentScheduleService.createPaymentSchedule(
                        event.getContractId(),
                        event.getCustomerId(),
                        event.getTotalAmount(),
                        event.getDurationInMonths(),
                        event.getStartDate()
                );
            }catch (Exception e){
                log.error("Error occurred while processing contract detail: {}",e.getMessage(), e);
                throw e;
            }
        };
    }
}
