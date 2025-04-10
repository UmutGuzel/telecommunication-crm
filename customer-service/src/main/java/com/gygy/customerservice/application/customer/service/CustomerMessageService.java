package com.gygy.customerservice.application.customer.service;

import org.springframework.stereotype.Service;

import com.gygy.customerservice.application.customer.query.GetCustomerByPhoneNumberResponse;

@Service
public class CustomerMessageService {
    
    public String getPhoneNumberStatusMessage(GetCustomerByPhoneNumberResponse response) {
        if (response.getIndividualCustomer() != null && response.getCorporateCustomer() != null) {
            return "This phone number is registered as both individual and corporate customer.";
        } else if (response.getIndividualCustomer() != null) {
            return "This phone number is registered as individual customer.";
        } else if (response.getCorporateCustomer() != null) {
            return "This phone number is registered as corporate customer.";
        }
        return "Phone number is not registered.";
    }
} 