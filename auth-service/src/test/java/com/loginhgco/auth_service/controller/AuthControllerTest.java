package com.loginhgco.auth_service.controller;

import com.loginhgco.auth_service.dtos.AuthResponse;
import com.loginhgco.auth_service.dtos.LoginRequest;
import com.loginhgco.auth_service.dtos.RegisterRequest;
import com.loginhgco.auth_service.service.AuthService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    @Test
    void loginDelegatesToServiceAndReturnsOk() {
        LoginRequest request = new LoginRequest("alice", "secret");
        AuthResponse response = response("Login exitoso");
        when(authService.login(request)).thenReturn(response);

        ResponseEntity<AuthResponse> result = authController.login(request);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
        verify(authService).login(request);
    }

    @Test
    void registerDelegatesToServiceAndReturnsCreated() {
        RegisterRequest request = new RegisterRequest();
        AuthResponse response = response("Usuario registrado con éxito");
        when(authService.register(request)).thenReturn(response);

        ResponseEntity<AuthResponse> result = authController.register(request);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(response, result.getBody());
        verify(authService).register(request);
    }

    private AuthResponse response(String message) {
        return AuthResponse.builder().username("alice").roles(Set.of()).servicios(Set.of()).message(message).build();
    }
}
