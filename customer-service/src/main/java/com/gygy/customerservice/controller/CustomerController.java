package com.gygy.customerservice.controller;

import com.gygy.customerservice.dto.CreateCustomerDTO;
import com.gygy.customerservice.dto.UpdateCustomerDTO;
import com.gygy.customerservice.dto.CustomerDetailsDTO;
import com.gygy.customerservice.dto.ListCustomerDTO;
import com.gygy.customerservice.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @PostMapping
    public ResponseEntity<CustomerDetailsDTO> createCustomer(@RequestBody CreateCustomerDTO createCustomerDTO) {
        CustomerDetailsDTO createdCustomer = customerService.createCustomer(createCustomerDTO);
        return ResponseEntity.ok(createdCustomer);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDetailsDTO> getCustomerById(@PathVariable UUID id) {
        CustomerDetailsDTO customer = customerService.getCustomerById(id);
        return ResponseEntity.ok(customer);
    }

    @GetMapping
    public ResponseEntity<List<ListCustomerDTO>> getAllCustomers() {
        List<ListCustomerDTO> customers = customerService.getAllCustomers();
        return ResponseEntity.ok(customers);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerDetailsDTO> updateCustomer(@PathVariable UUID id, @RequestBody UpdateCustomerDTO updateCustomerDTO) {
        CustomerDetailsDTO updatedCustomer = customerService.updateCustomer(id, updateCustomerDTO);
        return ResponseEntity.ok(updatedCustomer);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable UUID id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }
} 