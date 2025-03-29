package com.gygy.userservice.persistance;

import org.springframework.data.jpa.repository.JpaRepository;
import com.gygy.userservice.entity.User;
import java.util.UUID;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);

    Optional<User> findByResetToken(String resetToken);

    Optional<User> findByActivationToken(String activationToken);
}
