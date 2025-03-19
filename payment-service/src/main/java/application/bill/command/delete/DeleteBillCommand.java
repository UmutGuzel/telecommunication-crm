package application.bill.command.delete;

import an.awesome.pipelinr.Command;
import application.bill.command.delete.dto.DeletedBillResponse;
import application.bill.service.BillService;
import domain.entity.bill.Bill;
import domain.entity.bill.BillStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeleteBillCommand implements Command<DeletedBillResponse> {

    @NotNull(message = "Bill ID is required")
    private UUID billId;

    @Component
    @RequiredArgsConstructor
    public static class DeleteBillCommandHandler
            implements Command.Handler<DeleteBillCommand, DeletedBillResponse>{

        private final BillService billService;
        // TODO: private final KafkaTemplate<String, BillDeletedEvent> kafkaTemplate;

        @Override
        @Transactional
        public DeletedBillResponse handle(DeleteBillCommand deleteBillCommand) {

            Bill bill = billService.findById(deleteBillCommand.getBillId());

            bill.setStatus(BillStatus.DELETED);// soft delete
            bill.setDeletedAt(LocalDateTime.now());
            billService.save(bill);

            // TODO: kafka event gönderilir burada

            return new DeletedBillResponse(
                    bill.getBillId(),
                    bill.getDeletedAt(),
                    bill.getStatus()
            );

        }
}}
