package com.gygy.contractservice.repository;

import com.gygy.contractservice.entity.ContractDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ContractDetailRepository extends JpaRepository<ContractDetail, UUID> {
}
