package com.gygy.contractservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.sql.Date;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name="discount")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Discount {
    @Id
    @UuidGenerator
    private UUID id;

    @Column(name="discount_type")
    private String discountType;
    private double amount;
    private double percentage;

    @Column(name="start_date")
    private LocalDate startDate;
    @Column(name="end_date")
    private LocalDate endDate;
    @Column(name="created_at")
    private LocalDate createdAt;
    @Column(name="updated_at")
    private LocalDate updatedAt;

}
