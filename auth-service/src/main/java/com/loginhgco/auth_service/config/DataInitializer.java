package com.loginhgco.auth_service.config;

import com.loginhgco.auth_service.models.Role;
import com.loginhgco.auth_service.models.Service;
import com.loginhgco.auth_service.models.User;
import com.loginhgco.auth_service.repositories.RoleRepository;
import com.loginhgco.auth_service.repositories.ServiceRepository;
import com.loginhgco.auth_service.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final ServiceRepository serviceRepository;
    private final UserRepository userRepository;

    public DataInitializer(RoleRepository roleRepository, 
                           ServiceRepository serviceRepository, 
                           UserRepository userRepository) {
        this.roleRepository = roleRepository;
        this.serviceRepository = serviceRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // 1- Roles iniciales
        createRoleIfNotFound("ROL_ADMINISTRADOR");
        createRoleIfNotFound("ROL_MEDICO");
        createRoleIfNotFound("ROL_NUTRICIONISTA");
        createRoleIfNotFound("ROL_USUARIO");

        // 2- Servicios iniciales
        createServiceIfNotFound("QUIROFANO");
        createServiceIfNotFound("NUTRICION");
        createServiceIfNotFound("PEDIATRIA");
        createServiceIfNotFound("TIC");

        // 3- Usuario Administrador
        if (!userRepository.existsByUsername("admin")) {
            Role adminRole = roleRepository.findByNombre_rol("ROL_ADMINISTRADOR").orElse(null);
            Service tic = serviceRepository.findByNombre_servicio("TIC").orElse(null);

            User admin = new User();
            admin.setCi("1234567");
            admin.setNombre("Admin");
            admin.setApellido("Sistema");
            admin.setUsername("admin");
            admin.setPassword("admin123");
            admin.setActive(true);
            admin.setDescripcion("Usuario Administrador");

            if (adminRole != null) {
                admin.setRoles(Set.of(adminRole));
            }
            if (tic != null) {
                admin.setServicios(Set.of(tic));
            }

            userRepository.save(admin);
            System.out.println("✅ Usuario administrador creado: " + admin.getUsername());
        }
    }

    private void createRoleIfNotFound(String nombreRol) {
        if (roleRepository.findByNombre_rol(nombreRol).isEmpty()) {
            Role role = new Role();
            role.setNombre_rol(nombreRol);
            roleRepository.save(role);
            System.out.println("🔹 Rol creado: " + nombreRol);
        }
    }

    private void createServiceIfNotFound(String nombreServicio) {
        if (serviceRepository.findByNombre_servicio(nombreServicio).isEmpty()) {
            Service service = new Service();
            service.setNombre_servicio(nombreServicio);
            serviceRepository.save(service);
            System.out.println("🔹 Servicio creado: " + nombreServicio);
        }
    }
}
