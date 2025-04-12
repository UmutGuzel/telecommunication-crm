package com.gygy.customerservice.application.individualCustomer.service;

import com.gygy.customerservice.application.individualCustomer.query.GetListIndividualCustomerItemDto;
import com.gygy.customerservice.infrastructure.persistence.repository.IndividualCustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IndividualCustomerQueryService{
    private final IndividualCustomerRepository individualCustomerRepository;

    @Transactional(readOnly = true)
    public List<GetListIndividualCustomerItemDto> getAllIndividualCustomers() {
        return individualCustomerRepository.findAllAsDto();
    }
}
