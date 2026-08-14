package com.loginhgco.auth_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
            // 1. Deshabilitar CSRF (necesario para APIs REST/Postman)
            .csrf(csrf -> csrf.disable())
            
            // 2. Configurar los permisos de las URLs
            .authorizeHttpRequests(auth -> auth
                // Permitir acceso público a login y registro 👈 AQUÍ ESTÁ EL TRUCO
                .requestMatchers("/api/auth/**").permitAll()
                
                // Cualquier otra petición requerirá autenticación
                .anyRequest().authenticated()
            )
            
            // 3. Manejo de sesiones STATELESS (sin sesión en servidor, ideal para REST/JWT)
            .sessionManagement(session -> 
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            
            .build();
    }
}