package com.gygy.paymentservice.application.payment.command.update;
import an.awesome.pipelinr.Command;
import com.gygy.paymentservice.application.payment.command.update.dto.UpdatedPaymentMethodResponse;
import com.gygy.paymentservice.application.payment.service.PaymentService;
import com.gygy.paymentservice.domain.entity.payment.Payment;
import com.gygy.paymentservice.domain.entity.payment.PaymentMethod;
import com.gygy.paymentservice.domain.entity.payment.PaymentStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePaymentMethodCommand implements Command<UpdatedPaymentMethodResponse> {
    @NotNull(message = "Payment ID is required")
    private UUID paymentId;

    @NotNull(message = "Payment method is required")
    private PaymentMethod paymentMethod;

    @Component
    @RequiredArgsConstructor
    public static class UpdatePaymentMethodCommandHandler implements Command.Handler<UpdatePaymentMethodCommand, UpdatedPaymentMethodResponse> {
        private final PaymentService paymentService;

        @Override
        @Transactional
        public UpdatedPaymentMethodResponse handle(UpdatePaymentMethodCommand command) {
            Payment payment = paymentService.findById(command.getPaymentId());

            if (payment.getPaymentStatus() == PaymentStatus.COMPLETED) {
                throw new IllegalStateException("Payment method can only be updated for FAILED payments");
            }

            // Ödeme yöntemini güncelle
            payment.setPaymentMethod(command.getPaymentMethod());
            payment.setUpdatedAt(LocalDateTime.now());

            paymentService.save(payment);

            return new UpdatedPaymentMethodResponse(
                    payment.getPaymentId(),
                    payment.getBill().getBillId(),
                    payment.getCustomerId(),
                    payment.getPaymentMethod());

        }
    }
}
