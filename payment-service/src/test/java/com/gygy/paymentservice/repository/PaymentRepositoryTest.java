package com.gygy.paymentservice.repository;

import com.gygy.paymentservice.domain.entity.bill.Bill;
import com.gygy.paymentservice.domain.entity.bill.BillStatus;
import com.gygy.paymentservice.domain.entity.payment.Payment;
import com.gygy.paymentservice.domain.entity.payment.PaymentMethod;
import com.gygy.paymentservice.domain.entity.payment.PaymentStatus;
import com.gygy.paymentservice.persistence.bill.BillRepository;
import com.gygy.paymentservice.persistence.payment.PaymentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
public class PaymentRepositoryTest {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private BillRepository billRepository;

    @Test
    void whenSavePayment_thenPaymentShouldBeSaved() {
        // given
        UUID customerId = UUID.randomUUID();

        // Create bill
        Bill bill = new Bill();
        bill.setCustomerId(customerId);
        bill.setTotalAmount(new BigDecimal("100.00"));
        bill.setPaidAmount(BigDecimal.ZERO);
        bill.setDueDate(LocalDate.now().plusDays(30));
        bill.setStatus(BillStatus.PENDING);
        bill.setCreatedAt(LocalDateTime.now());
        bill = billRepository.save(bill);

        // Create payment
        Payment payment = new Payment();
        payment.setPaidAmount(new BigDecimal("100.00"));
        payment.setCustomerId(customerId);
        payment.setPaymentDate(LocalDateTime.now());
        payment.setPaymentStatus(PaymentStatus.PENDING);
        payment.setPaymentMethod(PaymentMethod.CREDIT_CARD);
        payment.setBill(bill);

        // when
        Payment saved = paymentRepository.save(payment);

        // then
        assertNotNull(saved.getPaymentId());
        assertEquals(new BigDecimal("100.00"), saved.getPaidAmount());
        assertEquals(customerId, saved.getCustomerId());
        assertEquals(PaymentStatus.PENDING, saved.getPaymentStatus());
        assertEquals(PaymentMethod.CREDIT_CARD, saved.getPaymentMethod());
        assertEquals(bill.getBillId(), saved.getBill().getBillId());
    }

    @Test
    void whenFindByCustomerId_thenPaymentsShouldBeFound() {
        // given
        UUID customerId = UUID.randomUUID();

        // Create bill
        Bill bill = new Bill();
        bill.setCustomerId(customerId);
        bill.setTotalAmount(new BigDecimal("300.00"));
        bill.setPaidAmount(BigDecimal.ZERO);
        bill.setDueDate(LocalDate.now().plusDays(30));
        bill.setStatus(BillStatus.PENDING);
        bill.setCreatedAt(LocalDateTime.now());
        bill = billRepository.save(bill);

        // Create payments
        Payment payment1 = new Payment();
        payment1.setPaidAmount(new BigDecimal("100.00"));
        payment1.setCustomerId(customerId);
        payment1.setPaymentDate(LocalDateTime.now());
        payment1.setPaymentStatus(PaymentStatus.COMPLETED);
        payment1.setPaymentMethod(PaymentMethod.CREDIT_CARD);
        payment1.setBill(bill);

        Payment payment2 = new Payment();
        payment2.setPaidAmount(new BigDecimal("200.00"));
        payment2.setCustomerId(customerId);
        payment2.setPaymentDate(LocalDateTime.now());
        payment2.setPaymentStatus(PaymentStatus.PENDING);
        payment2.setPaymentMethod(PaymentMethod.BANK_TRANSFER);
        payment2.setBill(bill);

        paymentRepository.save(payment1);
        paymentRepository.save(payment2);

        // when
        List<Payment> payments = paymentRepository.findByCustomerId(customerId);

        // then
        assertEquals(2, payments.size());
        assertTrue(payments.stream().anyMatch(p -> p.getPaidAmount().equals(new BigDecimal("100.00"))));
        assertTrue(payments.stream().anyMatch(p -> p.getPaidAmount().equals(new BigDecimal("200.00"))));
    }

    @Test
    void whenFindByPaymentStatus_thenPaymentsShouldBeFound() {
        // given
        UUID customerId1 = UUID.randomUUID();
        UUID customerId2 = UUID.randomUUID();

        // Create bills
        Bill bill1 = new Bill();
        bill1.setCustomerId(customerId1);
        bill1.setTotalAmount(new BigDecimal("100.00"));
        bill1.setPaidAmount(BigDecimal.ZERO);
        bill1.setDueDate(LocalDate.now().plusDays(30));
        bill1.setStatus(BillStatus.PENDING);
        bill1.setCreatedAt(LocalDateTime.now());
        bill1 = billRepository.save(bill1);

        Bill bill2 = new Bill();
        bill2.setCustomerId(customerId2);
        bill2.setTotalAmount(new BigDecimal("200.00"));
        bill2.setPaidAmount(BigDecimal.ZERO);
        bill2.setDueDate(LocalDate.now().plusDays(30));
        bill2.setStatus(BillStatus.PENDING);
        bill2.setCreatedAt(LocalDateTime.now());
        bill2 = billRepository.save(bill2);

        // Create payments
        Payment payment1 = new Payment();
        payment1.setPaidAmount(new BigDecimal("100.00"));
        payment1.setCustomerId(customerId1);
        payment1.setPaymentDate(LocalDateTime.now());
        payment1.setPaymentStatus(PaymentStatus.COMPLETED);
        payment1.setPaymentMethod(PaymentMethod.CREDIT_CARD);
        payment1.setBill(bill1);

        Payment payment2 = new Payment();
        payment2.setPaidAmount(new BigDecimal("200.00"));
        payment2.setCustomerId(customerId2);
        payment2.setPaymentDate(LocalDateTime.now());
        payment2.setPaymentStatus(PaymentStatus.COMPLETED);
        payment2.setPaymentMethod(PaymentMethod.WALLET);
        payment2.setBill(bill2);

        paymentRepository.save(payment1);
        paymentRepository.save(payment2);

        // when
        List<Payment> payments = paymentRepository.findByPaymentStatus(PaymentStatus.COMPLETED);

        // then
        assertEquals(2, payments.size());
        assertTrue(payments.stream().allMatch(p -> p.getPaymentStatus() == PaymentStatus.COMPLETED));
    }
}