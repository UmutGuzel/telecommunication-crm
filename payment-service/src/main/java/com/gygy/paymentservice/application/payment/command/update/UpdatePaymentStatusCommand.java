package com.gygy.paymentservice.application.payment.command.update;

import an.awesome.pipelinr.Command;
import com.gygy.paymentservice.application.bill.service.BillService;
import com.gygy.paymentservice.application.payment.command.update.dto.UpdatedPaymentStatusResponse;
import com.gygy.paymentservice.application.payment.service.PaymentService;
import com.gygy.paymentservice.domain.entity.payment.Payment;
import com.gygy.paymentservice.domain.entity.payment.PaymentStatus;
import lombok.*;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePaymentStatusCommand implements Command<UpdatedPaymentStatusResponse> {
    private UUID paymentId;
    private PaymentStatus paymentStatus;

    @Component
    @RequiredArgsConstructor
    public static class UpdatePaymentCommandHandler
            implements Handler<UpdatePaymentStatusCommand, UpdatedPaymentStatusResponse> {

        private final PaymentService paymentService;
        private final BillService billService;

        @Override
        @Transactional
        public UpdatedPaymentStatusResponse handle(UpdatePaymentStatusCommand command) {
            Payment payment = paymentService.findById(command.getPaymentId());

            // Ödeme durumu değişikliğini kontrol et
            if (payment.getPaymentStatus() == command.getPaymentStatus()) {
                throw new IllegalStateException("Payment status is already " + command.getPaymentStatus());
            }

            // Ödeme durumunu güncelleriz
            payment.setPaymentStatus(command.getPaymentStatus());
            payment.setUpdatedAt(LocalDateTime.now());

            paymentService.save(payment);


            return new UpdatedPaymentStatusResponse(
                    payment.getPaymentId(),
                    payment.getBill().getBillId(),
                    payment.getCustomerId(),
                    payment.getPaymentStatus());
        }
    }
}
