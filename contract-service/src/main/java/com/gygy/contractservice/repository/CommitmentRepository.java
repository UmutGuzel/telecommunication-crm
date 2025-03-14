package com.gygy.contractservice.repository;

import com.gygy.contractservice.entity.Commitment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface CommitmentRepository extends JpaRepository<Commitment, UUID> {
}
