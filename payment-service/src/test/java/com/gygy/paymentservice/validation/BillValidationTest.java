package com.gygy.paymentservice.validation;

import com.gygy.paymentservice.domain.entity.bill.Bill;
import com.gygy.paymentservice.domain.entity.bill.BillStatus;
import com.gygy.paymentservice.domain.exception.bill.BillAmountException;
import com.gygy.paymentservice.domain.exception.bill.BillValidationException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class BillValidationTest {

    @Test
    void whenBillAmountIsNegative_thenExceptionShouldBeThrown() {
        // given
        Bill bill = new Bill();
        bill.setCustomerId(UUID.randomUUID());
        bill.setTotalAmount(new BigDecimal("100.00"));
        bill.setPaidAmount(BigDecimal.ZERO);
        bill.setDueDate(LocalDate.now().plusDays(30));
        bill.setStatus(BillStatus.PENDING);

        // when & then
        assertThrows(BillAmountException.class, () -> bill.setTotalAmount(new BigDecimal("-100.00")));
    }

    @Test
    void whenBillDueDateIsInPast_thenExceptionShouldBeThrown() {
        // given
        Bill bill = new Bill();
        bill.setCustomerId(UUID.randomUUID());
        bill.setTotalAmount(new BigDecimal("100.00"));
        bill.setPaidAmount(BigDecimal.ZERO);
        bill.setStatus(BillStatus.PENDING);

        // when & then
        assertThrows(BillValidationException.class, () -> bill.setDueDate(LocalDate.now().minusDays(1)));
    }

    @Test
    void whenBillCustomerIdIsNull_thenExceptionShouldBeThrown() {
        // given
        Bill bill = new Bill();
        bill.setTotalAmount(new BigDecimal("100.00"));
        bill.setPaidAmount(BigDecimal.ZERO);
        bill.setDueDate(LocalDate.now().plusDays(30));
        bill.setStatus(BillStatus.PENDING);

        // when & then
        assertThrows(BillValidationException.class, () -> bill.setCustomerId(null));
    }

    @Test
    void whenBillIsValid_thenNoExceptionShouldBeThrown() {
        // given
        Bill bill = new Bill();
        bill.setCustomerId(UUID.randomUUID());
        bill.setTotalAmount(new BigDecimal("100.00"));
        bill.setPaidAmount(BigDecimal.ZERO);
        bill.setDueDate(LocalDate.now().plusDays(30));
        bill.setStatus(BillStatus.PENDING);

        // when & then
        assertDoesNotThrow(() -> bill.applyPayment(new BigDecimal("50.00")));
        assertEquals(new BigDecimal("50.00"), bill.getPaidAmount());
        assertEquals(BillStatus.PARTIALLY_PAID, bill.getStatus());
    }
}