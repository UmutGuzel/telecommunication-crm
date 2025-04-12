package com.gygy.customerservice.application.customer.service;

import com.gygy.customerservice.application.customer.query.GetListCustomerItemDto;
import com.gygy.customerservice.infrastructure.persistence.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerQueryService{
    private final CustomerRepository customerRepository;

    @Transactional(readOnly = true)
    public List<GetListCustomerItemDto> getAllCustomers() {
        return customerRepository.findAllAsDto();
    }
}