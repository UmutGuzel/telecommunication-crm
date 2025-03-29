package com.gygy.userservice.entity;

import jakarta.persistence.Table;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Data;
import java.util.List;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Entity;
import lombok.Builder;
import jakarta.persistence.Column;

@Entity
@Table(name = "permissions")
@Data
@Builder
public class Permission {
    @Id
    private UUID id;
    private String name;
    private String description;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    @ManyToMany(mappedBy = "permissions")
    private List<Role> roles;
    @ManyToMany(mappedBy = "permissions")
    private List<User> users;
}
