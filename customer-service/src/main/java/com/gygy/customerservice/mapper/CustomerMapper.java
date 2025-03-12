package com.gygy.customerservice.mapper;

import com.gygy.customerservice.dto.CreateCustomerDTO;
import com.gygy.customerservice.dto.CustomerDetailsDTO;
import com.gygy.customerservice.dto.ListCustomerDTO;
import com.gygy.customerservice.dto.UpdateCustomerDTO;
import com.gygy.customerservice.entity.Customer;

public class CustomerMapper {

    public static Customer toEntity(CreateCustomerDTO dto) {
        Customer customer = new Customer();
        customer.setEmail(dto.getEmail());
        customer.setPhoneNumber(dto.getPhoneNumber());
        customer.setStatus(dto.getStatus());
        // Assuming address is set elsewhere
        return customer;
    }

    public static Customer toEntity(UpdateCustomerDTO dto, Customer customer) {
        customer.setEmail(dto.getEmail());
        customer.setPhoneNumber(dto.getPhoneNumber());
        customer.setStatus(dto.getStatus());
        // Assuming address is set elsewhere
        return customer;
    }

    public static CustomerDetailsDTO toDetailsDTO(Customer customer) {
        CustomerDetailsDTO dto = new CustomerDetailsDTO();
        dto.setId(customer.getId());
        dto.setEmail(customer.getEmail());
        dto.setPhoneNumber(customer.getPhoneNumber());
        dto.setStatus(customer.getStatus());
        dto.setAddressId(customer.getAddress().getId());
        return dto;
    }

    public static ListCustomerDTO toListDTO(Customer customer) {
        ListCustomerDTO dto = new ListCustomerDTO();
        dto.setId(customer.getId());
        dto.setEmail(customer.getEmail());
        dto.setPhoneNumber(customer.getPhoneNumber());
        dto.setStatus(customer.getStatus());
        return dto;
    }
} 