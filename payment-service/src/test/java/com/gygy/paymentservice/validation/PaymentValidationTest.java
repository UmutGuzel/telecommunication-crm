package com.gygy.paymentservice.validation;

import com.gygy.paymentservice.domain.entity.payment.Payment;
import com.gygy.paymentservice.domain.entity.bill.Bill;
import com.gygy.paymentservice.domain.entity.bill.BillStatus;
import com.gygy.paymentservice.domain.entity.payment.PaymentMethod;
import com.gygy.paymentservice.domain.entity.payment.PaymentStatus;
import com.gygy.paymentservice.domain.exception.payment.PaymentValidationException;
import com.gygy.paymentservice.domain.exception.bill.BillAmountException;
import com.gygy.paymentservice.domain.exception.payment.PaymentAmountException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class PaymentValidationTest {

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

        // Create payment
        payment = new Payment();
        payment.setCustomerId(customerId);
        payment.setPaidAmount(new BigDecimal("100.00"));
        payment.setPaymentMethod(PaymentMethod.CREDIT_CARD);
        payment.setPaymentStatus(PaymentStatus.PENDING);
        payment.setBill(bill);
    }

    @Test
    void whenProcessValidPayment_thenPaymentShouldBeCompleted() {
        // when & then
        assertDoesNotThrow(() -> payment.processPayment());
        assertTrue(payment.isCompleted());
        assertEquals(PaymentStatus.COMPLETED, payment.getPaymentStatus());
        assertNotNull(payment.getPaymentDate());
    }

    @Test
    void whenProcessPaymentWithNullBill_thenExceptionShouldBeThrown() {
        // given
        Payment invalidPayment = new Payment();
        invalidPayment.setCustomerId(customerId);
        invalidPayment.setPaidAmount(new BigDecimal("100.00"));
        invalidPayment.setPaymentMethod(PaymentMethod.CREDIT_CARD);
        invalidPayment.setPaymentStatus(PaymentStatus.PENDING);
        // bill is null

        // when
        PaymentValidationException exception = assertThrows(PaymentValidationException.class,
                () -> invalidPayment.processPayment());

        // then
        assertEquals("Payment must be linked to a bill", exception.getMessage());
        assertEquals(PaymentStatus.FAILED, invalidPayment.getPaymentStatus());
        assertTrue(invalidPayment.isFailed());
        assertTrue(invalidPayment.getFailureReason().contains("Payment must be linked to a bill"));
    }

    @Test
    void whenProcessPaymentWithNegativeAmount_thenExceptionShouldBeThrown() {
        // given
        Payment invalidPayment = new Payment();
        invalidPayment.setCustomerId(customerId);
        invalidPayment.setPaymentMethod(PaymentMethod.CREDIT_CARD);
        invalidPayment.setPaymentStatus(PaymentStatus.PENDING);
        invalidPayment.setBill(bill);

        // when & then
        assertThrows(PaymentAmountException.class,
                () -> invalidPayment.setPaidAmount(new BigDecimal("-100.00")));
    }

    @Test
    void whenProcessPaymentWithZeroAmount_thenExceptionShouldBeThrown() {
        // given
        Payment invalidPayment = new Payment();
        invalidPayment.setCustomerId(customerId);
        invalidPayment.setPaymentMethod(PaymentMethod.CREDIT_CARD);
        invalidPayment.setPaymentStatus(PaymentStatus.PENDING);
        invalidPayment.setBill(bill);

        // when & then
        assertThrows(PaymentAmountException.class,
                () -> invalidPayment.setPaidAmount(BigDecimal.ZERO));
    }

    @Test
    void whenProcessPaymentWithAmountExceedingBillAmount_thenExceptionShouldBeThrown() {
        // given
        Payment invalidPayment = new Payment();
        invalidPayment.setCustomerId(customerId);
        invalidPayment.setPaymentMethod(PaymentMethod.CREDIT_CARD);
        invalidPayment.setPaymentStatus(PaymentStatus.PENDING);
        invalidPayment.setBill(bill);
        invalidPayment.setPaidAmount(new BigDecimal("2000.00")); // Bill amount'dan büyük

        // when
        PaymentAmountException exception = assertThrows(PaymentAmountException.class,
                invalidPayment::processPayment);

        // then
        assertEquals("The payment amount cannot exceed the bill total", exception.getMessage());
        assertEquals(PaymentStatus.FAILED, invalidPayment.getPaymentStatus());
        assertTrue(invalidPayment.isFailed());
        assertTrue(invalidPayment.getFailureReason().contains("The payment amount cannot exceed the bill total"));
    }
}