// Este archivo definira la tabla de los roles de los usuarios (administrador, medico , nutricionista etc)
// y sus permisos de acceso.

package com.loginhgco.auth_service.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 50)
    private String nombre_rol;

}
