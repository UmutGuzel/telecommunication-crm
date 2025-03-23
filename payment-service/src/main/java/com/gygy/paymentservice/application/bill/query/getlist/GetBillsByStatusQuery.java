package com.gygy.paymentservice.application.bill.query.getlist;

import an.awesome.pipelinr.Command;
import com.gygy.paymentservice.application.bill.query.getlist.dto.BillListResponse;
import com.gygy.paymentservice.application.bill.service.BillService;
import com.gygy.paymentservice.domain.entity.bill.Bill;
import com.gygy.paymentservice.domain.entity.bill.BillStatus;
import lombok.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetBillsByStatusQuery implements Command<List<BillListResponse>> {
    private BillStatus status;

    @Component
    @RequiredArgsConstructor
    public static class GetBillsByStatusQueryHandler
            implements Command.Handler<GetBillsByStatusQuery, List<BillListResponse>> {

        private final BillService billService;

        @Override
        public List<BillListResponse> handle(GetBillsByStatusQuery query) {
            List<Bill> bills = billService.findByStatus(query.getStatus());

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
