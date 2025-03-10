package com.gygy.userservice.persistance;

import org.springframework.data.jpa.repository.JpaRepository;
import com.gygy.userservice.entity.User;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

}
