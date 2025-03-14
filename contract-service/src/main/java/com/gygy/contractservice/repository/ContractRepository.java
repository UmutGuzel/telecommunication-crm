package com.gygy.contractservice.repository;

import com.gygy.contractservice.entity.Contract;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface ContractRepository extends JpaRepository<Contract, UUID> {
}
