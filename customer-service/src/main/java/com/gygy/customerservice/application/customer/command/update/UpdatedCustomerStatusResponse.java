package com.gygy.customerservice.application.customer.command.update;

import com.gygy.customerservice.domain.enums.CustomerStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdatedCustomerStatusResponse {
    private UUID id;
    private CustomerStatus status;
} 