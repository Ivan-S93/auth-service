package com.loginhgco.auth_service.service;

import com.loginhgco.auth_service.config.JwtUtils; // 👈 Asegúrate de tener importado JwtUtils
import com.loginhgco.auth_service.dtos.AuthResponse;
import com.loginhgco.auth_service.dtos.LoginRequest;
import com.loginhgco.auth_service.dtos.RegisterRequest;
import com.loginhgco.auth_service.models.Role;
import com.loginhgco.auth_service.models.ServiceEntity;
import com.loginhgco.auth_service.models.User;
import com.loginhgco.auth_service.repositories.RoleRepository;
import com.loginhgco.auth_service.repositories.ServiceRepository;
import com.loginhgco.auth_service.repositories.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ServiceRepository serviceRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils; // 👈 Inyección de JwtUtils

    public AuthResponse login(LoginRequest request) {

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario o contraseña incorrectos"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Usuario o contraseña incorrectos");
        }

        if (!user.isActive()) {
            throw new RuntimeException("El usuario está inactivo");
        }

        return buildAuthResponse(user, "Login exitoso");
    }

    public AuthResponse register(RegisterRequest request) {

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("El username ya está en uso");
        }

        if (userRepository.existsByCi(request.getCi())) {
            throw new RuntimeException("La cédula ya está registrada");
        }

        User user = new User();
        user.setCi(request.getCi());
        user.setNombre(request.getNombre());
        user.setApellido(request.getApellido());
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setDescripcion(request.getDescripcion());
        user.setActive(true);

        user.setRoles(resolveRoles(request.getRoles()));
        user.setServicios(resolveServicios(request.getServicios()));

        // 👈 2. CAMBIO CLAVE: Usar saveAndFlush para forzar el guardado en BD en Java 25
        User savedUser = userRepository.saveAndFlush(user);

        return buildAuthResponse(savedUser, "Usuario registrado con éxito");
    }

    // ---------- Helpers privados ----------

    private Set<Role> resolveRoles(Set<String> nombresRoles) {
        Set<Role> roles = new HashSet<>();

        if (nombresRoles == null || nombresRoles.isEmpty()) {
            Role rolUsuario = roleRepository.findByNombre_rol("ROL_USUARIO")
                    .orElseThrow(() -> new RuntimeException("Rol por defecto no encontrado"));
            roles.add(rolUsuario);
            return roles;
        }

        for (String nombreRol : nombresRoles) {
            Role role = roleRepository.findByNombre_rol(nombreRol)
                    .orElseThrow(() -> new RuntimeException("Rol no encontrado: " + nombreRol));
            roles.add(role);
        }
        return roles;
    }

    private Set<ServiceEntity> resolveServicios(Set<String> nombresServicios) {
        Set<ServiceEntity> servicios = new HashSet<>();

        if (nombresServicios == null || nombresServicios.isEmpty()) {
            return servicios;
        }

        for (String nombreServicio : nombresServicios) {
            ServiceEntity service = serviceRepository.findByNombreServicio(nombreServicio)
                    .orElseThrow(() -> new RuntimeException("Servicio no encontrado: " + nombreServicio));
            servicios.add(service);
        }
        return servicios;
    }

    private AuthResponse buildAuthResponse(User user, String message) {
        
        // 👈 3. Generación explícita del Token
        String token = jwtUtils.generateToken(user);

        Set<String> roleNames = (user.getRoles() != null) 
                ? user.getRoles().stream().map(Role::getNombre_rol).collect(Collectors.toSet())
                : new HashSet<>();

        Set<String> serviceNames = (user.getServicios() != null)
                ? user.getServicios().stream().map(ServiceEntity::getNombreServicio).collect(Collectors.toSet())
                : new HashSet<>();

        return AuthResponse.builder()
                .id(user.getId())
                .token(token) // 👈 Retornamos la cadena generada
                .type("Bearer")
                .username(user.getUsername())
                .nombre(user.getNombre())
                .apellido(user.getApellido())
                .roles(roleNames)
                .servicios(serviceNames)
                .message(message)
                .build();
    }
}



