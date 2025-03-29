package com.gygy.contractservice.repository;

import com.gygy.contractservice.entity.Commitment;
import com.gygy.contractservice.model.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface CommitmentRepository extends JpaRepository<Commitment, UUID> {
    boolean existsByName(String name);
    boolean existsByNameAndIdNot(String name, UUID id);
    boolean existsByContractDetail_CustomerIdAndStatus(UUID customerId, Status status);
}
