package com.gygy.paymentservice.core.consumer;

import an.awesome.pipelinr.Pipeline;
import com.gygy.common.events.contractservice.ContractCreatedEvent;
import com.gygy.paymentservice.application.bill.command.create.CreateBillCommand;
import com.gygy.paymentservice.application.bill.command.create.dto.CreatedBillResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
class ContractEventConsumerTest {

    @Mock
    private Pipeline pipeline;

    @InjectMocks
    private ContractEventConsumer consumer;

    @Captor
    private ArgumentCaptor<CreateBillCommand> commandCaptor;

    @Test
    void shouldProcessContractCreatedEvent() {
        // Given
        UUID customerId = UUID.randomUUID();
        BigDecimal totalAmount = new BigDecimal("100.00");

        ContractCreatedEvent event = new ContractCreatedEvent();
        event.setCustomerId(customerId);
        event.setTotalAmount(totalAmount);

        CreatedBillResponse expectedResponse = new CreatedBillResponse(
                UUID.randomUUID(),
                totalAmount,
                null,
                null,
                null);

        when(pipeline.send(any(CreateBillCommand.class))).thenReturn(expectedResponse);

        // When
        log.info("Test başlıyor - ContractCreatedEvent gönderiliyor: customerId={}, totalAmount={}", customerId,
                totalAmount);
        consumer.contractCreatedEventConsumer().accept(event);

        // Then
        verify(pipeline, times(1)).send(commandCaptor.capture());
        CreateBillCommand capturedCommand = commandCaptor.getValue();

        log.info("Test sonuçları:");
        log.info("1. CreateBillCommand oluşturuldu");
        log.info("2. CustomerId: {}", capturedCommand.getCustomerId());
        log.info("3. TotalAmount: {}", capturedCommand.getTotalAmount());

        assertEquals(customerId, capturedCommand.getCustomerId(), "CustomerId eşleşmiyor");
        assertEquals(totalAmount, capturedCommand.getTotalAmount(), "TotalAmount eşleşmiyor");

        log.info("Test başarıyla tamamlandı!");
    }
}