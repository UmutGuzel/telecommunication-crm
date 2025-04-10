package com.gygy.customerservice.application.customer.mapper;

import org.springframework.stereotype.Component;

import com.gygy.customerservice.application.customer.command.delete.DeletedCustomerResponse;
import com.gygy.customerservice.application.customer.query.GetCustomerByEmailResponse;
import com.gygy.customerservice.application.customer.query.GetCustomerByPhoneNumberResponse;
import com.gygy.customerservice.application.customer.query.GetListCustomerItemDto;
import com.gygy.customerservice.domain.entity.CorporateCustomer;
import com.gygy.customerservice.domain.entity.Customer;
import com.gygy.customerservice.domain.entity.IndividualCustomer;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CustomerMapper {
    private final AddressMapper addressMapper;

    public DeletedCustomerResponse convertCustomerToDeletedCustomerResponse(Customer customer) {
        return DeletedCustomerResponse.builder()
                .id(customer.getId())
                .build();
    }

    public GetListCustomerItemDto convertCustomerToGetListCustomerItemDto(Customer customer) {
        return GetListCustomerItemDto.builder()
                .id(customer.getId())
                .email(customer.getEmail())
                .phoneNumber(customer.getPhoneNumber())
                .build();
    }

    public GetCustomerByEmailResponse convertCustomerToGetCustomerByEmailResponse(Customer customer) {
        return GetCustomerByEmailResponse.builder()
                .id(customer.getId())
                .email(customer.getEmail())
                .phoneNumber(customer.getPhoneNumber())
                .customerType(customer.getType().name())
                .build();
    }

    public GetCustomerByPhoneNumberResponse.IndividualCustomerInfo convertToIndividualCustomerInfo(IndividualCustomer customer) {
        return GetCustomerByPhoneNumberResponse.IndividualCustomerInfo.builder()
                .id(customer.getId())
                .email(customer.getEmail())
                .name(customer.getName())
                .surname(customer.getSurname())
                .build();
    }

    public GetCustomerByPhoneNumberResponse.CorporateCustomerInfo convertToCorporateCustomerInfo(CorporateCustomer customer) {
        return GetCustomerByPhoneNumberResponse.CorporateCustomerInfo.builder()
                .id(customer.getId())
                .email(customer.getEmail())
                .companyName(customer.getCompanyName())
                .contactPersonName(customer.getContactPersonName())
                .contactPersonSurname(customer.getContactPersonSurname())
                .build();
    }
}
