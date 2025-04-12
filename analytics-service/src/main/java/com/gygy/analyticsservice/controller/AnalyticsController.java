package com.gygy.analyticsservice.controller;

import com.gygy.analyticsservice.service.TicketAnalyticsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * REST controller for the analytics service.
 * Provides endpoints for tracking and retrieving analytics data.
 */
@RestController
@RequestMapping("/api/v1/analytics")
@RequiredArgsConstructor
@Slf4j
public class AnalyticsController {
    private final TicketAnalyticsService ticketAnalyticsService;

    @GetMapping("/tickets/status")
    public ResponseEntity<Map<String, Object>> getTicketStatusData() {
        log.info("Retrieving ticket status analytics data");
        return ResponseEntity.ok(ticketAnalyticsService.getTicketStatusData());
    }
}