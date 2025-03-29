package com.gygy.paymentservice.repository;

import com.gygy.paymentservice.domain.entity.bill.Bill;
import com.gygy.paymentservice.domain.entity.bill.BillStatus;
import com.gygy.paymentservice.persistence.bill.BillRepository;
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
class BillRepositoryTest {

    @Autowired
    private BillRepository billRepository;

    @Test
    void whenSaveBill_thenBillShouldBeSaved() {
        // given
        UUID customerId = UUID.randomUUID();
        Bill bill = new Bill();
        bill.setCustomerId(customerId);
        bill.setTotalAmount(new BigDecimal("100.00"));
        bill.setPaidAmount(BigDecimal.ZERO);
        bill.setDueDate(LocalDate.now().plusDays(30));
        bill.setStatus(BillStatus.PENDING);
        bill.setCreatedAt(LocalDateTime.now());

        // when
        Bill saved = billRepository.save(bill);

        // then
        assertNotNull(saved.getBillId());
        assertEquals(customerId, saved.getCustomerId());
        assertEquals(new BigDecimal("100.00"), saved.getTotalAmount());
        assertEquals(BigDecimal.ZERO, saved.getPaidAmount());
        assertEquals(BillStatus.PENDING, saved.getStatus());
    }

    @Test
    void whenFindByCustomerId_thenBillsShouldBeFound() {
        // given
        UUID customerId = UUID.randomUUID();
        Bill bill1 = createBill(customerId, new BigDecimal("100.00"));
        Bill bill2 = createBill(customerId, new BigDecimal("200.00"));

        billRepository.save(bill1);
        billRepository.save(bill2);

        // when
        List<Bill> bills = billRepository.findByCustomerId(customerId);

        // then
        assertEquals(2, bills.size());
        assertTrue(bills.stream().allMatch(b -> b.getCustomerId().equals(customerId)));
    }

    @Test
    void whenFindByBillStatus_thenBillsShouldBeFound() {
        // given
        Bill bill1 = createBill(UUID.randomUUID(), new BigDecimal("100.00"));
        Bill bill2 = createBill(UUID.randomUUID(), new BigDecimal("200.00"));

        billRepository.save(bill1);
        billRepository.save(bill2);

        // when
        List<Bill> bills = billRepository.findByStatus(BillStatus.PENDING);

        // then
        assertEquals(2, bills.size());
        assertTrue(bills.stream().allMatch(b -> b.getStatus() == BillStatus.PENDING));
    }

    @Test
    void whenSoftDeleteBill_thenBillShouldNotBeFound() {
        // given
        Bill bill = createBill(UUID.randomUUID(), new BigDecimal("100.00"));
        bill = billRepository.save(bill);

        // when
        bill.setDeleted(true);
        billRepository.save(bill);

        // then
        List<Bill> bills = billRepository.findByCustomerId(bill.getCustomerId());
        assertTrue(bills.isEmpty());
    }

    private Bill createBill(UUID customerId, BigDecimal amount) {
        Bill bill = new Bill();
        bill.setCustomerId(customerId);
        bill.setTotalAmount(amount);
        bill.setPaidAmount(BigDecimal.ZERO);
        bill.setDueDate(LocalDate.now().plusDays(30));
        bill.setStatus(BillStatus.PENDING);
        bill.setCreatedAt(LocalDateTime.now());
        return bill;
    }
}