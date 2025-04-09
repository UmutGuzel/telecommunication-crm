package com.gygy.paymentservice.persistence.bill;

import com.gygy.paymentservice.domain.entity.bill.PaymentSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

//TODO: new
public interface PaymentScheduleRepository extends JpaRepository<PaymentSchedule, UUID> {
    List<PaymentSchedule> findByNextBillingDate(LocalDate date);
}
