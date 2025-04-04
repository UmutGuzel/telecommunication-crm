package com.gygy.customersupportservice.rule;

import com.gygy.customersupportservice.domain.Ticket;
import com.gygy.customersupportservice.domain.TicketResponse;
import com.gygy.customersupportservice.repository.TicketRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * TicketResponseRules encapsulates business rules for ticket responses
 */
@Component
@RequiredArgsConstructor
public class TicketResponseRules {
    private final TicketRepository ticketRepository;

    public TicketResponse applyNewResponseRules(TicketResponse response) {
        if (response.getTicket() == null || response.getTicket().getId() == null) {
            throw new IllegalArgumentException("Ticket must be specified for a response");
        }

        Ticket ticket = ticketRepository.findById(response.getTicket().getId())
                .orElseThrow(
                        () -> new EntityNotFoundException("Ticket not found with id: " + response.getTicket().getId()));

        response.setTicket(ticket);
        response.setCreatedAt(LocalDateTime.now());

        return response;
    }

    public void validateResponse(TicketResponse response) {
        if (response == null) {
            throw new IllegalArgumentException("Response cannot be null");
        }

        if (response.getMessage() == null || response.getMessage().trim().isEmpty()) {
            throw new IllegalArgumentException("Response message cannot be empty");
        }

        if (response.getUserId() == null) {
            throw new IllegalArgumentException("User ID must be specified for a response");
        }
    }
}