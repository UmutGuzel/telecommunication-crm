package com.gygy.customerservice.persistance.repository;

import java.util.Optional;
import java.util.UUID;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.gygy.customerservice.domain.entity.CorporateCustomer;
import com.gygy.customerservice.application.corporateCustomer.query.GetListCorporateCustomerItemDto;

@Repository
public interface CorporateCustomerRepository extends JpaRepository<CorporateCustomer, UUID> {
    Optional<CorporateCustomer> findByTaxNumber(String taxNumber);

    @Query("SELECT new com.gygy.customerservice.application.corporateCustomer.query.GetListCorporateCustomerItemDto(" +
           "c.id, c.email, c.phoneNumber, c.companyName, c.contactPersonName, c.contactPersonSurname) " +
           "FROM CorporateCustomer c")
    List<GetListCorporateCustomerItemDto> findAllAsDto();
}