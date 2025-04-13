package com.gygy.analyticsservice.repository;

import com.gygy.analyticsservice.model.TicketAnalytics;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TicketAnalyticsRepository extends MongoRepository<TicketAnalytics, String> {

    Optional<TicketAnalytics> findByTicketId(UUID ticketId);

    List<TicketAnalytics> findByCustomerId(Long customerId);

    List<TicketAnalytics> findByUserIdOrderByCreatedAtDesc(Long userId);

    List<TicketAnalytics> findByTicketTypeOrderByCreatedAtDesc(String ticketType);

    List<TicketAnalytics> findByCurrentStatus(String status);

    List<TicketAnalytics> findByResolvedTrue();

    List<TicketAnalytics> findByResolvedFalse();

    List<TicketAnalytics> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
}