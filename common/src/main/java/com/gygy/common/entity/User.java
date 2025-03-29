package com.gygy.common.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private UUID id;
    private String email;
    private Set<Role> roles;
    private Set<Permission> permissions;
}