// Este archivo definira la tabla de los roles de los usuarios (administrador, medico , nutricionista etc)
// y sus permisos de acceso.

package com.loginhgco.auth_service.models;

import com.fasterxml.jackson.annotation.JsonIgnore; 
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false, unique = true)
    private String nombre_rol;

    @ManyToMany(mappedBy = "roles")
    @JsonIgnore 
    private Set<User> usuarios = new HashSet<>();
}
