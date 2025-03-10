package com.gygy.userservice.persistance;

import org.springframework.data.jpa.repository.JpaRepository;
import com.gygy.userservice.entity.Role;
import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, UUID> {

}
