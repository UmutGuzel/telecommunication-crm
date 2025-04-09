package com.gygy.common.events.contractservice;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ContractCreatedEvent {
    private UUID contractId;
    private UUID customerId;
    private BigDecimal totalAmount;
    private LocalDate startDate;       // Örneğin kontratın başladığı gün
    private int durationInMonths;      // Örneğin 12 aylık kontrat



    //TODO: new
}
