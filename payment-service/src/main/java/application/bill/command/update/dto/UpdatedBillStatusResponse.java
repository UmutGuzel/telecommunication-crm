package application.bill.command.update.dto;

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
public class UpdatedBillStatusResponse {
    private UUID billId;
    private LocalDate dueDate;
    private BillStatus status;
    private LocalDateTime updatedAt;

}
