package com.loginhgco.auth_service.repositories;

import com.loginhgco.auth_service.models.ServiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ServiceRepository extends JpaRepository<ServiceEntity, Long> {

    @Query("SELECT s FROM ServiceEntity s WHERE s.nombreServicio = :nombreServicio")
    Optional<ServiceEntity> findByNombreServicio(@Param("nombreServicio") String nombreServicio);
}