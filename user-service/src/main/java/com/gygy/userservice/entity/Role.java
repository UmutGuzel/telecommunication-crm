package com.gygy.userservice.entity;

import jakarta.persistence.Table;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.List;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.CascadeType;
import jakarta.persistence.JoinTable;
import jakarta.persistence.JoinColumn;
import lombok.Data;

@Table(name = "roles")
@Data
public class Role {
    @Id
    private UUID id;
    private String Name;
    private String Description;
    private LocalDateTime CreatedAt;
    private LocalDateTime UpdatedAt;
    @ManyToMany
    @JoinTable(name = "role_permission", joinColumns = @JoinColumn(name = "role_id"), inverseJoinColumns = @JoinColumn(name = "permission_id"))
    private List<Permission> permissions;
}
