package com.gygy.paymentservice.application.bill.command.create;

import an.awesome.pipelinr.Command;
import com.gygy.common.constants.KafkaTopics;
import com.gygy.common.events.paymentservice.bill.BillCreatedEvent;
import com.gygy.common.kafka.producer.EventProducer;
import com.gygy.paymentservice.application.bill.command.create.dto.CreatedBillResponse;
import com.gygy.paymentservice.application.bill.service.BillService;
import com.gygy.paymentservice.domain.entity.bill.Bill;
import com.gygy.paymentservice.domain.entity.bill.BillStatus;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateBillCommand implements Command<CreatedBillResponse> {

    private UUID customerId;

    @NotNull(message = "Total amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    private BigDecimal totalAmount;

    //dışarıdaki topicten geliyorlar

    @Slf4j
    @Component
    @RequiredArgsConstructor
    public static class CreateBillCommandHandler implements Command.Handler<CreateBillCommand, CreatedBillResponse> {
        private final BillService billService;
        private final EventProducer eventProducer;

        @Override
        @Transactional
        public CreatedBillResponse handle(CreateBillCommand createBillCommand) {
            Bill bill = new Bill();
            bill.setCustomerId(createBillCommand.getCustomerId());
            bill.setTotalAmount(createBillCommand.getTotalAmount());
            bill.setDueDate(LocalDate.now().plusDays(30));
            bill.setStatus(BillStatus.PENDING);
            bill.setPaidAmount(BigDecimal.ZERO);
            bill.setCreatedAt(LocalDateTime.now());
            bill.setUpdatedAt(LocalDateTime.now());

            billService.save(bill);

            // Fatura oluşturuldu eventi
            BillCreatedEvent event = new BillCreatedEvent();
            event.setBillId(bill.getBillId());
            event.setCustomerId(bill.getCustomerId());
            event.setTotalAmount(bill.getTotalAmount());
            event.setDueDate(bill.getDueDate());
            event.setServiceName("payment-service");

            log.info("Event gönderiliyor: {}", event);
            try {
                eventProducer.sendEvent(KafkaTopics.BILL_CREATED, event);
                log.info("Event başarıyla gönderildi");
            } catch (Exception e) {
                log.error("Event gönderilirken hata oluştu: {}", e.getMessage(), e);
                throw e;
            }

            return new CreatedBillResponse(
                    bill.getBillId(),
                    bill.getTotalAmount(),
                    bill.getCreatedAt(),
                    bill.getStatus(),
                    bill.getDueDate());
        }
    }
}
