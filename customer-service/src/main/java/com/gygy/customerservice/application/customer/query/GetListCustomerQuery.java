package com.gygy.customerservice.application.customer.query;

import an.awesome.pipelinr.Command;
import com.gygy.customerservice.application.customer.service.CustomerQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

public class GetListCustomerQuery implements Command<List<GetListCustomerItemDto>> {
    @Component
    @RequiredArgsConstructor
    public static class GetListCustomerQueryHandler implements Command.Handler<GetListCustomerQuery, List<GetListCustomerItemDto>> {
        private final CustomerQueryService customerQueryService;

        @Override
        public List<GetListCustomerItemDto> handle(GetListCustomerQuery query) {
            return customerQueryService.getAllCustomers();
        }
    }
}
