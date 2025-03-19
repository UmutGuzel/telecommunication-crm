package application.bill.query.getlist;

import an.awesome.pipelinr.Command;
import application.bill.query.getlist.dto.BillListResponse;
import application.bill.service.BillService;
import domain.entity.bill.Bill;
import lombok.*;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
//Bir müşterinin tüm faturalarını listeliyor.
public class GetCustomerBillsQuery implements Command<List<BillListResponse>> {
    private UUID customerId;

    @Component
    @RequiredArgsConstructor
    public static class GetCustomerBillsQueryHandler
            implements Command.Handler<GetCustomerBillsQuery, List<BillListResponse>> {

        private final BillService billService;

        @Override
        public List<BillListResponse> handle(GetCustomerBillsQuery query) {
            List<Bill> bills = billService.findByCustomerId(query.getCustomerId());

            return bills.stream()
                    .map(bill -> new BillListResponse(
                            bill.getBillId(),
                            bill.getCustomerId(),
                            bill.getTotalAmount(),
                            bill.getPaidAmount(),
                            bill.getDueDate(),
                            bill.getStatus()))
                    .collect(Collectors.toList());
        }
    }
}