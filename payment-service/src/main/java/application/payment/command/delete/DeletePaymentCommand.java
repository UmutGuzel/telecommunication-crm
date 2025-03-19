package application.payment.command.delete;

import an.awesome.pipelinr.Command;
import application.payment.command.delete.dto.DeletedPaymentResponse;
import application.payment.service.PaymentService;
import domain.entity.payment.Payment;
import domain.entity.payment.PaymentStatus;
import domain.exception.payment.PaymentNotFoundException;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import persistence.payment.PaymentRepository;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeletePaymentCommand implements Command<DeletedPaymentResponse> {

    @NotNull(message = "Payment ID is required")
    private UUID paymentId;

    @Component
    @RequiredArgsConstructor
    public static class DeletePaymentCommandHandler
            implements Command.Handler<DeletePaymentCommand, DeletedPaymentResponse> {

        private final PaymentService paymentService;

        @Override
        @Transactional
        public DeletedPaymentResponse handle(DeletePaymentCommand deletePaymentCommand) {
            Payment payment = paymentService.findById(deletePaymentCommand.paymentId);

            payment.setPaymentStatus(PaymentStatus.DELETED);
            payment.setDeletedAt(LocalDateTime.now());
            paymentService.save(payment);

            return new DeletedPaymentResponse(
            payment.getPaymentId(),
            payment.getDeletedAt(),
            payment.getPaymentStatus());
        }
    }
}
