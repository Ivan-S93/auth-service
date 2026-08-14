package com.loginhgco.auth_service.controller;

import com.loginhgco.auth_service.dtos.AuthResponse;
import com.loginhgco.auth_service.dtos.LoginRequest;
import com.loginhgco.auth_service.dtos.RegisterRequest;
import com.loginhgco.auth_service.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")  // Ruta base para todas las solicitudes de autenticación
@RequiredArgsConstructor
public class AuthController {
    
    private final AuthService authService;

    @PostMapping("/login")  // Ruta para el inicio de sesión
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")  // Ruta para el registro de nuevos usuarios
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse response = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
