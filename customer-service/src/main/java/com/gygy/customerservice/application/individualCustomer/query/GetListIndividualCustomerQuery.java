package com.gygy.customerservice.application.individualCustomer.query;

import an.awesome.pipelinr.Command;
import com.gygy.customerservice.application.individualCustomer.service.IndividualCustomerQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class GetListIndividualCustomerQuery implements Command<List<GetListIndividualCustomerItemDto>> {
    
    @Component
    @RequiredArgsConstructor
    public static class GetListCustomerQueryHandler implements Command.Handler<GetListIndividualCustomerQuery, List<GetListIndividualCustomerItemDto>> {
        private final IndividualCustomerQueryService individualCustomerQueryService;

        @Override
        @Transactional(readOnly = true)
        public List<GetListIndividualCustomerItemDto> handle(GetListIndividualCustomerQuery query) {
            return individualCustomerQueryService.getAllIndividualCustomers();
        }
    }
}
