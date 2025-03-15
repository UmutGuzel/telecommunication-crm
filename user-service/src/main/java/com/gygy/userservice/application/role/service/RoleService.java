package com.gygy.userservice.application.role.service;

import java.util.List;

import com.gygy.userservice.entity.Role;

public interface RoleService {

    Role getRoleByName(String name);

    List<Role> getRolesByNames(List<String> names);

    List<Role> getAllRoles();
}
