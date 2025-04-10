package com.gygy.customerservice.persistance.repository;

import java.util.Optional;
import java.util.UUID;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gygy.customerservice.domain.entity.Customer;
import com.gygy.customerservice.domain.enums.CustomerType;

public interface CustomerRepository extends JpaRepository<Customer, UUID> {
    Optional<Customer> findByEmail(String email);
    Optional<Customer> findByPhoneNumber(String phoneNumber);
    List<Customer> findByPhoneNumberAndType(String phoneNumber, CustomerType type);
}