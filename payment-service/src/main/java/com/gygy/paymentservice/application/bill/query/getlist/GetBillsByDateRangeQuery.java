package com.gygy.paymentservice.application.bill.query.getlist;

import an.awesome.pipelinr.Command;
import com.gygy.paymentservice.application.bill.query.getlist.dto.BillListResponse;
import com.gygy.paymentservice.application.bill.service.BillService;
import com.gygy.paymentservice.domain.entity.bill.Bill;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetBillsByDateRangeQuery implements Command<List<BillListResponse>> {

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) // YYYY-MM-DD
    private LocalDate startDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) // YYYY-MM-DD, dahil.
    private LocalDate endDate;

    @Component
    @RequiredArgsConstructor
    public static class GetBillsByDateRangeQueryHandler
            implements Command.Handler<GetBillsByDateRangeQuery, List<BillListResponse>> {

        private final BillService billService;

        @Override
        public List<BillListResponse> handle(GetBillsByDateRangeQuery query) {
            List<Bill> bills = billService.findBillsByDateRange(
                    query.getStartDate(),
                    query.getEndDate());

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
