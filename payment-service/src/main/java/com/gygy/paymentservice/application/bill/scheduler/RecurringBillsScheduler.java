package com.gygy.paymentservice.application.bill.scheduler;

import an.awesome.pipelinr.Pipeline;
import com.gygy.paymentservice.application.bill.command.create.CreateBillCommand;
import com.gygy.paymentservice.application.bill.service.PaymentScheduleService;
import com.gygy.paymentservice.domain.entity.bill.PaymentSchedule;
import com.gygy.paymentservice.persistence.bill.PaymentScheduleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class RecurringBillsScheduler {
    private final PaymentScheduleService paymentScheduleService;
    private final Pipeline pipeline;
    private final PaymentScheduleRepository paymentScheduleRepository;

    @Scheduled(cron = "0 0 1 * * *") //her gece 1 de
    public void recurringBills() {
        log.info("Starting recurring bills");
        List<PaymentSchedule> schedules = paymentScheduleRepository.findByNextBillingDate(LocalDate.now());
        log.info("Found {} schedules for billing today", schedules.size());

        for (PaymentSchedule schedule : schedules) {
            try{
                //yeni fatura
                CreateBillCommand command = new CreateBillCommand();
                command.setCustomerId(schedule.getCustomerId());
                command.setTotalAmount(schedule.getAmount());
                command.setContractId(schedule.getContractId());
                pipeline.send(command);

                //güncelleriz
                schedule.setRemainingMonths(schedule.getRemainingMonths() - 1);
                schedule.setNextBillingDate(schedule.getNextBillingDate().plusMonths(1));
                paymentScheduleService.save(schedule);

                log.info("Generated bill for contract: {},  remaining months: {}",
                        schedule.getContractId(), schedule.getRemainingMonths());
            }catch (Exception e){
                log.error("Error generating bill for schedule {}: {}",
                        schedule.getId(), e.getMessage(), e);
            }
        }
    }

}
