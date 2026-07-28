package com.loginhgco.auth_service.repositories;

import com.loginhgco.auth_service.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {

    // Métodos para buscar un usuario por sus credenciales o datos únicos
    Optional<User> findByUsername(String username);
    Optional<User> findByCi(String ci);

    // Métodos para verificar si ya existen antes de registrar un nuevo usuario
    boolean existsByUsername(String username);
    boolean existsByCi(String ci);
}

