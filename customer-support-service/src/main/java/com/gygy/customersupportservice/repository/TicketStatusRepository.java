package com.gygy.customersupportservice.repository;

import com.gygy.customersupportservice.domain.TicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TicketStatusRepository extends JpaRepository<TicketStatus, UUID> {
    Optional<TicketStatus> findByStatus(String status);
}