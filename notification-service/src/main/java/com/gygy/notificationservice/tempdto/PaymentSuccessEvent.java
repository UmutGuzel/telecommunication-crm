package com.gygy.notificationservice.tempdto;

import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentSuccessEvent {
    private UUID billId;
    private UUID customerId;
    private BigDecimal paidAmount;
    private String paymentMethod;
    private String email;  // sadece burada geçici olarak eklendi
}
