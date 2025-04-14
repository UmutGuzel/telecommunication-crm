package com.gygy.customerservice.application.corporateCustomer.query.read;

import com.gygy.customerservice.application.corporateCustomer.mapper.CorporateCustomerMapper;
import com.gygy.customerservice.infrastructure.persistence.repository.CorporateCustomerReadRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import com.gygy.customerservice.domain.entity.CustomerReadEntity;

import an.awesome.pipelinr.Command;


public class GetListCorporateCustomerReadQuery implements Command<List<GetListCorporateCustomerItemReadDto>> {
    @Component
    @RequiredArgsConstructor
    public static class GetListCorporateCustomerReadQueryHandler implements Command.Handler<GetListCorporateCustomerReadQuery, List<GetListCorporateCustomerItemReadDto>> {
        private final CorporateCustomerReadRepository corporateCustomerReadRepository;
        private final CorporateCustomerMapper corporateCustomerMapper;

        @Override
        public List<GetListCorporateCustomerItemReadDto> handle(GetListCorporateCustomerReadQuery query) {
            List<CustomerReadEntity> corporateCustomers = corporateCustomerReadRepository.findAllCorporateCustomers();
            return corporateCustomers.stream()
                    .map(corporateCustomerMapper::convertCustomerReadEntityToGetListCorporateCustomerItemReadDto)
                    .collect(Collectors.toList());
        }
    }
}
