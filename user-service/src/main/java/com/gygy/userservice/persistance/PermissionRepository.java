package com.gygy.userservice.persistance;

import org.springframework.data.jpa.repository.JpaRepository;
import com.gygy.userservice.entity.Permission;
import java.util.UUID;

public interface PermissionRepository extends JpaRepository<Permission, UUID> {

}
