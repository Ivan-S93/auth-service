package com.loginhgco.auth_service.repositories;

import com.loginhgco.auth_service.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
    Optional<User> findByCi(String ci);

    boolean existsByUsername(String username);
    boolean existsByCi(String ci);
}
