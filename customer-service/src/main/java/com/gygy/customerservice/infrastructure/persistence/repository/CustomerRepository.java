package com.gygy.customerservice.infrastructure.persistence.repository;

import com.gygy.customerservice.domain.entity.Customer;
import com.gygy.customerservice.domain.enums.CustomerType;
import com.gygy.customerservice.application.customer.query.GetListCustomerItemDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerRepository extends JpaRepository<Customer, UUID> {
    Optional<Customer> findByEmail(String email);
    Optional<Customer> findByPhoneNumberAndType(String phoneNumber, CustomerType type);

    @Query("SELECT new com.gygy.customerservice.application.customer.query.GetListCustomerItemDto(" +
           "c.id, c.email, c.phoneNumber, c.type) " +
           "FROM Customer c")
    List<GetListCustomerItemDto> findAllAsDto();
}