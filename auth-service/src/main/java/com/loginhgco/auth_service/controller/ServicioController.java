package com.loginhgco.auth_service.controller;

import com.loginhgco.auth_service.models.ServiceEntity; // Ajusta según tu entidad Servicio
import com.loginhgco.auth_service.repositories.ServiceRepository; // Ajusta tu repositorio
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/servicios")
@RequiredArgsConstructor
public class ServicioController {

    private final ServiceRepository serviceRepository;

    @GetMapping
    public ResponseEntity<List<ServiceEntity>> getAllServicios() {
        return ResponseEntity.ok(serviceRepository.findAll());
    }
}