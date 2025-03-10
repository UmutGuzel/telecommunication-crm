package com.gygy.userservice.entity;

import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Data;
import java.util.UUID;
import java.util.List;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.JoinTable;
import jakarta.persistence.JoinColumn;

@Table(name = "users")
@Data
public class User {
    @Id
    private UUID id;
    private String Name;
    private String Surname;
    private String Email;
    private String Password;
    private String PhoneNumber;
    private String Address;
    private String City;
    private String Country;
    private LocalDate BirthDate;
    private LocalDateTime CreatedAt;
    private LocalDateTime UpdatedAt;
    @ManyToMany
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<Role> roles;
    @ManyToMany
    @JoinTable(name = "user_permission", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "permission_id"))
    private List<Permission> permissions;
}
