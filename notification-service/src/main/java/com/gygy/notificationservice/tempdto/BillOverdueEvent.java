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
public class BillOverdueEvent {
    private UUID billId;
    private UUID customerId;
    private BigDecimal amount;
    private LocalDate dueDate;
    private String email; // NOT: common'da yok ama burada ekliyoruz
}
