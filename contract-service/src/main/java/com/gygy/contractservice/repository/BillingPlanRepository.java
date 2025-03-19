package com.gygy.contractservice.repository;

import com.gygy.contractservice.entity.BillingPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface BillingPlanRepository extends JpaRepository<BillingPlan, UUID> {
    boolean existsByName(String name);
    boolean existsByNameAndIdNot(String name, UUID id);
}
