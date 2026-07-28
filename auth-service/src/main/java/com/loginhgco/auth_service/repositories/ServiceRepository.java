package com.loginhgco.auth_service.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import com.loginhgco.auth_service.models.Service;
import java.util.Optional;


public interface ServiceRepository extends JpaRepository<Service, Long> {
    // Método para buscar un servicio por su nombre (ej: "QUIROFANO")
    Optional<Service> findByNombreServicio(String nombreServicio);
}
