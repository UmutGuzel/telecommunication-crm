package com.gygy.customerservice.application.individualCustomer.query;

import an.awesome.pipelinr.Command;
import com.gygy.customerservice.application.individualCustomer.mapper.IndividualCustomerMapper;
import com.gygy.customerservice.domain.entity.IndividualCustomer;
import com.gygy.customerservice.persistance.repository.IndividualCustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

public class GetListIndividualCustomerQuery implements Command<List<GetListIndividualCustomerItemDto>> {
    @Component
    @RequiredArgsConstructor
    public static class GetListCustomerQueryHandler implements Command.Handler<GetListIndividualCustomerQuery, List<GetListIndividualCustomerItemDto>> {
        private final IndividualCustomerRepository individualCustomerRepository;
        private final IndividualCustomerMapper individualCustomerMapper;

        @Override
        public List<GetListIndividualCustomerItemDto> handle(GetListIndividualCustomerQuery query) {
            List<IndividualCustomer> individualCustomers = individualCustomerRepository.findAll();

            List<GetListIndividualCustomerItemDto> individualCustomerList = individualCustomers.stream()
                    .map(individualCustomerMapper::convertIndividualCustomerToGetListIndividualCustomerItemDto)
                    .toList();

            return individualCustomerList;
        }
    }
}
