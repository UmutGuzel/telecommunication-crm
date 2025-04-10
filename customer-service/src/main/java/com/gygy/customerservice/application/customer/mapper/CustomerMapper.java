package com.gygy.customerservice.application.customer.mapper;

import com.gygy.customerservice.application.customer.command.delete.DeletedCustomerResponse;
import com.gygy.customerservice.application.customer.query.GetCustomerByEmailResponse;
import com.gygy.customerservice.application.customer.query.GetListCustomerItemDto;
import com.gygy.customerservice.domain.entity.Customer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

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
}
