package application.bill.command.delete.dto;

import domain.entity.bill.BillStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DeletedBillResponse {
    private UUID billId;
    private LocalDateTime deletedAt;
    private BillStatus status;
}
