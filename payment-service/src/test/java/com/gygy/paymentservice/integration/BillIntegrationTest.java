package com.gygy.paymentservice.integration;

import com.gygy.paymentservice.application.bill.service.BillService;
import com.gygy.paymentservice.domain.entity.bill.Bill;
import com.gygy.paymentservice.domain.entity.bill.BillStatus;
import com.gygy.paymentservice.domain.entity.payment.Payment;
import com.gygy.paymentservice.domain.entity.payment.PaymentStatus;
import com.gygy.paymentservice.domain.entity.payment.PaymentMethod;
import com.gygy.paymentservice.domain.exception.bill.BillAmountException;
import com.gygy.paymentservice.persistence.bill.BillRepository;
import com.gygy.paymentservice.persistence.payment.PaymentRepository;
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


@Transactional
@SpringBootTest
@ActiveProfiles("test")
class BillIntegrationTest {

    @Autowired
    private BillService billService;

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Test
    void whenCreateBill_thenBillShouldBeSaved() {
        // given
        UUID customerId = UUID.randomUUID();
        final Bill bill = new Bill();
        bill.setCustomerId(customerId);
        bill.setTotalAmount(new BigDecimal("1000.00"));
        bill.setPaidAmount(BigDecimal.ZERO);  // ✅ Null hatasını önlemek için ekledik
        bill.setDueDate(LocalDate.now().plusDays(30));
        bill.setStatus(BillStatus.PENDING);  // ✅ Null hatasını önlemek için ekledik

        // when
        billRepository.save(bill);

        // then
        Bill savedBill = billRepository.findById(bill.getBillId()).orElseThrow();
        assertEquals(customerId, savedBill.getCustomerId());
        assertEquals(new BigDecimal("1000.00"), savedBill.getTotalAmount());
        assertEquals(BigDecimal.ZERO, savedBill.getPaidAmount());  // ✅ Artık null olmayacak
        assertEquals(BillStatus.PENDING, savedBill.getStatus());  // ✅ Artık null olmayacak
    }


    @Test
    void whenApplyPayment_thenBillStatusShouldBeUpdated() {
        // given
        UUID customerId = UUID.randomUUID();
        Bill bill = new Bill();
        bill.setCustomerId(customerId);
        bill.setTotalAmount(new BigDecimal("1000.00"));
        bill.setPaidAmount(BigDecimal.ZERO);  // ✅ NullPointer'ı önlemek için ekledik
        bill.setDueDate(LocalDate.now().plusDays(30));
        bill.setStatus(BillStatus.PENDING);  // ✅ NullPointer'ı önlemek için ekledik
        bill = billRepository.save(bill);

        Payment payment = new Payment();
        payment.setCustomerId(customerId);
        payment.setPaidAmount(new BigDecimal("500.00"));
        payment.setPaymentMethod(PaymentMethod.CREDIT_CARD);
        payment.setPaymentStatus(PaymentStatus.COMPLETED);
        payment.setBill(bill);
        paymentRepository.save(payment);

        // when
        bill.applyPayment(new BigDecimal("500.00"));
        billRepository.save(bill);

        // then
        Bill updatedBill = billRepository.findById(bill.getBillId()).orElseThrow();
        assertEquals(new BigDecimal("500.00"), updatedBill.getPaidAmount());  // ✅ Null yerine artık 500.00
        assertEquals(BillStatus.PARTIALLY_PAID, updatedBill.getStatus());  // ✅ Null yerine doğru status
    }


    @Test
    void whenApplyPaymentExceedingAmount_thenExceptionShouldBeThrown() {
        // given
        UUID customerId = UUID.randomUUID();
        final Bill bill = new Bill();
        bill.setCustomerId(customerId);
        bill.setTotalAmount(new BigDecimal("1000.00"));
        bill.setPaidAmount(BigDecimal.ZERO);  // ✅ NullPointerException'ı önlemek için ekledik
        bill.setDueDate(LocalDate.now().plusDays(30));
        bill.setStatus(BillStatus.PENDING);  // ✅ NullPointerException'ı önlemek için ekledik
        billRepository.save(bill);

        // when & then
        assertThrows(BillAmountException.class, () -> bill.applyPayment(new BigDecimal("2000.00")));
    }


    @Test
    void whenGetBillsByCustomerId_thenBillsShouldBeFound() {
        // given
        UUID customerId = UUID.randomUUID();
        Bill bill = new Bill();
        bill.setCustomerId(customerId);
        bill.setTotalAmount(new BigDecimal("1000.00"));
        bill.setPaidAmount(BigDecimal.ZERO);  // ✅ Null hatasını önlemek için ekledik
        bill.setDueDate(LocalDate.now().plusDays(30));
        bill.setStatus(BillStatus.PENDING);   // ✅ Null hatasını önlemek için ekledik
        bill.setCreatedAt(LocalDateTime.now());
        billRepository.save(bill);

        // when
        List<Bill> bills = billRepository.findByCustomerId(customerId);

        // then
        assertFalse(bills.isEmpty());
        assertEquals(1, bills.size());
        assertEquals(customerId, bills.get(0).getCustomerId());
        assertEquals(BigDecimal.ZERO, bills.get(0).getPaidAmount()); // ✅ Artık null olmayacak
        assertEquals(BillStatus.PENDING, bills.get(0).getStatus()); // ✅ Artık null olmayacak
        assertNotNull(bills.get(0).getCreatedAt());
    }

}
