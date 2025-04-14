package com.gygy.customerservice.application.individualCustomer.query.read;

import com.gygy.customerservice.application.individualCustomer.mapper.IndividualCustomerMapper;
import com.gygy.customerservice.infrastructure.persistence.repository.IndividualCustomerReadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import com.gygy.customerservice.domain.entity.CustomerReadEntity;

import an.awesome.pipelinr.Command;


public class GetListIndividualCustomerReadQuery implements Command<List<GetListIndividualCustomerItemReadDto>> {
    @Component
    @RequiredArgsConstructor
    public static class GetListIndividualCustomerReadQueryHandler implements Command.Handler<GetListIndividualCustomerReadQuery, List<GetListIndividualCustomerItemReadDto>> {
        private final IndividualCustomerReadRepository individualCustomerReadRepository;
        private final IndividualCustomerMapper individualCustomerMapper;

        @Override
        public List<GetListIndividualCustomerItemReadDto> handle(GetListIndividualCustomerReadQuery query) {
            List<CustomerReadEntity> individualCustomers = individualCustomerReadRepository.findAllIndividualCustomers();
            return individualCustomers.stream()
                    .map(individualCustomerMapper::convertCustomerReadEntityToGetListIndividualCustomerItemReadDto)
                    .collect(Collectors.toList());
        }
    }
}
