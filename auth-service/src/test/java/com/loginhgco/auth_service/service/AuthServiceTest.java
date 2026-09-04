package com.loginhgco.auth_service.service;

import com.loginhgco.auth_service.dtos.AuthResponse;
import com.loginhgco.auth_service.dtos.LoginRequest;
import com.loginhgco.auth_service.dtos.RegisterRequest;
import com.loginhgco.auth_service.models.Role;
import com.loginhgco.auth_service.models.ServiceEntity;
import com.loginhgco.auth_service.models.User;
import com.loginhgco.auth_service.repositories.RoleRepository;
import com.loginhgco.auth_service.repositories.ServiceRepository;
import com.loginhgco.auth_service.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private ServiceRepository serviceRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    @Test
    void loginReturnsUserDetailsWhenCredentialsAndStatusAreValid() {
        User user = userWithAssociations();
        LoginRequest request = new LoginRequest("alice", "secret");
        when(userRepository.findByUsername("alice")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("secret", "encoded")).thenReturn(true);

        AuthResponse response = authService.login(request);

        assertEquals("alice", response.getUsername());
        assertEquals(Set.of("ROL_ADMIN"), response.getRoles());
        assertEquals(Set.of("TIC"), response.getServicios());
        assertEquals("Bearer", response.getType());
        assertNull(response.getToken());
        assertEquals("Login exitoso", response.getMessage());
    }

    @Test
    void loginRejectsUnknownUser() {
        when(userRepository.findByUsername("missing")).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> authService.login(new LoginRequest("missing", "secret")));

        assertEquals("Usuario o contraseña incorrectos", exception.getMessage());
    }

    @Test
    void loginRejectsWrongPasswordAndInactiveUser() {
        User user = userWithAssociations();
        when(userRepository.findByUsername("alice")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrong", "encoded")).thenReturn(false);

        RuntimeException wrongPassword = assertThrows(RuntimeException.class,
                () -> authService.login(new LoginRequest("alice", "wrong")));
        assertEquals("Usuario o contraseña incorrectos", wrongPassword.getMessage());

        when(passwordEncoder.matches("secret", "encoded")).thenReturn(true);
        user.setActive(false);
        RuntimeException inactive = assertThrows(RuntimeException.class,
                () -> authService.login(new LoginRequest("alice", "secret")));
        assertEquals("El usuario está inactivo", inactive.getMessage());
    }

    @Test
    void registerEncodesPasswordResolvesAssociationsAndReturnsResponse() {
        Role role = role("ROL_ADMIN");
        ServiceEntity service = service("TIC");
        RegisterRequest request = new RegisterRequest("1", "Alice", "User", "alice", "secret", "desc",
                new LinkedHashSet<>(Set.of("ROL_ADMIN")), new LinkedHashSet<>(Set.of("TIC")));
        when(userRepository.existsByUsername("alice")).thenReturn(false);
        when(userRepository.existsByCi("1")).thenReturn(false);
        when(passwordEncoder.encode("secret")).thenReturn("encoded");
        when(roleRepository.findByNombre_rol("ROL_ADMIN")).thenReturn(Optional.of(role));
        when(serviceRepository.findByNombreServicio("TIC")).thenReturn(Optional.of(service));

        AuthResponse response = authService.register(request);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        User saved = captor.getValue();
        assertEquals("alice", saved.getUsername());
        assertEquals("encoded", saved.getPassword());
        assertEquals(Set.of(role), saved.getRoles());
        assertEquals(Set.of(service), saved.getServicios());
        assertEquals("Usuario registrado con éxito", response.getMessage());
    }

    @Test
    void registerAssignsDefaultRoleWhenNoRolesAreProvided() {
        Role defaultRole = role("ROL_USUARIO");
        RegisterRequest request = new RegisterRequest("1", "Alice", "User", "alice", "secret", null, null, Set.of());
        when(userRepository.existsByUsername("alice")).thenReturn(false);
        when(userRepository.existsByCi("1")).thenReturn(false);
        when(passwordEncoder.encode("secret")).thenReturn("encoded");
        when(roleRepository.findByNombre_rol("ROL_USUARIO")).thenReturn(Optional.of(defaultRole));

        authService.register(request);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        assertEquals(Set.of(defaultRole), captor.getValue().getRoles());
        assertEquals(Set.of(), captor.getValue().getServicios());
    }

    @Test
    void registerRejectsDuplicateUsernameOrCi() {
        RegisterRequest request = new RegisterRequest("1", "Alice", "User", "alice", "secret", null, Set.of("R"), Set.of("S"));
        when(userRepository.existsByUsername("alice")).thenReturn(true);
        RuntimeException duplicateUsername = assertThrows(RuntimeException.class, () -> authService.register(request));
        assertEquals("El username ya está en uso", duplicateUsername.getMessage());

        when(userRepository.existsByUsername("alice")).thenReturn(false);
        when(userRepository.existsByCi("1")).thenReturn(true);
        RuntimeException duplicateCi = assertThrows(RuntimeException.class, () -> authService.register(request));
        assertEquals("La cédula ya está registrada", duplicateCi.getMessage());
    }

    @Test
    void registerRejectsUnknownRoleOrService() {
        RegisterRequest roleRequest = new RegisterRequest("1", "Alice", "User", "alice", "secret", null, Set.of("UNKNOWN"), Set.of());
        when(userRepository.existsByUsername("alice")).thenReturn(false);
        when(userRepository.existsByCi("1")).thenReturn(false);
        when(passwordEncoder.encode("secret")).thenReturn("encoded");
        when(roleRepository.findByNombre_rol("UNKNOWN")).thenReturn(Optional.empty());
        RuntimeException roleException = assertThrows(RuntimeException.class, () -> authService.register(roleRequest));
        assertEquals("Rol no encontrado: UNKNOWN", roleException.getMessage());

        RegisterRequest serviceRequest = new RegisterRequest("2", "Alice", "User", "alice2", "secret", null, Set.of("R"), Set.of("UNKNOWN"));
        Role role = role("R");
        when(roleRepository.findByNombre_rol("R")).thenReturn(Optional.of(role));
        when(serviceRepository.findByNombreServicio("UNKNOWN")).thenReturn(Optional.empty());
        RuntimeException serviceException = assertThrows(RuntimeException.class, () -> authService.register(serviceRequest));
        assertEquals("Servicio no encontrado: UNKNOWN", serviceException.getMessage());
    }

    private User userWithAssociations() {
        User user = new User();
        user.setId(7L);
        user.setUsername("alice");
        user.setNombre("Alice");
        user.setApellido("User");
        user.setPassword("encoded");
        user.setActive(true);
        user.setRoles(Set.of(role("ROL_ADMIN")));
        user.setServicios(Set.of(service("TIC")));
        return user;
    }

    private Role role(String name) {
        Role role = new Role();
        role.setNombre_rol(name);
        return role;
    }

    private ServiceEntity service(String name) {
        ServiceEntity service = new ServiceEntity();
        service.setNombreServicio(name);
        return service;
    }
}
