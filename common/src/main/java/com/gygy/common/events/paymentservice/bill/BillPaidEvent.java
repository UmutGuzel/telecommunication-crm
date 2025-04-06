package com.gygy.common.events.paymentservice.bill;


import com.gygy.common.events.base.BaseEvent;
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
public class BillPaidEvent extends BaseEvent {
    private UUID billId;
    private BigDecimal totalAmount;
    private String paymentMethod;
    private LocalDateTime paymentDate;
}
