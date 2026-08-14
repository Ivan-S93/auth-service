package com.loginhgco.auth_service.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.Set;

@Entity
@Table(name = "servicios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre_servicio", unique = true, nullable = false)
    private String nombreServicio;

    @ManyToMany(mappedBy = "servicios")
    @JsonIgnore
    private Set<User> usuarios;
}