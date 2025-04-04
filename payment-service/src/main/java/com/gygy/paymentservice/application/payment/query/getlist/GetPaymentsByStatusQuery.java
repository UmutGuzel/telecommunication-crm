package com.gygy.paymentservice.application.payment.query.getlist;
import an.awesome.pipelinr.Command;
import com.gygy.paymentservice.application.payment.query.getlist.dto.PaymentListResponse;
import com.gygy.paymentservice.application.payment.service.PaymentService;
import com.gygy.paymentservice.domain.entity.payment.Payment;
import com.gygy.paymentservice.domain.entity.payment.PaymentStatus;
import lombok.*;
import org.springframework.stereotype.Component;


import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class GetPaymentsByStatusQuery implements Command<List<PaymentListResponse>> {
    private PaymentStatus paymentStatus;

    @Component
    @RequiredArgsConstructor
    public static class GetPaymentsByStatusQueryHandler
            implements Command.Handler<GetPaymentsByStatusQuery, List<PaymentListResponse>> {

        private final PaymentService paymentService;

        @Override
        public List<PaymentListResponse> handle(GetPaymentsByStatusQuery query) {

            List<Payment> payments = paymentService.findByPaymentStatus(query.getPaymentStatus());

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
