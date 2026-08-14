package com.loginhgco.auth_service.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder // permite construir el objeto con sintaxis fluida AuthResponse.builder()
@AllArgsConstructor // Requerido por @Builder para que lombok pueda generar el builder correctamente
@NoArgsConstructor

public class AuthResponse {
    private Long id;

    private String token; // token JWT, se completa cuando implementamos seguridad
    private String type; // Fijo en Bearer, ayuda al frontend a armar el header Authorizacion

    private String username;
    private String nombre;
    private String apellido;

    private Set<String> roles;
    private Set<String> servicios;

    private String message; // Mensaje "Login exitoso" o "usuario registrado con exito"
}
