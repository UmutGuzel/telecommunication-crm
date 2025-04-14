package com.gygy.customerservice.infrastructure.persistence.repository;

import com.gygy.customerservice.domain.entity.CustomerReadEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface IndividualCustomerReadRepository extends MongoRepository<CustomerReadEntity, UUID> {
    @Query(value = "{ 'type': 'INDIVIDUAL' }", fields = "{ '_id': 1, 'email': 1, 'phoneNumber': 1, 'name': 1, 'surname': 1, 'gender': 1, 'birthDate': 1}")
    List<CustomerReadEntity> findAllIndividualCustomers();
}
