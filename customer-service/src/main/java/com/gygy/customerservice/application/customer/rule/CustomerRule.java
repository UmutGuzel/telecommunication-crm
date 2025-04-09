package com.gygy.customerservice.application.customer.rule;

import org.springframework.stereotype.Component;

import com.gygy.customerservice.domain.entity.Customer;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CustomerRule {

    public void checkCustomerNotExists(Customer customer) {
        if (customer != null) {
            throw new RuntimeException("Customer already exists");
        }
    }

    public void checkCustomerExists(Customer customer) {
        if (customer == null) {
            throw new RuntimeException("Customer does not exist");
        }
    }
}
