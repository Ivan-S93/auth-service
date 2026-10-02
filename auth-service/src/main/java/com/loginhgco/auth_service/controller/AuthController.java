package com.loginhgco.auth_service.controller;

import com.loginhgco.auth_service.dtos.AuthResponse;
import com.loginhgco.auth_service.dtos.LoginRequest;
import com.loginhgco.auth_service.dtos.RegisterRequest;
import com.loginhgco.auth_service.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")  // Ruta base para todas las solicitudes de autenticación
@RequiredArgsConstructor
public class AuthController {
    
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        try {
            AuthResponse response = authService.login(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.out.println("❌ ERROR EN LOGIN: " + e.getMessage());
            e.printStackTrace(); // Imprime toda la pila del error en la consola de Spring Boot
            return ResponseEntity.status(400).body(Map.of("message", e.getMessage()));
        }
    }

    @PostMapping("/register")  // Ruta para el registro de nuevos usuarios
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse response = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
