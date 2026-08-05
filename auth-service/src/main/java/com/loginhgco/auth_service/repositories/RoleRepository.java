package com.loginhgco.auth_service.repositories;

import com.loginhgco.auth_service.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.Optional;


public interface RoleRepository extends JpaRepository<Role, Long> {

    @Query("SELECT r FROM Role r WHERE r.nombre_rol = :nombreRol")
    Optional<Role> findByNombre_rol(@Param("nombreRol") String nombreRol);
}