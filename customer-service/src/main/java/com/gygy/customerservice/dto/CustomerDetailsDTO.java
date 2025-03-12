package com.gygy.customerservice.dto;

import java.util.UUID;

import com.gygy.customerservice.entity.CustomerStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerDetailsDTO {
    private UUID id;
    private String email;
    private String phoneNumber;
    private CustomerStatus status;
    private UUID addressId;
} 