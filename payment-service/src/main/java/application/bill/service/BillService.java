package application.bill.service;

import domain.entity.bill.Bill;
import domain.entity.bill.BillStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BillService {

    void save(Bill bill);

    Bill findById(UUID billId);

    List<Bill> findByCustomerId(UUID customerId);

    List<Bill> findAllBills();

    List<Bill> findPendingBillsDueBefore(LocalDate date);

    void saveAll(List<Bill> bills);

    List<Bill> findByStatus(BillStatus status);

    List<Bill> findBillsByDateRange(LocalDate startDate, LocalDate endDate);

    // List<Bill> findByCustomerIdAndStatus(UUID customerId, BillStatus status);

}
