package com.gygy.customerservice.infrastructure.persistence.repository;

import com.gygy.customerservice.domain.entity.CustomerReadEntity;
import com.gygy.customerservice.domain.enums.CustomerType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CorporateCustomerReadRepository extends MongoRepository<CustomerReadEntity, UUID> {
    @Query(value = "{ 'type': 'CORPORATE' }", fields = "{ '_id': 1, 'email': 1, 'phoneNumber': 1, 'companyName': 1, 'contactPersonName': 1, 'contactPersonSurname': 1}")
    List<CustomerReadEntity> findAllCorporateCustomers();
}
