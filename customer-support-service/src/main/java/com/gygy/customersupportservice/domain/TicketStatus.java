package com.gygy.customersupportservice.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Table(name = "ticket_statuses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TicketStatus {
    @Id
    @UuidGenerator
    private UUID id;

    @Column(nullable = false, unique = true, length = 50)
    private String status;

    @Column(columnDefinition = "TEXT")
    private String description;
}