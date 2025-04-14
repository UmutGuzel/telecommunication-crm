package com.gygy.customersupportservice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.gygy.customersupportservice.domain.TicketResponse;

import java.util.List;
import java.util.UUID;

@Repository
public interface TicketResponseRepository extends JpaRepository<TicketResponse, UUID> {
    List<TicketResponse> findByTicketId(UUID ticketId);

    List<TicketResponse> findByUserId(UUID userId);

    Page<TicketResponse> findByTicketId(UUID ticketId, Pageable pageable);

    Page<TicketResponse> findByUserId(UUID userId, Pageable pageable);

    @Query("SELECT r FROM TicketResponse r JOIN FETCH r.ticket WHERE r.id = :id")
    TicketResponse findByIdWithTicket(@Param("id") UUID id);

    @Query(value = "SELECT * FROM ticket_responses WHERE ticket_id = :ticketId ORDER BY created_at DESC", nativeQuery = true)
    List<TicketResponse> findLatestResponsesByTicketId(@Param("ticketId") UUID ticketId, Pageable pageable);
}