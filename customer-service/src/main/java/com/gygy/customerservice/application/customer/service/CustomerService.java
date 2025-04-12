package com.gygy.customerservice.application.customer.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    public UUID convertStringToUUID(String uuidString) {
        return UUID.fromString(uuidString);
    }
} 