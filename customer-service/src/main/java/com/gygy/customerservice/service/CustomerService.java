package com.gygy.customerservice.service;

import com.gygy.customerservice.dto.CreateCustomerDTO;
import com.gygy.customerservice.dto.UpdateCustomerDTO;
import com.gygy.customerservice.dto.CustomerDetailsDTO;
import com.gygy.customerservice.dto.ListCustomerDTO;
import java.util.List;
import java.util.UUID;

public interface CustomerService {
    CustomerDetailsDTO createCustomer(CreateCustomerDTO createCustomerDTO);
    CustomerDetailsDTO getCustomerById(UUID id);
    List<ListCustomerDTO> getAllCustomers();
    CustomerDetailsDTO updateCustomer(UUID id, UpdateCustomerDTO updateCustomerDTO);
    void deleteCustomer(UUID id);
} 