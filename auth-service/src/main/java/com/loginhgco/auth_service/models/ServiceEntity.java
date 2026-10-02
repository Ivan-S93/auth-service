package com.loginhgco.auth_service.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "servicios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ServiceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "nombre_servicio", unique = true, nullable = false)
    private String nombreServicio;

    @ManyToMany(mappedBy = "servicios")
    @JsonIgnore
    private Set<User> usuarios = new HashSet<>();
}