package com.gygy.paymentservice.integration;

import com.gygy.paymentservice.application.payment.service.PaymentService;
import com.gygy.paymentservice.domain.entity.bill.BillStatus;
import com.gygy.paymentservice.domain.entity.payment.Payment;
import com.gygy.paymentservice.domain.entity.payment.PaymentStatus;
import com.gygy.paymentservice.domain.entity.payment.PaymentMethod;
import com.gygy.paymentservice.domain.entity.bill.Bill;
import com.gygy.paymentservice.domain.exception.payment.PaymentAmountException;
import com.gygy.paymentservice.persistence.payment.PaymentRepository;
import com.gygy.paymentservice.persistence.bill.BillRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class PaymentIntegrationTest {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private BillRepository billRepository;

    @Test
    void whenCreateAndProcessPayment_thenPaymentShouldBeSavedAndProcessed() {
        // given
        UUID customerId = UUID.randomUUID();
        Bill bill = new Bill();
        bill.setCustomerId(customerId);
        bill.setTotalAmount(new BigDecimal("1000.00"));
        bill.setDueDate(LocalDate.now().plusDays(30)); // Zorunlu alan
        bill.setPaidAmount(BigDecimal.ZERO); // Varsayılan 0
        bill.setStatus(BillStatus.PENDING); // Varsayılan durum
        bill.setCreatedAt(LocalDateTime.now()); // Varsayılan oluşturma tarihi
        billRepository.save(bill); // Kaydedilen nesneyi tekrar ata

        Payment payment = new Payment();
        payment.setCustomerId(customerId);
        payment.setPaidAmount(new BigDecimal("500.00"));
        payment.setPaymentMethod(PaymentMethod.CREDIT_CARD);
        payment.setPaymentStatus(PaymentStatus.PENDING);
        payment.setBill(bill);

        // when
        paymentService.save(payment);
        payment.processPayment();
        paymentService.save(payment);

        // then
        Payment savedPayment = paymentRepository.findById(payment.getPaymentId()).orElseThrow();
        assertEquals(PaymentStatus.COMPLETED, savedPayment.getPaymentStatus());
        assertNotNull(savedPayment.getPaymentDate());
        assertEquals(new BigDecimal("500.00"), savedPayment.getPaidAmount());
    }

    @Test
    void whenProcessPaymentWithInvalidAmount_thenPaymentShouldFail() {
        // given
        UUID customerId = UUID.randomUUID();
        Bill bill = new Bill();
        bill.setCustomerId(customerId);
        bill.setTotalAmount(new BigDecimal("1000.00"));
        bill.setDueDate(LocalDate.now().plusDays(30));
        bill.setPaidAmount(BigDecimal.ZERO);
        bill.setStatus(BillStatus.PENDING);
        bill.setCreatedAt(LocalDateTime.now());
        billRepository.save(bill);

        Payment payment = new Payment();
        payment.setCustomerId(customerId);
        payment.setPaidAmount(new BigDecimal("2000.00")); // Bill total'dan büyük
        payment.setPaymentMethod(PaymentMethod.CREDIT_CARD);
        payment.setPaymentStatus(PaymentStatus.PENDING);
        payment.setBill(bill);

        // when - then
        assertThrows(PaymentAmountException.class, payment::processPayment);

        // Ödeme başarısız olduğu için kaydedelim
        paymentService.save(payment);

        Payment savedPayment = paymentRepository.findById(payment.getPaymentId()).orElseThrow();
        assertEquals(PaymentStatus.FAILED, savedPayment.getPaymentStatus());
        assertNotNull(savedPayment.getFailureReason());
    }

    @Test
    void whenGetPaymentsByCustomerId_thenPaymentsShouldBeFound() {
        // given
        UUID customerId = UUID.randomUUID();
        Bill bill = new Bill();
        bill.setCustomerId(customerId);
        bill.setTotalAmount(new BigDecimal("1000.00"));
        bill.setDueDate(LocalDate.now().plusDays(30));
        bill.setPaidAmount(BigDecimal.ZERO);
        bill.setStatus(BillStatus.PENDING);
        bill.setCreatedAt(LocalDateTime.now());
        billRepository.save(bill);

        Payment payment1 = new Payment();
        payment1.setCustomerId(customerId);
        payment1.setPaidAmount(new BigDecimal("500.00"));
        payment1.setPaymentMethod(PaymentMethod.CREDIT_CARD);
        payment1.setPaymentStatus(PaymentStatus.COMPLETED);
        payment1.setBill(bill);
        paymentService.save(payment1);

        Payment payment2 = new Payment();
        payment2.setCustomerId(customerId);
        payment2.setPaidAmount(new BigDecimal("300.00"));
        payment2.setPaymentMethod(PaymentMethod.BANK_TRANSFER);
        payment2.setPaymentStatus(PaymentStatus.PENDING);
        payment2.setBill(bill);
        paymentService.save(payment2);

        // when
        List<Payment> payments = paymentService.findByCustomerId(customerId);

        // then
        assertEquals(2, payments.size());
        assertTrue(payments.stream().anyMatch(p -> p.getPaymentStatus() == PaymentStatus.COMPLETED));
        assertTrue(payments.stream().anyMatch(p -> p.getPaymentStatus() == PaymentStatus.PENDING));
    }
}
