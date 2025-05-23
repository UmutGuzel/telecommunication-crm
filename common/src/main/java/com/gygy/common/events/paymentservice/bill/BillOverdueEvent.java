package com.gygy.common.events.paymentservice.bill;

import com.gygy.common.events.base.BaseEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BillOverdueEvent extends BaseEvent {
    private UUID billId;
    private BigDecimal amount;
    private LocalDate dueDate;
}
