package com.loginhgco.auth_service.repositories;

import com.loginhgco.auth_service.models.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.Optional;


public interface ServiceRepository extends JpaRepository<Service, Long> {

    @Query("SELECT s FROM Service s WHERE s.nombre_servicio = :nombreServicio")
    Optional<Service> findByNombre_servicio(@Param("nombreServicio") String nombreServicio);
}