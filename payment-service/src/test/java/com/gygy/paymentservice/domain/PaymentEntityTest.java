package com.gygy.paymentservice.domain;

import com.gygy.paymentservice.domain.entity.bill.Bill;
import com.gygy.paymentservice.domain.entity.bill.BillStatus;
import com.gygy.paymentservice.domain.entity.payment.Payment;
import com.gygy.paymentservice.domain.entity.payment.PaymentMethod;
import com.gygy.paymentservice.domain.entity.payment.PaymentStatus;
import com.gygy.paymentservice.domain.exception.payment.PaymentValidationException;
import com.gygy.paymentservice.domain.exception.payment.PaymentAmountException;
import com.gygy.paymentservice.domain.exception.bill.BillAmountException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class PaymentEntityTest {

    private Payment payment;
    private Bill bill;
    private UUID customerId;

    @BeforeEach
    void setUp() {
        customerId = UUID.randomUUID();

        // Create bill
        bill = new Bill();
        bill.setBillId(UUID.randomUUID());
        bill.setCustomerId(customerId);
        bill.setTotalAmount(new BigDecimal("1000.00"));
        bill.setPaidAmount(BigDecimal.ZERO);
        bill.setDueDate(LocalDateTime.now().plusDays(30).toLocalDate());
        bill.setStatus(BillStatus.PENDING);
        bill.setCreatedAt(LocalDateTime.now());
        bill.setUpdatedAt(LocalDateTime.now());

        // Create payment
        payment = new Payment();
        payment.setPaymentId(UUID.randomUUID());
        payment.setCustomerId(customerId);
        payment.setPaidAmount(new BigDecimal("100.00"));
        payment.setPaymentMethod(PaymentMethod.CREDIT_CARD);
        payment.setPaymentStatus(PaymentStatus.PENDING);
        payment.setBill(bill);
        payment.setCreatedAt(LocalDateTime.now());
        payment.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    void whenCreatePaymentWithValidData_thenPaymentShouldBeCreated() {
        assertNotNull(payment.getPaymentId());
        assertEquals(customerId, payment.getCustomerId());
        assertEquals(new BigDecimal("100.00"), payment.getPaidAmount());
        assertEquals(PaymentMethod.CREDIT_CARD, payment.getPaymentMethod());
        assertEquals(PaymentStatus.PENDING, payment.getPaymentStatus());
        assertEquals(bill, payment.getBill());
        assertNotNull(payment.getCreatedAt());
        assertNotNull(payment.getUpdatedAt());
    }

    @Test
    void whenSetNegativeAmount_thenExceptionShouldBeThrown() {
        assertThrows(PaymentAmountException.class, () -> {
            payment.setPaidAmount(new BigDecimal("-100.00"));
        });
    }

    @Test
    void whenSetZeroAmount_thenExceptionShouldBeThrown() {
        assertThrows(PaymentAmountException.class, () -> {
            payment.setPaidAmount(BigDecimal.ZERO);
        });
    }

    @Test
    void whenSetNullCustomerId_thenExceptionShouldBeThrown() {
        assertThrows(PaymentValidationException.class, () -> {
            payment.setCustomerId(null);
        });
    }

    @Test
    void whenSetNullBill_thenExceptionShouldBeThrown() {
        assertThrows(PaymentValidationException.class, () -> {
            payment.setBill(null);
        });
    }

    @Test
    void whenSetNullPaymentMethod_thenExceptionShouldBeThrown() {
        assertThrows(PaymentValidationException.class, () -> {
            payment.setPaymentMethod(null);
        });
    }

    @Test
    void whenSetNullPaymentStatus_thenExceptionShouldBeThrown() {
        assertThrows(PaymentValidationException.class, () -> {
            payment.setPaymentStatus(null);
        });
    }

    @Test
    void whenSetFuturePaymentDate_thenExceptionShouldBeThrown() {
        assertThrows(PaymentValidationException.class, () -> {
            payment.setPaymentDate(LocalDateTime.now().plusDays(1));
        });
    }

    @Test
    void whenProcessPayment_thenStatusShouldBeUpdated() {
        LocalDateTime beforeTest = LocalDateTime.now();
        payment.processPayment();
        LocalDateTime afterTest = LocalDateTime.now();

        assertEquals(PaymentStatus.COMPLETED, payment.getPaymentStatus());
        assertNotNull(payment.getPaymentDate());

        // Payment date şu aralıkta olmalı: beforeTest <= paymentDate <= afterTest
        assertFalse(payment.getPaymentDate().isBefore(beforeTest));
        assertFalse(payment.getPaymentDate().isAfter(afterTest));
    }

    @Test
    void whenProcessPaymentWithNullBill_thenExceptionShouldBeThrown() {
        // given
        Payment invalidPayment = new Payment();
        invalidPayment.setPaymentId(UUID.randomUUID());
        invalidPayment.setCustomerId(customerId);
        invalidPayment.setPaidAmount(new BigDecimal("100.00"));
        invalidPayment.setPaymentMethod(PaymentMethod.CREDIT_CARD);
        invalidPayment.setPaymentStatus(PaymentStatus.PENDING);
        invalidPayment.setCreatedAt(LocalDateTime.now());
        invalidPayment.setUpdatedAt(LocalDateTime.now());
        // bill is null

        // when
        PaymentValidationException exception = assertThrows(PaymentValidationException.class, () -> {
            invalidPayment.processPayment();
        });

        // then
        assertEquals("Payment must be linked to a bill", exception.getMessage());
        assertEquals(PaymentStatus.FAILED, invalidPayment.getPaymentStatus());
        assertTrue(invalidPayment.getFailureReason().contains("Payment must be linked to a bill"));
    }

    @Test
    void whenProcessPaymentWithInvalidAmount_thenExceptionShouldBeThrown() {
        payment.setPaidAmount(new BigDecimal("2000.00")); // Bill amount'dan büyük
        PaymentAmountException exception = assertThrows(PaymentAmountException.class, () -> {
            payment.processPayment();
        });
        assertEquals("The payment amount cannot exceed the bill total", exception.getMessage());
        assertEquals(PaymentStatus.FAILED, payment.getPaymentStatus());
        assertTrue(payment.getFailureReason().contains("The payment amount cannot exceed the bill total"));
    }

    @Test
    void whenPaymentIsCompleted_thenIsCompletedShouldReturnTrue() {
        payment.setPaymentStatus(PaymentStatus.COMPLETED);
        assertTrue(payment.isCompleted());
    }

    @Test
    void whenPaymentIsFailed_thenIsFailedShouldReturnTrue() {
        payment.setPaymentStatus(PaymentStatus.FAILED);
        assertTrue(payment.isFailed());
    }
}