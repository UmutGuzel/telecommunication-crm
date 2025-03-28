package com.gygy.paymentservice.application.bill.query.getlist;

import an.awesome.pipelinr.Command;
import com.gygy.paymentservice.application.bill.query.getlist.dto.BillListResponse;
import com.gygy.paymentservice.application.bill.service.BillService;
import com.gygy.paymentservice.domain.entity.bill.Bill;
import lombok.*;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
// admin paneli için tüm faturaları getirir
public class GetBillsQuery implements Command<List<BillListResponse>> {

    @Component
    @RequiredArgsConstructor
    public static class GetBillsQueryHandler
            implements Command.Handler<GetBillsQuery, List<BillListResponse>> {

        private final BillService billService;

        @Override
        public List<BillListResponse> handle(GetBillsQuery query) {
            List<Bill> bills = billService.findAllBills();

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