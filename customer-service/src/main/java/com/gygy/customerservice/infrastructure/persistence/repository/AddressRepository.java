package com.gygy.customerservice.infrastructure.persistence.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gygy.customerservice.domain.entity.Address;

public interface AddressRepository extends JpaRepository<Address, UUID> {
    Optional<Address> findByStreetAndDistrictAndCityAndCountry(String street, String district, String city, String country);
} 