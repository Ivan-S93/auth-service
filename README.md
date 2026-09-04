# Auth Service - Microservicio de Autenticación JWT

Servicio REST desacoplado de autenticación y autorización desarrollado con **Spring Boot**, **Spring Security** y **PostgreSQL**. Diseñado para ser integrado como módulo central de seguridad en múltiples sistemas institucionales.

## Estado del Proyecto

Capa de autenticación 100% funcional y probada:
* Autenticación y registro con contraseñas encriptadas (**BCrypt**).
* Persistencia en **PostgreSQL** con relaciones N:M (`user_roles`, `user_servicios`).
* Generación, firma y validación de **Tokens JWT** activa.
* Protección de rutas con filtro personalizado (`JwtAuthenticationFilter`).

## Tecnologías Utilizadas

* **Java 17+ / 25**
* **Spring Boot 3.x**
* **Spring Security** (Autenticación y Control de Acceso)
* **JJWT (io.jsonwebtoken)** (Manejo de Tokens JWT)
* **Spring Data JPA / Hibernate** (Persistencia de Datos)
* **PostgreSQL** (Base de Datos Relacional)
* **Lombok** (Reducción de código boilerplate)

## 📁 Estructura Principal del Proyecto

```text
src/main/java/com/loginhgco/auth_service/
├── config/
│   ├── ApplicationConfig.java       # UserDetailsService y Beans de Spring
│   ├── DataInitializer.java         # Sembrado automático de roles, servicios y admin
│   ├── JwtAuthenticationFilter.java # Filtro que intercepta y valida el Token JWT
│   ├── JwtUtils.java                # Generación, firma y parseo de Tokens
│   ├── PasswordEncoderConfig.java   # Bean de encriptación BCrypt
│   └── SecurityConfig.java          # Configuración de URLs públicas/privadas y CSRF
├── controllers/
│   └── AuthController.java          # Endpoints REST (/login, /register)
├── dtos/
│   ├── AuthResponse.java            # DTO de respuesta con datos del usuario y token
│   ├── LoginRequest.java            # DTO de entrada para credenciales
│   └── RegisterRequest.java         # DTO de entrada para nuevos registros
├── exceptions/
│   └── GlobalExceptionHandler.java  # Manejador global de excepciones HTTP
├── models/
│   ├── Role.java                    # Entidad Rol (ROL_ADMINISTRADOR, ROL_MEDICO, etc.)
│   ├── ServiceEntity.java           # Entidad Servicio (TIC, QUIROFANO, PEDIATRIA, etc.)
│   └── User.java                    # Entidad Usuario
├── repositories/
│   ├── RoleRepository.java
│   ├── ServiceRepository.java
│   └── UserRepository.java
└── service/
    └── AuthService.java             # Lógica de negocio (Login, Registro y Mapeo DTO)