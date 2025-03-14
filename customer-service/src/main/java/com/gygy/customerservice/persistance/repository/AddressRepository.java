package com.gygy.customerservice.persistance.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gygy.customerservice.domain.entity.Address;

public interface AddressRepository extends JpaRepository<Address, UUID> {
} 