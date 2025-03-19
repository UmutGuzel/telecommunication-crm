package application.payment.command.update.dto;

import domain.entity.payment.PaymentMethod;
import domain.entity.payment.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdatedPaymentStatusResponse {
    private UUID paymentId;
    private UUID billId;
    private UUID customerId;
    private PaymentStatus paymentStatus;
}
