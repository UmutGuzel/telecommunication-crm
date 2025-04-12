package com.gygy.customersupportservice.mapper;

import com.gygy.customersupportservice.domain.Ticket;
import com.gygy.customersupportservice.domain.TicketStatus;
import com.gygy.customersupportservice.dto.TicketDTO;
import com.gygy.customersupportservice.repository.TicketStatusRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TicketMapper {
    private final TicketStatusRepository ticketStatusRepository;

    public TicketDTO toDTO(Ticket ticket) {
        if (ticket == null) {
            return null;
        }

        return TicketDTO.builder()
                .id(ticket.getId())
                .title(ticket.getTitle())
                .description(ticket.getDescription())
                .type(ticket.getType())
                .customerId(ticket.getCustomerId())
                .userId(ticket.getUserId())
                .status(ticket.getStatus() != null ? ticket.getStatus().getStatus() : null)
                .statusDescription(ticket.getStatus() != null ? ticket.getStatus().getDescription() : null)
                .createdAt(ticket.getCreatedAt())
                .updatedAt(ticket.getUpdatedAt())
                .build();
    }

    public Ticket toEntity(TicketDTO dto) {
        if (dto == null) {
            return null;
        }

        Ticket ticket = new Ticket();
        // ticket.setId(dto.getId());
        ticket.setTitle(dto.getTitle());
        ticket.setDescription(dto.getDescription());
        ticket.setType(dto.getType());
        ticket.setCustomerId(dto.getCustomerId());
        ticket.setUserId(dto.getUserId());

        if (dto.getStatus() != null) {
            TicketStatus status = ticketStatusRepository.findByStatus(dto.getStatus())
                    .orElseThrow(() -> new EntityNotFoundException("Status not found: " + dto.getStatus()));
            ticket.setStatus(status);
        }

        ticket.setCreatedAt(dto.getCreatedAt());
        ticket.setUpdatedAt(dto.getUpdatedAt());

        return ticket;
    }

    public List<TicketDTO> toDTOList(List<Ticket> tickets) {
        if (tickets == null) {
            return null;
        }

        return tickets.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}