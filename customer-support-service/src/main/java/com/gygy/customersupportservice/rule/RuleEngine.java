package com.gygy.customersupportservice.rule;

import com.gygy.customersupportservice.domain.Ticket;
import com.gygy.customersupportservice.domain.TicketResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * RuleEngine coordinates application of business rules to various domain
 * entities
 */
@Component
@RequiredArgsConstructor
public class RuleEngine {
    private final TicketRules ticketRules;
    private final TicketResponseRules responseRules;

    public Ticket processNewTicket(Ticket ticket) {
        validateTicket(ticket);
        return ticketRules.applyNewTicketRules(ticket);
    }

    public Ticket processTicketStatusChange(Ticket ticket, String status) {
        ticketRules.validateTicketExists(ticket);
        return ticketRules.applyStatusChangeRules(ticket, status);
    }

    public TicketResponse processNewResponse(TicketResponse response) {
        responseRules.validateResponse(response);
        return responseRules.applyNewResponseRules(response);
    }

    private void validateTicket(Ticket ticket) {
        if (ticket == null) {
            throw new IllegalArgumentException("Ticket cannot be null");
        }

        if (ticket.getTitle() == null || ticket.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Ticket title cannot be empty");
        }

        if (ticket.getDescription() == null || ticket.getDescription().trim().isEmpty()) {
            throw new IllegalArgumentException("Ticket description cannot be empty");
        }

        if (ticket.getType() == null || ticket.getType().trim().isEmpty()) {
            throw new IllegalArgumentException("Ticket type cannot be empty");
        }

        if (ticket.getCustomerId() == null) {
            throw new IllegalArgumentException("Customer ID must be specified for a ticket");
        }

        if (ticket.getUserId() == null) {
            throw new IllegalArgumentException("User ID must be specified for a ticket");
        }
    }
}