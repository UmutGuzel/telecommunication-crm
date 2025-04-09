package com.gygy.paymentservice.application.bill.service.impl;

import com.gygy.paymentservice.application.bill.service.PaymentScheduleService;
import com.gygy.paymentservice.domain.entity.bill.PaymentSchedule;
import com.gygy.paymentservice.persistence.bill.PaymentScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;


//TODO:new
@Service
@RequiredArgsConstructor
public class PaymentScheduleServiceImpl implements PaymentScheduleService {

    private final PaymentScheduleRepository paymentScheduleRepository;

    @Override
    public void save(PaymentSchedule schedule) {
        paymentScheduleRepository.save(schedule);
    }

    @Override
    public void createPaymentSchedule(UUID contractId, UUID customerId, BigDecimal amount, int durationInMonths, LocalDate startDate) {
        PaymentSchedule paymentSchedule = new PaymentSchedule();
        paymentSchedule.setContractId(contractId);
        paymentSchedule.setCustomerId(customerId);
        paymentSchedule.setAmount(amount);
        paymentSchedule.setNextBillingDate(startDate.plusMonths(1));
        paymentSchedule.setRemainingMonths(durationInMonths-1); //toplam süreden azaltır
        save(paymentSchedule);
    }
}
