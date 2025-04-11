package com.gygy.notificationservice.tempdto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BillPaidEvent {
    private UUID billId;
    private UUID customerId;
    private BigDecimal totalAmount;
    private String paymentMethod;
    private LocalDateTime paymentDate;
    private String email;  // common’da yok, burada geçici olarak ekliyoruz
}
