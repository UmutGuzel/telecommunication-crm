package com.gygy.analyticsservice.repository;

import com.gygy.analyticsservice.model.TicketStatusHistory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface TicketStatusHistoryRepository extends MongoRepository<TicketStatusHistory, String> {

    List<TicketStatusHistory> findByTicketIdOrderByChangedAtAsc(UUID ticketId);

    List<TicketStatusHistory> findByChangedAtBetween(LocalDateTime start, LocalDateTime end);

    List<TicketStatusHistory> findByNewStatus(String status);

    List<TicketStatusHistory> findByPreviousStatus(String status);
}