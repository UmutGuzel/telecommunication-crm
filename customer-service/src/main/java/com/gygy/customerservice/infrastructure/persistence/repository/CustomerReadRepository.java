package com.gygy.customerservice.infrastructure.persistence.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.gygy.customerservice.domain.entity.CustomerReadEntity;
import com.gygy.customerservice.domain.enums.CustomerType;

@Repository
public interface CustomerReadRepository extends MongoRepository<CustomerReadEntity, UUID> {
    Optional<CustomerReadEntity> findByEmail(String email);
    Optional<CustomerReadEntity> findByPhoneNumberAndType(String phoneNumber, CustomerType type);

    @Query(value = "{}", fields = "{ '_id': 1, 'email': 1, 'phoneNumber': 1, 'type': 1}")
    List<CustomerReadEntity> findAllAsDto();
}