package com.gygy.customersupportservice.service;

import com.gygy.customersupportservice.domain.TicketStatus;
import com.gygy.customersupportservice.repository.TicketStatusRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatusInitializationService {
    private final TicketStatusRepository ticketStatusRepository;

    @PostConstruct
    @Transactional
    public void initializeStatuses() {
        log.info("Initializing default ticket statuses");

        List<TicketStatus> defaultStatuses = Arrays.asList(
                createStatus("OPEN", "Ticket is newly created and awaiting response"),
                createStatus("IN_PROGRESS", "Ticket is being worked on"),
                createStatus("WAITING_FOR_CUSTOMER", "Waiting for customer response"),
                createStatus("RESOLVED", "Issue has been resolved"),
                createStatus("CLOSED", "Ticket has been closed"));

        for (TicketStatus status : defaultStatuses) {
            if (!ticketStatusRepository.findByStatus(status.getStatus()).isPresent()) {
                ticketStatusRepository.save(status);
                log.info("Created status: {}", status.getStatus());
            }
        }
    }

    private TicketStatus createStatus(String statusName, String description) {
        TicketStatus status = new TicketStatus();
        status.setStatus(statusName);
        status.setDescription(description);
        return status;
    }
}