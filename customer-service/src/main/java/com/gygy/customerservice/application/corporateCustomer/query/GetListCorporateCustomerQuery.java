package com.gygy.customerservice.application.corporateCustomer.query;

import an.awesome.pipelinr.Command;
import com.gygy.customerservice.application.corporateCustomer.mapper.CorporateCustomerMapper;
import com.gygy.customerservice.domain.entity.CorporateCustomer;
import com.gygy.customerservice.persistance.repository.CorporateCustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

public class GetListCorporateCustomerQuery implements Command<List<GetListCorporateCustomerItemDto>> {
    @Component
    @RequiredArgsConstructor
    public static class GetListCustomerQueryHandler implements Command.Handler<GetListCorporateCustomerQuery, List<GetListCorporateCustomerItemDto>> {
        private final CorporateCustomerRepository corporateCustomerRepository;
        private final CorporateCustomerMapper corporateCustomerMapper;

        @Override
        public List<GetListCorporateCustomerItemDto> handle(GetListCorporateCustomerQuery query) {
            List<CorporateCustomer> corporateCustomers = corporateCustomerRepository.findAll();

            List<GetListCorporateCustomerItemDto> corporateCustomerList = corporateCustomers.stream()
                    .map(corporateCustomerMapper::convertCorporateCustomerToGetListCorporateCustomerItemDto)
                    .toList();

            return corporateCustomerList;
        }
    }
}
