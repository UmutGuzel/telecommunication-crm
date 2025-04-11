package com.gygy.customerservice.application.customer.query;

import com.gygy.customerservice.domain.enums.CustomerType;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetListCustomerItemDto {
    private UUID id;
    private String email;
    private String phoneNumber;
    private CustomerType type;
}
