package com.gygy.customerservice.persistance.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gygy.customerservice.domain.entity.IndividualCustomer;

public interface IndividualCustomerRepository extends JpaRepository<IndividualCustomer, UUID> {
}