package com.gygy.paymentservice.application.bill.command.update;

import an.awesome.pipelinr.Command;
import com.gygy.paymentservice.application.bill.command.update.dto.UpdatedBillStatusResponse;
import com.gygy.paymentservice.application.bill.service.BillService;
import com.gygy.paymentservice.domain.entity.bill.Bill;
import com.gygy.paymentservice.domain.entity.bill.BillStatus;
import lombok.*;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateBillStatusCommand implements Command<UpdatedBillStatusResponse> {
    private UUID billId;
    private BillStatus status;

    @Component
    @RequiredArgsConstructor
    public static class UpdateBillStatusCommandHandler
            implements Command.Handler<UpdateBillStatusCommand, UpdatedBillStatusResponse> {

        private final BillService billService;

        @Override
        @Transactional
        public UpdatedBillStatusResponse handle(UpdateBillStatusCommand command) {

            Bill bill = billService.findById(command.getBillId());

            if (command.getStatus() == null) {
                throw new IllegalArgumentException("Status cannot be null");
            }

            bill.setStatus(command.getStatus());
            bill.setUpdatedAt(LocalDateTime.now());

            billService.save(bill);

            return new UpdatedBillStatusResponse(
                    bill.getBillId(),
                    bill.getDueDate(),
                    bill.getStatus(),
                    bill.getUpdatedAt()
            );

        }
    }
}
