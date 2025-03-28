package com.gygy.paymentservice.persistence.bill;

import com.gygy.paymentservice.domain.entity.bill.Bill;
import com.gygy.paymentservice.domain.entity.bill.BillStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface BillRepository extends JpaRepository<Bill, UUID> {

    List<Bill> findByCustomerId(UUID customerId);

    List<Bill> findByStatus(BillStatus status);

    List<Bill> findByDueDateBeforeAndStatus(LocalDate date, BillStatus status);

    List<Bill> findByDueDateBetween(LocalDate startDate, LocalDate endDate);

    List<Bill> findByStatusIn(List<BillStatus> statuses);

    List<Bill> findByCustomerIdAndStatus(UUID customerId, BillStatus status);

}
