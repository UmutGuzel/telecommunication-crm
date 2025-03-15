package com.gygy.userservice.persistance;

import org.springframework.data.jpa.repository.JpaRepository;
import com.gygy.userservice.entity.Role;
import java.util.UUID;
import java.util.Optional;
import java.util.List;

public interface RoleRepository extends JpaRepository<Role, UUID> {
    Optional<Role> findByName(String name);

    List<Role> findAllByNameIn(List<String> names);
}
