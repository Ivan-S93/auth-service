package com.loginhgco.auth_service.config;

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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DataInitializerTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private ServiceRepository serviceRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private DataInitializer dataInitializer;

    @Test
    void runCreatesAdminWhenItDoesNotExist() throws Exception {
        when(roleRepository.findByNombre_rol(org.mockito.ArgumentMatchers.anyString())).thenReturn(Optional.empty());
        when(serviceRepository.findByNombreServicio(org.mockito.ArgumentMatchers.anyString())).thenReturn(Optional.empty());
        when(userRepository.existsByUsername("admin")).thenReturn(false);
        when(passwordEncoder.encode("admin123")).thenReturn("encoded-admin");

        dataInitializer.run();

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        User admin = captor.getValue();
        assertEquals("admin", admin.getUsername());
        assertEquals("encoded-admin", admin.getPassword());
        assertEquals("1111111", admin.getCi());
        assertTrue(admin.isActive());
        assertEquals(1, admin.getRoles().size());
        assertEquals(1, admin.getServicios().size());
    }

    @Test
    void runDoesNotCreateAnotherAdminWhenOneExists() throws Exception {
        when(roleRepository.findByNombre_rol(org.mockito.ArgumentMatchers.anyString())).thenReturn(Optional.of(new com.loginhgco.auth_service.models.Role()));
        when(serviceRepository.findByNombreServicio(org.mockito.ArgumentMatchers.anyString())).thenReturn(Optional.of(new com.loginhgco.auth_service.models.ServiceEntity()));
        when(userRepository.existsByUsername("admin")).thenReturn(true);

        dataInitializer.run();

        verify(userRepository, never()).save(org.mockito.ArgumentMatchers.any(User.class));
    }
}
