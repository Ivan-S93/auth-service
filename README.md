🛡️ Auth Service - Módulo Centralizado de Autenticación
Servicio REST centralizado de autenticación y autorización desarrollado en Java (Spring Boot), Spring Security y PostgreSQL. Diseñado como un microservicio desacoplado para ser integrado con múltiples sistemas de la institución.

🚀 Estado Actual del Proyecto
El proyecto cuenta con la capa de autenticación completa y funcional, persistencia de datos en PostgreSQL, encriptación segura de contraseñas mediante BCrypt, manejo transaccional de relaciones Many-to-Many y endpoints REST totalmente validados.

🛠️ Tecnologías Utilizadas
Java 17+

Spring Boot 3.x

Spring Security (Autenticación y Control de Acceso)

Spring Data JPA / Hibernate (Persistencia de Datos)

PostgreSQL (Base de Datos Relacional)

BCrypt (Encriptación de Contraseñas)

Lombok (Reducción de código boilerplate)

📁 Estructura del Proyecto
Plaintext
src/main/java/com/loginhgco/auth_service/
├── config/
│   ├── DataInitializer.java         # Sembrado automático de roles, servicios y admin
│   ├── PasswordEncoderConfig.java   # Bean de encriptación BCryptPasswordEncoder
│   └── SecurityConfig.java          # Configuración de URLs públicas y deshabilitación de CSRF
├── controllers/
│   └── AuthController.java          # Endpoints REST para /login y /register
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


🗄️ Modelo de Datos (PostgreSQL)
El sistema maneja un esquema con dos relaciones N:M (Muchos a Muchos):

Usuarios y Roles (user_roles)

Usuarios y Servicios (user_servicios)

/////
📌 Siguientes Pasos (Roadmap)
[ ] Implementación de generación y firma de Tokens JWT (JwtUtils).

[ ] Creación de filtro de autenticación por Token (JwtAuthenticationFilter).

[ ] Implementación de Refresh Tokens.