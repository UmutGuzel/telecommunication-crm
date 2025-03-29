package com.gygy.paymentservice.application.bill.command.create;

import an.awesome.pipelinr.Command;
import com.gygy.paymentservice.application.bill.command.create.dto.CreatedBillResponse;
import com.gygy.paymentservice.application.bill.service.BillService;
import com.gygy.paymentservice.domain.entity.bill.Bill;
import com.gygy.paymentservice.domain.entity.bill.BillStatus;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.*;

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

    // TODO: customerId dışarıdan alınmalı
    private UUID customerId;

    // TODO: amount dışarıdan alınmalı
    @NotNull(message = "Total amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    private BigDecimal totalAmount;

    @NotNull(message = "Due date is required")
    @Future(message = "Due date must be in the future")
    private LocalDate dueDate;

    @Component
    @RequiredArgsConstructor
    public static class CreateBillCommandHandler implements Command.Handler<CreateBillCommand, CreatedBillResponse> {
        private final BillService billService;

        @Override
        @Transactional
        public CreatedBillResponse handle(CreateBillCommand createBillCommand) {
            Bill bill = new Bill();
            bill.setCustomerId(createBillCommand.getCustomerId());
            bill.setTotalAmount(createBillCommand.getTotalAmount());
            bill.setDueDate(createBillCommand.getDueDate());
            bill.setStatus(BillStatus.PENDING);
            bill.setPaidAmount(BigDecimal.ZERO);
            bill.setCreatedAt(LocalDateTime.now());
            bill.setUpdatedAt(LocalDateTime.now());

            billService.save(bill);

            return new CreatedBillResponse(
                    bill.getBillId(),
                    bill.getTotalAmount(),
                    bill.getCreatedAt(),
                    bill.getStatus(),
                    bill.getDueDate());
        }
    }
}
