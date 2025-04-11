package com.gygy.customerservice.application.corporateCustomer.query;

import an.awesome.pipelinr.Command;
import com.gygy.customerservice.application.corporateCustomer.service.CorporateCustomerQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class GetListCorporateCustomerQuery implements Command<List<GetListCorporateCustomerItemDto>> {
    @Component
    @RequiredArgsConstructor
    public static class GetListCustomerQueryHandler implements Command.Handler<GetListCorporateCustomerQuery, List<GetListCorporateCustomerItemDto>> {
        private final CorporateCustomerQueryService corporateCustomerQueryService;

        @Override
        @Transactional(readOnly = true)
        public List<GetListCorporateCustomerItemDto> handle(GetListCorporateCustomerQuery query) {
            return corporateCustomerQueryService.getAllCorporateCustomers();
        }
    }
}

