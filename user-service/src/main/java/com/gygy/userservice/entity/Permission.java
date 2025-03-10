package com.gygy.userservice.entity;

import jakarta.persistence.Table;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Data;
import java.util.List;
import jakarta.persistence.ManyToMany;

@Table(name = "permissions")
@Data
public class Permission {
    @Id
    private UUID id;
    private String name;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    @ManyToMany(mappedBy = "permissions")
    private List<Role> roles;
    @ManyToMany(mappedBy = "permissions")
    private List<User> users;
}
