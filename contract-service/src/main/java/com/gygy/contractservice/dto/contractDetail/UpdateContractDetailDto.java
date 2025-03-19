package com.gygy.contractservice.dto.contractDetail;

import com.gygy.contractservice.model.enums.ServiceType;
import jakarta.validation.constraints.NotNull;
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
    private ServiceType serviceType;
    private String type;
    @NotNull(message = "Contract ID is required")
    private UUID contractId;

}
