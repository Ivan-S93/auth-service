// Este archivo definira la tabla de llos usuarios y la relacion de muchos a muchos con la tabla de roles
// asi un usuario puede tener varios roles si necesita.

package com.loginhgco.auth_service.models;

import jakarta.persistence.*;
import lombok.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "usuarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 50)
    private String ci; // cedula de identidad

    @Column(unique = true, nullable = false, length = 100)
    private String nombre;

    @Column(unique = true, nullable = false, length = 100)
    private String apellido;

    @Column(unique = true, nullable = false, length = 50)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private boolean active = true; // Estado: true (Activo) / false (Inactivo)

    @Column(length = 255)
    private String descripcion;

    // Relacion muchos a muchos con roles
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "usario_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    // Relacion muchos a muchos con servicios ( quirofano, nutricion, clinia medica, urgencias, etc)
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "usuarios_servicios",
            joinColumns = @JoinColumn(name = "usuario_id"),
            inverseJoinColumns = @JoinColumn(name = "servicio_id")
    )
    private Set<Service> servicios = new HashSet<>();



     

    


}

