package com.gygy.paymentservice.service;

import com.gygy.paymentservice.application.bill.service.impl.BillServiceImpl;
import com.gygy.paymentservice.domain.entity.bill.Bill;
import com.gygy.paymentservice.domain.entity.bill.BillStatus;
import com.gygy.paymentservice.persistence.bill.BillRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class BillServiceTest {

    @Mock
    private BillRepository billRepository;

    @InjectMocks
    private BillServiceImpl billService;

    @Test
    void whenCreateBill_thenBillShouldBeSaved() {
        // given
        UUID customerId = UUID.randomUUID();
        BigDecimal amount = new BigDecimal("100.00");

        // when
        Bill bill = createBill(customerId, amount); // createBill metodunu kullanıyoruz

        when(billRepository.save(any(Bill.class))).thenReturn(bill);  // billService yerine billRepository
        billService.save(bill);

        // then
        verify(billRepository).save(any(Bill.class));  // billService yerine billRepository
        assertEquals(customerId, bill.getCustomerId());
        assertEquals(amount, bill.getTotalAmount());
        assertEquals(LocalDate.now().plusDays(30), bill.getDueDate()); // createBill metodunun set ettiği değer
        assertEquals(BillStatus.PENDING, bill.getStatus());
    }

    @Test
    void whenGetBillsByCustomerId_thenBillsShouldBeReturned() {
        // given
        UUID customerId = UUID.randomUUID();
        List<Bill> expectedBills = Arrays.asList(
                createBill(customerId, new BigDecimal("100.00")),
                createBill(customerId, new BigDecimal("200.00")));
        when(billRepository.findByCustomerId(customerId)).thenReturn(expectedBills);

        // when
        List<Bill> actualBills = billService.findByCustomerId(customerId);

        // then
        assertEquals(expectedBills, actualBills);
    }

    @Test
    void whenGetUnpaidBills_thenBillsShouldBeFound() {
        // given
        List<Bill> expectedBills = Arrays.asList(
                createBill(UUID.randomUUID(), new BigDecimal("100.00")),
                createBill(UUID.randomUUID(), new BigDecimal("200.00")));

        when(billRepository.findByStatusIn(Arrays.asList(BillStatus.PENDING, BillStatus.PARTIALLY_PAID)))
                .thenReturn(expectedBills);

        // when
        List<Bill> actualBills = billService.findUnpaidBills();

        // then
        assertEquals(expectedBills, actualBills);
        verify(billRepository).findByStatusIn(Arrays.asList(BillStatus.PENDING, BillStatus.PARTIALLY_PAID));
    }

    private Bill createBill(UUID customerId, BigDecimal amount) {
        Bill bill = new Bill();
        bill.setCustomerId(customerId);
        bill.setTotalAmount(amount);
        bill.setDueDate(LocalDate.now().plusDays(30));
        bill.setStatus(BillStatus.PENDING);
        return bill;
    }
}