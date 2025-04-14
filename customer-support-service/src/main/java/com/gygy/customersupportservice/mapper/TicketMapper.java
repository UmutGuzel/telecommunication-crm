package com.gygy.customersupportservice.mapper;

import com.gygy.customersupportservice.domain.Ticket;
import com.gygy.customersupportservice.dto.TicketDTO;
import com.gygy.customersupportservice.dto.TicketReturnDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TicketMapper {

    public TicketReturnDTO toDTO(Ticket ticket) {
        if (ticket == null) {
            return null;
        }

        return TicketReturnDTO.builder()
                .title(ticket.getTitle())
                .description(ticket.getDescription())
                .type(ticket.getType())
                .customerId(ticket.getCustomerId())
                .status(ticket.getStatus() != null ? ticket.getStatus().getStatus() : null)
                .createdAt(ticket.getCreatedAt())
                .build();
    }

    public Ticket toEntity(TicketDTO dto) {
        if (dto == null) {
            return null;
        }

        Ticket ticket = new Ticket();
        ticket.setTitle(dto.getTitle());
        ticket.setDescription(dto.getDescription());
        ticket.setType(dto.getType());
        ticket.setCustomerId(dto.getCustomerId());
        ticket.setCreatedAt(LocalDateTime.now());
        ticket.setUpdatedAt(LocalDateTime.now());

        return ticket;
    }

    public List<TicketReturnDTO> toDTOList(List<Ticket> tickets) {
        if (tickets == null) {
            return null;
        }

        return tickets.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}