package application.bill.command.create.dto;

import domain.entity.bill.BillStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreatedBillResponse {
    private UUID billId;
    private BigDecimal totalAmount;
    private LocalDateTime createdAt;
    private BillStatus status;
    private LocalDate dueDate;

}
