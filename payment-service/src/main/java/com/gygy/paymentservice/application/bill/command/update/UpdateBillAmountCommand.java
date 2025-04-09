package com.gygy.paymentservice.application.bill.command.update;

import an.awesome.pipelinr.Command;
import com.gygy.common.constants.KafkaTopics;
import com.gygy.common.kafka.producer.EventProducer;
import com.gygy.paymentservice.application.bill.command.update.dto.UpdatedBillAmountResponse;
import com.gygy.paymentservice.application.bill.service.BillService;
import com.gygy.paymentservice.domain.entity.bill.Bill;
import com.gygy.paymentservice.domain.exception.bill.BillAmountException;
import lombok.*;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateBillAmountCommand implements Command<UpdatedBillAmountResponse> {
    private UUID billId;
    private BigDecimal totalAmount;

    @Component
    @RequiredArgsConstructor
    public static class UpdateBillAmountCommandHandler
            implements Command.Handler<UpdateBillAmountCommand, UpdatedBillAmountResponse> {

        private final BillService billService;

        @Override
        @Transactional
        public UpdatedBillAmountResponse handle(UpdateBillAmountCommand command) {
            Bill bill = billService.findById(command.getBillId());

            if (command.getTotalAmount().compareTo(bill.getPaidAmount()) < 0) {
                throw new BillAmountException("New total amount cannot be less than paid amount");
            }

            bill.setTotalAmount(command.getTotalAmount());
            bill.setUpdatedAt(LocalDateTime.now());
            bill.updateStatus();

            billService.save(bill);

            return new UpdatedBillAmountResponse(
                    bill.getBillId(),
                    bill.getTotalAmount(),
                    bill.getPaidAmount());
        }
    }
}

