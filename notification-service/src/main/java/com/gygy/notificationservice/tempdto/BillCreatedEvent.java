package com.gygy.notificationservice.tempdto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BillCreatedEvent {
    private UUID billId;
    private UUID customerId;
    private BigDecimal totalAmount;
    private LocalDate dueDate;
    private String email;  // Bu alan notification için eklendi
}
