package com.gygy.common.events.contractservice;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ContractCreatedEvent {
    private UUID contractId;
    private UUID customerId;
    private BigDecimal totalAmount;
    private LocalDate startDate;       // Örneğin kontratın başladığı gün
    private int durationInMonths;      // Örneğin 12 aylık kontrat

    //TODO: new
}
