package com.loginhgco.auth_service.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data                  // Genera getters, setters, equals, hashCode y toString automáticamente
@AllArgsConstructor    // Genera un constructor con todos los campos (username, password)
@NoArgsConstructor     // Genera un constructor vacío, obligatorio para que Jackson pueda deserializar el JSON
public class LoginRequest {

    @NotBlank(message = "El username es obligatorio")
    private String username;

    @NotBlank(message = "La contraseña es obligatoria")
    private String password;
}