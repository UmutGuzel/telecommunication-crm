package com.gygy.customerservice.infrastructure.outbox;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OutboxRepository extends JpaRepository<OutboxEntity, UUID> {
    List<OutboxEntity> findByStatusOrderByCreatedAtAsc(OutboxStatus status);
    List<OutboxEntity> findByStatus(OutboxStatus status);
    Optional<OutboxEntity> findByAggregateId(String aggregateId);
} 