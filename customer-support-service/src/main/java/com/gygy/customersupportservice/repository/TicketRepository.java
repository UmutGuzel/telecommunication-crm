package com.gygy.customersupportservice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.gygy.customersupportservice.domain.Ticket;

import java.util.List;
import java.util.UUID;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, UUID> {
    List<Ticket> findByCustomerId(Long customerId);

    List<Ticket> findByUserId(Long userId);

    Page<Ticket> findByCustomerId(Long customerId, Pageable pageable);

    Page<Ticket> findByUserId(Long userId, Pageable pageable);

    @Query(value = "SELECT t.* FROM tickets t " +
            "INNER JOIN ticket_statuses ts ON t.status_id = ts.id " +
            "WHERE ts.status = :status", nativeQuery = true)
    List<Ticket> findByStatusName(@Param("status") String status);

    @Query("SELECT t FROM Ticket t LEFT JOIN FETCH t.status WHERE t.id = :id")
    Ticket findByIdWithStatus(@Param("id") UUID id);

    Page<Ticket> findByType(String type, Pageable pageable);
}