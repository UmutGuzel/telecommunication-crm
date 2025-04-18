package com.gygy.paymentservice.application.bill.scheduler;

import com.gygy.common.constants.KafkaTopics;
import com.gygy.common.events.paymentservice.bill.BillOverdueEvent;
import com.gygy.common.kafka.producer.EventProducer;
import com.gygy.paymentservice.application.bill.service.BillService;
import com.gygy.paymentservice.domain.entity.bill.Bill;
import com.gygy.paymentservice.domain.entity.bill.BillStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class BillStatusUpdateScheduler {
    private final BillService billService;
    private final EventProducer eventProducer;

    @Scheduled(cron = "0 */5 * * * *") // Her 5 dakikada bir çalışır. TODO: test için her 5dk yaptım, 0 0 1 * * *  yapılacak.
    @Transactional
    public void updateOverdueBills() {
        try {
            List<Bill> pendingBills = billService.findPendingBillsDueBefore(LocalDate.now());

            if (pendingBills.isEmpty()) {
                return;
            }

            for (Bill bill : pendingBills) {
                try {
                    bill.updateStatus();
                    bill.setUpdatedAt(LocalDateTime.now());
                    BillOverdueEvent event = new BillOverdueEvent();
                    event.setBillId(bill.getBillId());
                    event.setAmount(bill.getTotalAmount());
                    event.setDueDate(bill.getDueDate());

                    eventProducer.sendEvent(KafkaTopics.BILL_OVERDUE, event);
                } catch (Exception e) {
                    // Fatura durumu güncellenirken hata oluştu, diğer faturalarla devam eder
                    continue;
                }
            }

            billService.saveAll(pendingBills);

            // TODO: Kafka notification service'e gönderilecek


        } catch (Exception e) {
            // Genel hata durumunda işlem başarısız oldu, bir sonraki çalışmada tekrar denenecek
        }
    }

    @Scheduled(cron = "0 */5 * * * *") // Her 5 dakikada bir çalışır
    @Transactional
    public void updatePartiallyPaidBills() {
        try {
            List<Bill> partiallyPaidBills = billService.findByStatus(BillStatus.PARTIALLY_PAID);

            if (partiallyPaidBills.isEmpty()) {
                return;
            }

            for (Bill bill : partiallyPaidBills) {
                try {
                    bill.updateStatus();
                    bill.setUpdatedAt(LocalDateTime.now());

                } catch (Exception e) {
                    continue;
                }
            }

            billService.saveAll(partiallyPaidBills);

        } catch (Exception e) {

        }
    }
}
