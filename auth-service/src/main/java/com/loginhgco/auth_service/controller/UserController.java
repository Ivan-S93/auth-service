package com.loginhgco.auth_service.controller;

import com.loginhgco.auth_service.models.User;
import com.loginhgco.auth_service.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;   // para guardar la contraseña encriptada en la b

    // GET /api/users -> Carga los usuarios en la tabla de React
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userRepository.findAll();
        return ResponseEntity.ok(users);
    }

    // POST /api/users -> Crear nuevo usuario (Exclusivo para Administradores)
    @PostMapping
    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    public ResponseEntity<?> createUser(@RequestBody User user) {
        // 1. Encriptar contraseña recibida en texto plano
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        // 2. Definir estado activo por defecto
        user.setActive(true);

        // 3. Guardar en base de datos PostgreSQL
        User savedUser = userRepository.save(user);
        return ResponseEntity.ok(savedUser);
    }

    // PATCH /api/users/{id}/status -> Activa o desactiva al usuario
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    public ResponseEntity<User> toggleUserStatus(
            @PathVariable Long id,
            @RequestBody Map<String, Boolean> statusUpdate) {
        
        return userRepository.findById(id)
                .map(user -> {
                    Boolean active = statusUpdate.get("active");
                    if (active != null) {
                        user.setActive(active);
                    }
                    User updatedUser = userRepository.save(user);
                    return ResponseEntity.ok(updatedUser);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}