package com.gygy.paymentservice.application.bill.service;

import com.gygy.paymentservice.domain.entity.bill.PaymentSchedule;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

//TODO:new
public interface PaymentScheduleService {

    void save(PaymentSchedule paymentSchedule);

    void createPaymentSchedule(UUID contractId, UUID customerId, BigDecimal amount,
                               int durationInMonths, LocalDate startDate);
}
