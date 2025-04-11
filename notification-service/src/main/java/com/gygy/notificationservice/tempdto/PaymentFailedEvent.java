package com.gygy.notificationservice.tempdto;

import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentFailedEvent {
    private UUID billId;
    private UUID customerId;
    private BigDecimal paidAmount;
    private String reason;
    private String email;  // sadece burada geçici olarak var
}
