package com.gygy.customerservice.infrastructure.persistence.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.gygy.customerservice.domain.entity.IndividualCustomer;
import com.gygy.customerservice.application.individualCustomer.query.GetListIndividualCustomerItemDto;

@Repository
public interface IndividualCustomerRepository extends JpaRepository<IndividualCustomer, UUID> {
    Optional<IndividualCustomer> findByIdentityNumberHash(String identityNumberHash);
    
    IndividualCustomer findByEmail(String email);
    
    IndividualCustomer findByPhoneNumber(String phoneNumber);

    @Query("SELECT new com.gygy.customerservice.application.individualCustomer.query.GetListIndividualCustomerItemDto(" +
           "c.id, c.email, c.phoneNumber, c.name, c.surname, c.gender, c.birthDate) " +
           "FROM IndividualCustomer c")
    List<GetListIndividualCustomerItemDto> findAllAsDto();
}