package application.bill.query.getbyid;

import an.awesome.pipelinr.Command;
import application.bill.query.getbyid.dto.BillDetailResponse;
import application.bill.service.BillService;
import domain.entity.bill.Bill;
import lombok.*;
import org.springframework.stereotype.Component;


import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
//Belirli bir faturanın detaylarını getiriyor.
public class GetBillByIdQuery implements Command<BillDetailResponse> {
    private UUID billId;

    @Component
    @RequiredArgsConstructor
    public static class GetBillByIdQueryHandler
            implements Command.Handler<GetBillByIdQuery, BillDetailResponse> {

        private final BillService billService;

        @Override
        public BillDetailResponse handle(GetBillByIdQuery query) {
            Bill bill = billService.findById(query.getBillId());

            return new BillDetailResponse(
                    bill.getBillId(),
                    bill.getCustomerId(),
                    bill.getTotalAmount(),
                    bill.getPaidAmount(),
                    bill.getDueDate(),
                    bill.getStatus());

        }
    }
}