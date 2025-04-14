package com.gygy.contractservice.dto.contract;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import java.util.UUID;

@Getter
@Setter
public class CreateContractDto {
    @NotNull(message = "Plan ID is required")
    private UUID planId;
    @NotBlank(message = "Customer email is required")
    private String customerEmail;
    @NotBlank(message = "Service type is required")
    private String serviceType;
    @NotBlank(message = "Payment method is required")
    private String paymentMethod;
    @NotBlank(message = "Cycle type is required")
    private String cycleType;
    @NotBlank(message = "Discount ID is required")
    private UUID discountId;
    // @NotBlank(message = "Contract number is required")
    // private String contractNumber;

    // @NotBlank(message = "Document type is required")
    // private String documentType;

    // private String documentUrl;

    // private LocalDateTime createDate;

    // private LocalDateTime uploadDate;

    // @NotNull(message = "Status is required")
    // private Status status;
}
