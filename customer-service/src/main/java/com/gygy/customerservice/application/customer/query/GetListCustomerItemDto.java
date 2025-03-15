package com.gygy.customerservice.application.customer.query;

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
}
