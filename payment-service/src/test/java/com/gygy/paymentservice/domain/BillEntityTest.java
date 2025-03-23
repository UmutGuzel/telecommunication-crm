package com.gygy.paymentservice.domain;

import com.gygy.paymentservice.domain.entity.bill.Bill;
import com.gygy.paymentservice.domain.entity.bill.BillStatus;
import com.gygy.paymentservice.domain.exception.bill.BillAmountException;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class BillEntityTest {
    @Test
    void whenCreateBill_thenBillShouldBeCreated() {
        // given
        UUID billId = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();
        BigDecimal amount = new BigDecimal("100.00");
        LocalDate dueDate = LocalDate.now().plusDays(30);
        LocalDateTime now = LocalDateTime.now();

        // when
        Bill bill = new Bill();
        bill.setBillId(billId);
        bill.setCustomerId(customerId);
        bill.setTotalAmount(amount);
        bill.setPaidAmount(BigDecimal.ZERO);
        bill.setDueDate(dueDate);
        bill.setStatus(BillStatus.PENDING);
        bill.setCreatedAt(now);
        bill.setUpdatedAt(now);

        // then
        assertEquals(billId, bill.getBillId());
        assertEquals(customerId, bill.getCustomerId());
        assertEquals(amount, bill.getTotalAmount());
        assertEquals(dueDate, bill.getDueDate());
        assertEquals(BillStatus.PENDING, bill.getStatus());
        assertEquals(now, bill.getCreatedAt());
        assertEquals(now, bill.getUpdatedAt());
    }

    @Test
    void whenApplyPayment_thenAmountShouldBeUpdated() {
        // given
        Bill bill = new Bill();
        bill.setTotalAmount(new BigDecimal("100.00"));
        bill.setPaidAmount(BigDecimal.ZERO);
        BigDecimal paymentAmount = new BigDecimal("50.00");

        // when
        bill.applyPayment(paymentAmount);

        // then
        assertEquals(new BigDecimal("50.00"), bill.getPaidAmount());
    }

    @Test
    void whenApplyPaymentExceedingAmount_thenExceptionShouldBeThrown() {
        // given
        Bill bill = new Bill();
        bill.setTotalAmount(new BigDecimal("100.00"));
        bill.setPaidAmount(BigDecimal.ZERO);
        BigDecimal paymentAmount = new BigDecimal("150.00");

        // when & then
        assertThrows(BillAmountException.class, () -> bill.applyPayment(paymentAmount));
    }

    @Test
    void whenBillIsFullyPaid_thenStatusShouldBeUpdated() {
        // given
        Bill bill = new Bill();
        bill.setTotalAmount(new BigDecimal("100.00"));
        bill.setPaidAmount(BigDecimal.ZERO);
        bill.setStatus(BillStatus.PENDING);

        // when
        bill.applyPayment(new BigDecimal("100.00"));

        // then
        assertEquals(BillStatus.PAID, bill.getStatus());
    }
}
