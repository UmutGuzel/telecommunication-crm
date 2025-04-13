package com.gygy.analyticsservice.service;

import com.gygy.analyticsservice.model.TicketAnalytics;
import com.gygy.analyticsservice.model.TicketResponseMetric;
import com.gygy.analyticsservice.model.TicketStatusHistory;
import com.gygy.analyticsservice.repository.TicketAnalyticsRepository;
import com.gygy.analyticsservice.repository.TicketResponseMetricRepository;
import com.gygy.analyticsservice.repository.TicketStatusHistoryRepository;
import com.gygy.common.events.customersupportservice.TicketCreatedEvent;
import com.gygy.common.events.customersupportservice.TicketResponseEvent;
import com.gygy.common.events.customersupportservice.TicketStatusChangeEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TicketAnalyticsService {

    private final TicketAnalyticsRepository ticketAnalyticsRepository;
    private final TicketStatusHistoryRepository ticketStatusHistoryRepository;
    private final TicketResponseMetricRepository ticketResponseMetricRepository;

    public void processTicketCreatedEvent(TicketCreatedEvent event) {
        log.info("Processing ticket created event: {}", event);

        // Create and save initial ticket analytics record
        TicketAnalytics ticketAnalytics = TicketAnalytics.builder()
                .ticketId(event.getTicketId())
                .customerId(event.getCustomerId())
                .userId(event.getUserId())
                .ticketTitle(event.getTicketTitle())
                .ticketType(event.getTicketType())
                .createdAt(event.getTimestamp())
                .responseCount(0)
                .statusChangeCount(0)
                .currentStatus("OPEN")
                .resolved(false)
                .build();

        try {
            TicketAnalytics savedTicketAnalytics = ticketAnalyticsRepository.save(ticketAnalytics);
            log.info("Successfully saved ticket analytics: {}", savedTicketAnalytics);
        } catch (Exception e) {
            log.error("Failed to save ticket analytics to MongoDB", e);
        }

        // Create initial status history entry
        TicketStatusHistory statusHistory = TicketStatusHistory.builder()
                .ticketId(event.getTicketId())
                .previousStatus(null) // No previous status for new ticket
                .newStatus("OPEN") // Assuming initial status is OPEN
                .changedAt(event.getTimestamp())
                .durationInPreviousStatusMinutes(0L) // No previous status
                .build();

        try {
            TicketStatusHistory savedStatusHistory = ticketStatusHistoryRepository.save(statusHistory);
            log.info("Successfully saved ticket status history: {}", savedStatusHistory);
        } catch (Exception e) {
            log.error("Failed to save ticket status history to MongoDB", e);
        }
    }

    public void processTicketResponseEvent(TicketResponseEvent event) {
        log.info("Processing ticket response event: {}", event);

        // Find the ticket analytics record
        Optional<TicketAnalytics> ticketAnalyticsOpt = ticketAnalyticsRepository.findByTicketId(event.getTicketId());

        if (ticketAnalyticsOpt.isPresent()) {
            TicketAnalytics ticketAnalytics = ticketAnalyticsOpt.get();
            LocalDateTime responseTime = event.getTimestamp();

            // Calculate response metrics
            int responseCount = ticketAnalytics.getResponseCount() + 1;
            LocalDateTime firstResponseAt = ticketAnalytics.getFirstResponseAt();

            if (firstResponseAt == null) {
                // This is the first response
                firstResponseAt = responseTime;

                // Calculate time to first response in minutes
                long timeToFirstResponse = Duration.between(ticketAnalytics.getCreatedAt(), responseTime).toMinutes();
                ticketAnalytics.setAverageResponseTimeMinutes(timeToFirstResponse);
            } else {
                // Calculate new average response time
                List<TicketResponseMetric> allResponses = ticketResponseMetricRepository
                        .findByTicketIdOrderByResponseTimestampAsc(event.getTicketId());

                long totalResponseTime = allResponses.stream()
                        .mapToLong(TicketResponseMetric::getResponseTimeMinutes)
                        .sum();

                // Add this response time
                LocalDateTime lastResponseOrCreation = ticketAnalytics.getLastResponseAt() != null
                        ? ticketAnalytics.getLastResponseAt()
                        : ticketAnalytics.getCreatedAt();
                long currentResponseTime = Duration.between(lastResponseOrCreation, responseTime).toMinutes();
                totalResponseTime += currentResponseTime;

                ticketAnalytics.setAverageResponseTimeMinutes(totalResponseTime / responseCount);
            }

            // Update ticket analytics
            ticketAnalytics.setResponseCount(responseCount);
            ticketAnalytics.setFirstResponseAt(firstResponseAt);
            ticketAnalytics.setLastResponseAt(responseTime);

            ticketAnalyticsRepository.save(ticketAnalytics);

            // Create response metric record
            TicketResponseMetric responseMetric = TicketResponseMetric.builder()
                    .ticketId(event.getTicketId())
                    .responseId(event.getResponseId())
                    .responseTimestamp(responseTime)
                    .responseNumber(responseCount)
                    .build();

            // Calculate response time
            LocalDateTime previousTimestamp = ticketAnalytics.getLastResponseAt() != null
                    ? ticketAnalytics.getLastResponseAt()
                    : ticketAnalytics.getCreatedAt();
            long responseTimeMinutes = Duration.between(previousTimestamp, responseTime).toMinutes();
            responseMetric.setResponseTimeMinutes(responseTimeMinutes);

            ticketResponseMetricRepository.save(responseMetric);
        } else {
            log.warn("Received response event for unknown ticket ID: {}", event.getTicketId());
        }
    }

    public void processTicketStatusChangeEvent(TicketStatusChangeEvent event) {
        log.info("Processing ticket status change event: {}", event);

        // Find the ticket analytics record
        Optional<TicketAnalytics> ticketAnalyticsOpt = ticketAnalyticsRepository.findByTicketId(event.getTicketId());

        if (ticketAnalyticsOpt.isPresent()) {
            TicketAnalytics ticketAnalytics = ticketAnalyticsOpt.get();
            String previousStatus = event.getPreviousStatus();
            String newStatus = event.getNewStatus();
            LocalDateTime statusChangeTime = event.getTimestamp();

            // Update status change count
            ticketAnalytics.setStatusChangeCount(ticketAnalytics.getStatusChangeCount() + 1);
            ticketAnalytics.setCurrentStatus(newStatus);
            ticketAnalytics.setLastStatusChangeAt(statusChangeTime);

            // Check if ticket is resolved
            if (newStatus.equals("RESOLVED") || newStatus.equals("CLOSED")) {
                ticketAnalytics.setResolved(true);
                ticketAnalytics.setResolvedAt(statusChangeTime);

                // Calculate time to resolution
                long resolutionTimeMinutes = Duration.between(ticketAnalytics.getCreatedAt(), statusChangeTime)
                        .toMinutes();
                ticketAnalytics.setTimeToResolutionMinutes(resolutionTimeMinutes);
            }

            ticketAnalyticsRepository.save(ticketAnalytics);

            // Create status history record
            TicketStatusHistory statusHistory = TicketStatusHistory.builder()
                    .ticketId(event.getTicketId())
                    .previousStatus(previousStatus)
                    .newStatus(newStatus)
                    .changedAt(statusChangeTime)
                    .build();

            // Calculate how long the ticket was in the previous status
            if (previousStatus != null) {
                // Find when the ticket entered the previous status
                Optional<TicketStatusHistory> previousStatusHistoryOpt = ticketStatusHistoryRepository
                        .findByTicketIdOrderByChangedAtAsc(event.getTicketId())
                        .stream()
                        .filter(history -> history.getNewStatus().equals(previousStatus))
                        .findFirst();

                if (previousStatusHistoryOpt.isPresent()) {
                    LocalDateTime enteredPreviousStatusAt = previousStatusHistoryOpt.get().getChangedAt();
                    long durationInPreviousStatus = Duration.between(enteredPreviousStatusAt, statusChangeTime)
                            .toMinutes();
                    statusHistory.setDurationInPreviousStatusMinutes(durationInPreviousStatus);
                }
            }

            ticketStatusHistoryRepository.save(statusHistory);
        } else {
            log.warn("Received status change event for unknown ticket ID: {}", event.getTicketId());
        }
    }

    public Map<String, Object> getTicketStatusData() {
        log.info("Getting ticket status analytics data");

        log.info("Retrieving solved tickets");
        List<TicketAnalytics> solvedTickets = ticketAnalyticsRepository.findByResolvedTrue();
        log.info("Retrieving unsolved tickets");
        List<TicketAnalytics> unsolvedTickets = ticketAnalyticsRepository.findByResolvedFalse();

        Map<String, Object> response = new HashMap<>();
        response.put("solvedTicketsCount", solvedTickets.size());
        response.put("unsolvedTicketsCount", unsolvedTickets.size());
        response.put("solvedTickets", solvedTickets);
        response.put("unsolvedTickets", unsolvedTickets);

        return response;
    }
}