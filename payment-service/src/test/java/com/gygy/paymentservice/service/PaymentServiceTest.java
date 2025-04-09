package com.gygy.paymentservice.service;
import com.gygy.paymentservice.application.payment.service.impl.PaymentServiceImpl;
import com.gygy.paymentservice.domain.entity.bill.Bill;
import com.gygy.paymentservice.domain.entity.bill.BillStatus;
import com.gygy.paymentservice.domain.entity.payment.Payment;
import com.gygy.paymentservice.domain.entity.payment.PaymentStatus;
import com.gygy.paymentservice.persistence.payment.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    private Payment testPayment;
    private UUID testPaymentId;
    private UUID testCustomerId;

    @BeforeEach
    void setUp() {
        testPaymentId = UUID.randomUUID();
        testCustomerId = UUID.randomUUID();

        Bill testBill = new Bill();
        testBill.setBillId(UUID.randomUUID());
        testBill.setCustomerId(testCustomerId);
        testBill.setTotalAmount(new BigDecimal("100.00"));
        testBill.setPaidAmount(BigDecimal.ZERO);
        testBill.setDueDate(LocalDate.now().plusDays(30));
        testBill.setStatus(BillStatus.PENDING);

        testPayment = new Payment();
        testPayment.setPaymentId(testPaymentId);
        testPayment.setPaidAmount(new BigDecimal("100.00"));
        testPayment.setPaymentDate(LocalDateTime.now());
        testPayment.setPaymentStatus(PaymentStatus.COMPLETED);
        testPayment.setCustomerId(testCustomerId);
        testPayment.setBill(testBill);
    }

    @Test
    void whenFindById_thenPaymentShouldBeFound() {
        // given
        when(paymentRepository.findById(testPaymentId)).thenReturn(Optional.of(testPayment));

        // when
        Payment found = paymentService.findById(testPaymentId);

        // then
        assertNotNull(found);
        assertEquals(testPaymentId, found.getPaymentId());
        assertEquals(testPayment.getPaidAmount(), found.getPaidAmount());
        assertEquals(testPayment.getPaymentStatus(), found.getPaymentStatus());
        verify(paymentRepository).findById(testPaymentId);
    }

    @Test
    void whenSavePayment_thenPaymentShouldBeSaved() {
        // given
        when(paymentRepository.save(any(Payment.class))).thenReturn(testPayment);

        // when
        paymentService.save(testPayment);

        // then
        verify(paymentRepository).save(testPayment);
    }

    @Test
    void whenProcessPayment_thenPaymentShouldBeProcessed() {
        // given
        when(paymentRepository.save(any(Payment.class))).thenReturn(testPayment);

        // when
        testPayment.processPayment(); // Entity'den direkt çağırıyoruz
        paymentService.save(testPayment); // İşlenmiş payment'ı kaydediyoruz

        // then
        assertEquals(PaymentStatus.COMPLETED, testPayment.getPaymentStatus());
        verify(paymentRepository).save(testPayment);
    }
}