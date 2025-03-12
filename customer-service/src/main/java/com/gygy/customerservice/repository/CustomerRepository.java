package com.gygy.customerservice.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gygy.customerservice.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, UUID> {
} 