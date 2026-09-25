package com.loginhgco.auth_service.controller;

import com.loginhgco.auth_service.models.Role; // Ajusta según tu entidad Role/Rol
import com.loginhgco.auth_service.repositories.RoleRepository; // Ajusta tu repositorio
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleRepository roleRepository;

    @GetMapping
    public ResponseEntity<List<Role>> getAllRoles() {
        return ResponseEntity.ok(roleRepository.findAll());
    }
}