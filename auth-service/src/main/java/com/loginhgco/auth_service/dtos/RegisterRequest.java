package com.loginhgco.auth_service.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class RegisterRequest {
    @NotBlank(message = "La cedula es obligatoria")
    private String ci;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    private String apellido;

    @NotBlank(message = "El username es obligatorio")
    private String username;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String password;

    private String descripcion; //  opcional por eso no tiene NotBlank

    @NotEmpty(message = "Debe asignar al menos un rol al usuario")
    private Set<String> roles; // Nombre de roles a asignar al usuario ej ADMIN , USUARIO, MEDICO etc.

    @NotEmpty(message = "Debe asignar al menos un servicio al usuario")
    private Set<String> servicios; // Nombre de servicios a asignar ej: TIC, NUTRICION , QUIROFANO.
}
