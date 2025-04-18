package com.gygy.common.events.contractservice;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContractDetailEvent {
    private UUID discountId;
    private String discountName;
    private String discountDescription;
    private String contractName;
    private String customerName;
    private String email;
    private LocalDateTime signatureDate;

}
