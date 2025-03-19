package application.bill.scheduler;

import application.bill.service.BillService;
import domain.entity.bill.Bill;
import domain.entity.bill.BillStatus;
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
