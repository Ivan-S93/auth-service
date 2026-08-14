package com.loginhgco.auth_service.config;

import com.loginhgco.auth_service.models.Role;
import com.loginhgco.auth_service.models.ServiceEntity;
import com.loginhgco.auth_service.models.User;
import com.loginhgco.auth_service.repositories.RoleRepository;
import com.loginhgco.auth_service.repositories.ServiceRepository;
import com.loginhgco.auth_service.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ServiceRepository serviceRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) throws Exception {

        // 1. Guardar Roles iniciales
        Role rolAdmin = getOrCreateRole("ROL_ADMINISTRADOR");
        Role rolMedico = getOrCreateRole("ROL_MEDICO");
        Role rolNutricionista = getOrCreateRole("ROL_NUTRICIONISTA");
        Role rolUsuario = getOrCreateRole("ROL_USUARIO");

        // 2. Guardar Servicios iniciales
        ServiceEntity servicioTic = getOrCreateService("TIC");
        ServiceEntity servicioQuirofano = getOrCreateService("QUIROFANO");
        ServiceEntity servicioPediatria = getOrCreateService("PEDIATRIA");

        // 3. Crear el Usuario Admin si no existe
        if (!userRepository.existsByUsername("admin")) {
            User admin = new User();
            admin.setCi("1111111");
            admin.setNombre("Admin");
            admin.setApellido("Sistema");
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123")); // Clave encriptada
            admin.setActive(true);
            admin.setDescripcion("Administrador inicial");

            // Asignar rol ROL_ADMINISTRADOR
            Role adminRole = getOrCreateRole("ROL_ADMINISTRADOR");
            Set<Role> roles = new HashSet<>();
            roles.add(adminRole);
            admin.setRoles(roles);

            // Asignar servicio TIC
            Set<ServiceEntity> servicios = new HashSet<>();
            servicios.add(servicioTic);
            admin.setServicios(servicios);

            userRepository.save(admin);
            System.out.println("✅ DataInitializer: Usuario 'admin' creado exitosamente con contraseña encriptada.");
        }
    }

    private Role getOrCreateRole(String nombreRol) {
        return roleRepository.findByNombre_rol(nombreRol)
                .orElseGet(() -> {
                    Role newRole = new Role();
                    newRole.setNombre_rol(nombreRol);
                    return roleRepository.save(newRole);
                });
    }

    private ServiceEntity getOrCreateService(String nombreServicio) {
        // Ajusta findByNombreServicio o findByNombre_servicio según el nombre exacto en tu ServiceRepository
        return serviceRepository.findByNombreServicio(nombreServicio)
                .orElseGet(() -> {
                    ServiceEntity newService = new ServiceEntity();
                    newService.setNombreServicio(nombreServicio);
                    return serviceRepository.save(newService);
                });
    }
}