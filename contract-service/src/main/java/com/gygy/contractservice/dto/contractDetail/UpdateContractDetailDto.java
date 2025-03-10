package com.gygy.contractservice.dto.contractDetail;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class UpdateContractDetailDto {
    private UUID id;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;
    private String serviceType;
    private String type;

}
