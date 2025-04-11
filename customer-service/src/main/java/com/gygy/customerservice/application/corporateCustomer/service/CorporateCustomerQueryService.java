package com.gygy.customerservice.application.corporateCustomer.service;

import com.gygy.customerservice.application.corporateCustomer.query.GetListCorporateCustomerItemDto;
import com.gygy.customerservice.persistance.repository.CorporateCustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CorporateCustomerQueryService{
    private final CorporateCustomerRepository corporateCustomerRepository;

    @Transactional(readOnly = true)
    public List<GetListCorporateCustomerItemDto> getAllCorporateCustomers() {
        return corporateCustomerRepository.findAllAsDto();
    }
}
