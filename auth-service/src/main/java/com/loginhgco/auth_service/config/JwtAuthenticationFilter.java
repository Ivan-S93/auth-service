package com.loginhgco.auth_service.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            @Nullable HttpServletRequest request,
            @Nullable HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        // 1. Obtener el token del encabezado "Authorization"
        String token = parseJwt(request);

        // LOG DE CONTROL: Muestra en la consola de Spring Boot si está llegando el header
        if (token == null) {
            System.out.println("⚠️ JWT Filter: No se encontró header Authorization en: " + request.getRequestURI());
        } else {
            System.out.println("🔑 JWT Filter: Token detectado para la ruta: " + request.getRequestURI());
        }

        // 2. Validar el token
        if (token != null && jwtUtils.validateToken(token)) {
            String username = jwtUtils.getUsernameFromToken(token);

            // 3. Cargar los detalles del usuario
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // 4. Crear el objeto de autenticación para Spring Security
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities()
            );

            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            // 5. Establecer el usuario autenticado en el contexto de Spring Security
            SecurityContextHolder.getContext().setAuthentication(authentication);
            System.out.println("✅ JWT Filter: Usuario '" + username + "' autenticado con éxito.");
        }

        // Continuar con la cadena de filtros
        filterChain.doFilter(request, response);
    }

    // Método auxiliar para extraer el Bearer Token del Header
    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7); // Quitar la palabra "Bearer "
        }

        return null;
    }
}