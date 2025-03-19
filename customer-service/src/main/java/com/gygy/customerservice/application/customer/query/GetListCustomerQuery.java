package com.gygy.customerservice.application.customer.query;

import an.awesome.pipelinr.Command;
import com.gygy.customerservice.application.customer.mapper.CustomerMapper;
import com.gygy.customerservice.domain.entity.Customer;
import com.gygy.customerservice.persistance.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

public class GetListCustomerQuery implements Command<List<GetListCustomerItemDto>> {
    @Component
    @RequiredArgsConstructor
    public static class GetListCustomerQueryHandler implements Command.Handler<GetListCustomerQuery, List<GetListCustomerItemDto>> {
        private final CustomerRepository customerRepository;
        private final CustomerMapper customerMapper;

        @Override
        public List<GetListCustomerItemDto> handle(GetListCustomerQuery query) {
            List<Customer> customers = customerRepository.findAll();

            List<GetListCustomerItemDto> customerList = customers.stream()
                    .map(customerMapper::convertCustomerToGetListCustomerItemDto)
                    .toList();

            return customerList;
        }
    }
}
