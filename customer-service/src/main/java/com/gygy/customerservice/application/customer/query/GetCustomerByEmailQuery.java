package com.gygy.customerservice.application.customer.query;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.gygy.customerservice.application.customer.mapper.CustomerMapper;
import com.gygy.customerservice.domain.entity.Customer;
import com.gygy.customerservice.persistance.repository.CustomerRepository;

import an.awesome.pipelinr.Command;
import lombok.RequiredArgsConstructor;

public class GetCustomerByEmailQuery implements Command<GetCustomerByEmailResponse> {
    private final String email;

    public GetCustomerByEmailQuery(String email) {
        this.email = email;
    }

    @Component
    @RequiredArgsConstructor
    public static class GetCustomerByEmailQueryHandler implements Command.Handler<GetCustomerByEmailQuery, GetCustomerByEmailResponse> {
        private final CustomerRepository customerRepository;
        private final CustomerMapper customerMapper;

        @Override
        public GetCustomerByEmailResponse handle(GetCustomerByEmailQuery query) {
            Optional<Customer> customer = customerRepository.findByEmail(query.email);
            return customer.map(customerMapper::convertCustomerToGetCustomerByEmailResponse)
                    .orElse(null);
        }
    }
} 