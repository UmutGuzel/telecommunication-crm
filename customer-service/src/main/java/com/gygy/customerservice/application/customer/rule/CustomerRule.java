package com.gygy.customerservice.application.customer.rule;

import com.gygy.customerservice.domain.entity.Customer;
import com.gygy.customerservice.persistance.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomerRule {

    public void checkUserNotExists(Customer customer) {
        if (customer != null) {
            throw new RuntimeException("Customer already exists");
        }
    }

    public void checkUserExists(Customer customer) {
        if (customer == null) {
            throw new RuntimeException("Customer does not exist");
        }
    }
}
