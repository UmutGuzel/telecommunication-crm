package com.gygy.paymentservice.application.payment.query.getbyid;

import an.awesome.pipelinr.Command;
import com.gygy.paymentservice.application.payment.query.getbyid.dto.PaymentDetailResponse;
import com.gygy.paymentservice.application.payment.service.PaymentService;
import com.gygy.paymentservice.domain.entity.payment.Payment;
import lombok.*;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
//Belirli bir ödemenin detaylarını getiriyor.
public class GetPaymentByIdQuery implements Command<PaymentDetailResponse> {
    private UUID paymentId;

    @Component
    @RequiredArgsConstructor
    public static class GetPaymentByIdQueryHandler
            implements Command.Handler<GetPaymentByIdQuery, PaymentDetailResponse> {

        private final PaymentService paymentService;

        @Override
        public PaymentDetailResponse handle(GetPaymentByIdQuery query) {
            Payment payment = paymentService.findById(query.getPaymentId());

            return new PaymentDetailResponse(
                    payment.getPaymentId(),
                    payment.getBill().getBillId(),
                    payment.getCustomerId(),
                    payment.getPaidAmount(),
                    payment.getPaymentMethod(),
                    payment.getPaymentStatus(),
                    payment.getPaymentDate());
        }
    }
}