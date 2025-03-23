package com.gygy.paymentservice.application.payment.query.getlist;

import an.awesome.pipelinr.Command;
import com.gygy.paymentservice.application.payment.query.getlist.dto.PaymentListResponse;
import com.gygy.paymentservice.application.payment.service.PaymentService;
import com.gygy.paymentservice.domain.entity.payment.Payment;
import lombok.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
//Bir müşterinin tüm ödemelerini listeliyor.
public class GetCustomerPaymentsQuery implements Command<List<PaymentListResponse>> {
    private UUID customerId;

    @Component
    @RequiredArgsConstructor
    public static class GetCustomerPaymentsQueryHandler
            implements Command.Handler<GetCustomerPaymentsQuery, List<PaymentListResponse>> {

        private final PaymentService paymentService;

        @Override
        public List<PaymentListResponse> handle(GetCustomerPaymentsQuery query) {
            List<Payment> payments = paymentService.findByCustomerId(query.getCustomerId());

            return payments.stream()
                    .map(payment -> new PaymentListResponse(
                            payment.getPaymentId(),
                            payment.getBill().getBillId(),
                            payment.getCustomerId(),
                            payment.getPaidAmount(),
                            payment.getPaymentMethod(),
                            payment.getPaymentStatus(),
                            payment.getPaymentDate()))
                    .collect(Collectors.toList());
        }
    }
}