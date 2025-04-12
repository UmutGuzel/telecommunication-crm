package com.gygy.notificationservice.tempdto;

import lombok.*;

import java.util.UUID;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContractCreatedEvent {
    private UUID contractId;
    private UUID customerId;
    private String contractName;
    private LocalDateTime startDate;
    private String email;
}
