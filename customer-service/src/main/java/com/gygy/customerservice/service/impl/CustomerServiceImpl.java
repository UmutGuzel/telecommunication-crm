package com.gygy.customerservice.service.impl;

import com.gygy.customerservice.dto.CreateCustomerDTO;
import com.gygy.customerservice.dto.UpdateCustomerDTO;
import com.gygy.customerservice.dto.CustomerDetailsDTO;
import com.gygy.customerservice.dto.ListCustomerDTO;
import com.gygy.customerservice.entity.Customer;
import com.gygy.customerservice.mapper.CustomerMapper;
import com.gygy.customerservice.repository.CustomerRepository;
import com.gygy.customerservice.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public CustomerDetailsDTO createCustomer(CreateCustomerDTO createCustomerDTO) {
        Customer customer = CustomerMapper.toEntity(createCustomerDTO);
        customer = customerRepository.save(customer);
        return CustomerMapper.toDetailsDTO(customer);
    }

    @Override
    public CustomerDetailsDTO getCustomerById(UUID id) {
        Customer customer = customerRepository.findById(id).orElseThrow();
        return CustomerMapper.toDetailsDTO(customer);
    }

    @Override
    public List<ListCustomerDTO> getAllCustomers() {
        return customerRepository.findAll().stream()
                .map(CustomerMapper::toListDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CustomerDetailsDTO updateCustomer(UUID id, UpdateCustomerDTO updateCustomerDTO) {
        Customer customer = customerRepository.findById(id).orElseThrow();
        CustomerMapper.toEntity(updateCustomerDTO, customer);
        customer = customerRepository.save(customer);
        return CustomerMapper.toDetailsDTO(customer);
    }

    @Override
    public void deleteCustomer(UUID id) {
        customerRepository.deleteById(id);
    }
} 