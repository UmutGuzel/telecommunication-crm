package com.gygy.customerservice.application.customer.query;

import com.gygy.customerservice.application.customer.dto.AddressResponse;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetCustomerByPhoneNumberResponse {
    private UUID id;
    private String email;
    private String phoneNumber;
    private String customerType;
} 