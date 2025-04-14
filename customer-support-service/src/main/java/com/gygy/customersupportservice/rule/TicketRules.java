package com.gygy.customersupportservice.rule;

import com.gygy.customersupportservice.domain.Ticket;
import com.gygy.customersupportservice.domain.TicketStatus;
import com.gygy.customersupportservice.repository.TicketStatusRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * TicketRules encapsulates business rules for ticket management
 */
@Component
@RequiredArgsConstructor
public class TicketRules {
    private final TicketStatusRepository ticketStatusRepository;

    public Ticket applyNewTicketRules(Ticket ticket) {

        ticket.setCreatedAt(LocalDateTime.now());
        return ticket;
    }

    public Ticket applyStatusChangeRules(Ticket ticket, String statusName) {
        TicketStatus newStatus = ticketStatusRepository.findByStatus(statusName)
                .orElseThrow(() -> new EntityNotFoundException("Ticket status not found: " + statusName));
        ticket.setStatus(newStatus);
        ticket.setUpdatedAt(LocalDateTime.now());
        return ticket;
    }

    public void validateTicket(Ticket ticket) {
        if (ticket == null) {
            throw new EntityNotFoundException("Ticket not found");
        }

        if (ticket.getId() == null) {
            throw new EntityNotFoundException("Ticket ID cannot be null");
        }
    }
}