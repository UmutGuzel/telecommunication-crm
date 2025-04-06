package com.gygy.customerservice.application.customer.query;

import an.awesome.pipelinr.Command;
import com.gygy.customerservice.application.customer.mapper.CustomerMapper;
import com.gygy.customerservice.domain.entity.Customer;
import com.gygy.customerservice.persistance.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

public class GetCustomerByPhoneNumberQuery implements Command<GetCustomerByPhoneNumberResponse> {
    private final String phoneNumber;

    public GetCustomerByPhoneNumberQuery(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Component
    @RequiredArgsConstructor
    public static class GetCustomerByPhoneNumberQueryHandler implements Command.Handler<GetCustomerByPhoneNumberQuery, GetCustomerByPhoneNumberResponse> {
        private final CustomerRepository customerRepository;
        private final CustomerMapper customerMapper;

        @Override
        public GetCustomerByPhoneNumberResponse handle(GetCustomerByPhoneNumberQuery query) {
            Optional<Customer> customer = customerRepository.findByPhoneNumber(query.phoneNumber);
            return customer.map(customerMapper::convertCustomerToGetCustomerByPhoneNumberResponse)
                    .orElse(null);
        }
    }
} 