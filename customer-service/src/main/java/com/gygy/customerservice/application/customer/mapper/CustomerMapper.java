package com.gygy.customerservice.application.customer.mapper;

import com.gygy.customerservice.application.customer.command.create.CreateCustomerCommand;
import com.gygy.customerservice.application.customer.command.create.CreatedCustomerResponse;
import com.gygy.customerservice.application.customer.command.delete.DeletedCustomerResponse;
import com.gygy.customerservice.application.customer.command.update.UpdateCustomerCommand;
import com.gygy.customerservice.application.customer.command.update.UpdatedCustomerResponse;
import com.gygy.customerservice.application.customer.query.GetListCustomerItemDto;
import com.gygy.customerservice.domain.entity.Customer;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

import com.gygy.customerservice.application.customer.dto.AddressResponse;
import com.gygy.customerservice.domain.entity.Address;

@Component
@RequiredArgsConstructor
public class CustomerMapper {
    private final AddressMapper addressMapper;
   
    public Customer convertCreateCommandToCustomer(CreateCustomerCommand command) {
        Address address = addressMapper.convertCreateAddressDtoToAddress(command.getAddress());
        
        return Customer.builder()
                .email(command.getEmail())
                .phoneNumber(command.getPhoneNumber())
                .build();
    }

    public CreatedCustomerResponse convertCustomerToCreatedCustomerResponse(Customer customer) {
        AddressResponse addressResponse = addressMapper.convertAddressToAddressResponse(customer.getAddress());

        return CreatedCustomerResponse.builder()
                .id(customer.getId())
                .email(customer.getEmail())
                .phoneNumber(customer.getPhoneNumber())
                .address(addressResponse)
                .build();
    }

    public void updateCustomer(Customer customer, UpdateCustomerCommand command) {
        if (command.getEmail() != null && !command.getEmail().isEmpty()) {
            customer.setEmail(command.getEmail());
        }
        if (command.getPhoneNumber() != null && !command.getPhoneNumber().isEmpty()) {
            customer.setPhoneNumber(command.getPhoneNumber());
        }

        if (command.getAddress() != null) {
            Address updatedAddress = addressMapper.convertUpdateAddressDtoToAddress(customer.getAddress(), command.getAddress());
            customer.setAddress(updatedAddress);
        }
    }

    public UpdatedCustomerResponse convertCustomerToUpdatedCustomerResponse(Customer customer) {
        return UpdatedCustomerResponse.builder()
                .id(customer.getId())
                .email(customer.getEmail())
                .phoneNumber(customer.getPhoneNumber())
                .address(addressMapper.convertAddressToAddressResponse(customer.getAddress()))
                .build();
    }

    public DeletedCustomerResponse converCustomerToDeletedCustomerResponse(Customer customer) {
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
}
