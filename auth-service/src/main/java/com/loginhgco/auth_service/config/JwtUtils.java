package com.loginhgco.auth_service.config;

import com.loginhgco.auth_service.models.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class JwtUtils {

    @Value("${jwt.secret:TuClaveSecretaSuperSeguraParaFirmarLosTokensJWT123456789}")
    private String jwtSecret;

    @Value("${jwt.expiration:3600000}")
    private long jwtExpirationMs;



    // Obtener la clave de firma desde la propiedad secreta
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    // 1. Generar Token JWT usando los datos del Usuario
    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        
        // Agregar roles y servicios dentro de los claims del token
        claims.put("roles", user.getRoles().stream()
                .map(role -> role.getNombre_rol())
                .collect(Collectors.toList()));
                
        claims.put("servicios", user.getServicios().stream()
                .map(service -> service.getNombreServicio())
                .collect(Collectors.toList()));

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // 2. Obtener el Username desde el Token
    public String getUsernameFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // 3. Validar si el Token es correcto y no ha expirado
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            // Token inválido, expirado o con firma alterada
            return false;
        }
    }
}