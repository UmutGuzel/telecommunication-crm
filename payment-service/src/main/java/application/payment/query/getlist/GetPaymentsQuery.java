package application.payment.query.getlist;

import an.awesome.pipelinr.Command;
import application.payment.query.getlist.dto.PaymentListResponse;
import application.payment.service.PaymentService;
import domain.entity.payment.Payment;
import domain.entity.payment.PaymentStatus;
import lombok.*;
import org.springframework.stereotype.Component;
import persistence.payment.PaymentRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
//Bir müşterinin başarısız ödemelerini listeliyor.
public class GetPaymentsQuery implements Command<List<PaymentListResponse>> {
    private UUID customerId;
    private PaymentStatus paymentStatus;

    @Component
    @RequiredArgsConstructor
    public static class GetFailedPaymentsQueryHandler
            implements Command.Handler<GetPaymentsQuery, List<PaymentListResponse>> {

        private final PaymentService paymentService;

        @Override
        public List<PaymentListResponse> handle(GetPaymentsQuery query) {
            List<Payment> payments = paymentService.findByCustomerIdAndPaymentStatus(query.getCustomerId(), query.getPaymentStatus());

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