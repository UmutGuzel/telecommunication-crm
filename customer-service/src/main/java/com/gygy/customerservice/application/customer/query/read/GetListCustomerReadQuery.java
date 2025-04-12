package com.gygy.customerservice.application.customer.query.read;

import an.awesome.pipelinr.Command;
import com.gygy.customerservice.application.customer.mapper.CustomerMapper;
import com.gygy.customerservice.application.customer.query.GetListCustomerItemDto;
import com.gygy.customerservice.domain.entity.CustomerReadEntity;
import com.gygy.customerservice.infrastructure.persistence.repository.CustomerReadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

public class GetListCustomerReadQuery implements Command<List<GetListCustomerItemDto>> {
    @Component
    @RequiredArgsConstructor
    public static class GetListCustomerReadQueryHandler implements Command.Handler<GetListCustomerReadQuery, List<GetListCustomerItemDto>> {
        private final CustomerReadRepository customerReadRepository;
        private final CustomerMapper customerMapper;

        @Override
        public List<GetListCustomerItemDto> handle(GetListCustomerReadQuery query) {
            List<CustomerReadEntity> customers = customerReadRepository.findAllAsDto();
            return customers.stream()
                    .map(customerMapper::convertCustomerReadEntityToGetListCustomerItemDto)
                    .collect(Collectors.toList());
        }
    }
} 