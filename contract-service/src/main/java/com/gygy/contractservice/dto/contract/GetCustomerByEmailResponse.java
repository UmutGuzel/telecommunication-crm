package com.gygy.contractservice.dto.contract;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetCustomerByEmailResponse {
    private UUID id;
    private String email;
    private String phoneNumber;
    private String customerType;
}