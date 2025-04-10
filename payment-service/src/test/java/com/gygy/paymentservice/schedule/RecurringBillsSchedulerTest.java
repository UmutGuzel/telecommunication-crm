package com.gygy.paymentservice.schedule;

import an.awesome.pipelinr.Command;
import an.awesome.pipelinr.Pipeline;
import com.gygy.paymentservice.application.bill.command.create.CreateBillCommand;
import com.gygy.paymentservice.application.bill.scheduler.RecurringBillsScheduler;
import com.gygy.paymentservice.application.bill.service.PaymentScheduleService;
import com.gygy.paymentservice.domain.entity.bill.PaymentSchedule;
import com.gygy.paymentservice.persistence.bill.PaymentScheduleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecurringBillsSchedulerTest {

    @Mock
    private PaymentScheduleService paymentScheduleService;

    @Mock
    private Pipeline pipeline;

    @Mock
    private PaymentScheduleRepository paymentScheduleRepository;

    private RecurringBillsScheduler recurringBillsScheduler;

    @BeforeEach
    void setUp() {
        // Create the scheduler using constructor injection
        recurringBillsScheduler = new RecurringBillsScheduler(
                paymentScheduleService,
                pipeline,
                paymentScheduleRepository);
    }

    @Test
    void recurringBills_shouldGenerateBillsAndUpdateSchedules_whenSchedulesFound() {
        // Arrange
        LocalDate today = LocalDate.now();
        UUID contractId1 = UUID.randomUUID();
        UUID customerId1 = UUID.randomUUID();
        UUID scheduleId1 = UUID.randomUUID();

        PaymentSchedule schedule1 = new PaymentSchedule();
        schedule1.setId(scheduleId1);
        schedule1.setContractId(contractId1);
        schedule1.setCustomerId(customerId1);
        schedule1.setAmount(BigDecimal.TEN);
        schedule1.setNextBillingDate(today);
        schedule1.setRemainingMonths(2);

        when(paymentScheduleRepository.findByNextBillingDate(today)).thenReturn(Arrays.asList(schedule1));

        // ArgumentCaptor kullanarak türü yakalayın
        ArgumentCaptor<CreateBillCommand> commandCaptor = ArgumentCaptor.forClass(CreateBillCommand.class);
        ArgumentCaptor<PaymentSchedule> scheduleCaptor = ArgumentCaptor.forClass(PaymentSchedule.class);

        // Act
        recurringBillsScheduler.recurringBills();

        // Assert
        // `send` metodunun doğru çağrıldığından emin olun, command parametre tipini belirtin
        verify(pipeline, times(1)).send(commandCaptor.capture());
        CreateBillCommand capturedCommand = commandCaptor.getValue();
        assertEquals(customerId1, capturedCommand.getCustomerId());
        assertEquals(BigDecimal.TEN, capturedCommand.getTotalAmount());

        // PaymentSchedule güncellemesini kontrol et
        verify(paymentScheduleService, times(1)).save(scheduleCaptor.capture());
        PaymentSchedule updatedSchedule = scheduleCaptor.getValue();
        assertEquals(scheduleId1, updatedSchedule.getId());
        assertEquals(1, updatedSchedule.getRemainingMonths());
        assertEquals(today.plusMonths(1), updatedSchedule.getNextBillingDate());
    }

    @Test
    void recurringBills_shouldGenerateLastBillAndNotUpdateNextDate_whenLastPayment() {
        // Arrange
        LocalDate today = LocalDate.now();
        UUID contractId1 = UUID.randomUUID();
        UUID customerId1 = UUID.randomUUID();
        UUID scheduleId1 = UUID.randomUUID();

        PaymentSchedule schedule1 = new PaymentSchedule();
        schedule1.setId(scheduleId1);
        schedule1.setContractId(contractId1);
        schedule1.setCustomerId(customerId1);
        schedule1.setAmount(BigDecimal.TEN);
        schedule1.setNextBillingDate(today);
        schedule1.setRemainingMonths(1); // Son ödeme

        when(paymentScheduleRepository.findByNextBillingDate(today)).thenReturn(Arrays.asList(schedule1));

        // ArgumentCaptor kullanarak türü yakalayın
        ArgumentCaptor<CreateBillCommand> commandCaptor = ArgumentCaptor.forClass(CreateBillCommand.class);
        ArgumentCaptor<PaymentSchedule> scheduleCaptor = ArgumentCaptor.forClass(PaymentSchedule.class);

        // Act
        recurringBillsScheduler.recurringBills();

        // Assert
        // `send` metodunun doğru çağrıldığından emin olun
        verify(pipeline, times(1)).send(commandCaptor.capture());
        CreateBillCommand capturedCommand = commandCaptor.getValue();
        assertEquals(customerId1, capturedCommand.getCustomerId());
        assertEquals(BigDecimal.TEN, capturedCommand.getTotalAmount());

        // PaymentSchedule güncellemesini kontrol et
        verify(paymentScheduleService, times(1)).save(scheduleCaptor.capture());
        PaymentSchedule updatedSchedule = scheduleCaptor.getValue();
        assertEquals(scheduleId1, updatedSchedule.getId());
        assertEquals(0, updatedSchedule.getRemainingMonths());
        // Son ödeme olduğundan, nextBillingDate değişmemeli
        assertEquals(today, updatedSchedule.getNextBillingDate());
    }

    @Test
    void recurringBills_shouldNotGenerateBills_whenNoSchedulesFound() {
        // Arrange
        LocalDate today = LocalDate.now();
        when(paymentScheduleRepository.findByNextBillingDate(today)).thenReturn(Collections.emptyList());

        // Act
        recurringBillsScheduler.recurringBills();

        // Assert
        // `send` metodunun çağrılmaması gerektiğini kontrol et
        verify(pipeline, never()).send(any(Command.class));
        verify(paymentScheduleService, never()).save(any());
    }

    @Test
    void recurringBills_shouldLogError_whenExceptionOccurs() {
        // Arrange
        LocalDate today = LocalDate.now();
        UUID scheduleId1 = UUID.randomUUID();
        PaymentSchedule schedule1 = new PaymentSchedule();
        schedule1.setId(scheduleId1);
        schedule1.setNextBillingDate(today);

        when(paymentScheduleRepository.findByNextBillingDate(today)).thenReturn(Arrays.asList(schedule1));

        // Açıkça Command tipini belirtiyoruz
        doThrow(new RuntimeException("Test Exception")).when(pipeline).send(any(Command.class));

        // Act
        recurringBillsScheduler.recurringBills();

        // Assert
        verify(pipeline).send(any(Command.class));
        verify(paymentScheduleService, never()).save(any());
    }
}