package com.gygy.analyticsservice.repository;

import com.gygy.analyticsservice.model.TicketResponseMetric;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TicketResponseMetricRepository extends MongoRepository<TicketResponseMetric, String> {

    List<TicketResponseMetric> findByTicketIdOrderByResponseTimestampAsc(UUID ticketId);

    Optional<TicketResponseMetric> findByResponseId(UUID responseId);

    List<TicketResponseMetric> findByResponseTimestampBetween(LocalDateTime start, LocalDateTime end);

    List<TicketResponseMetric> findByResponseNumber(int responseNumber);
}