# Auth Service - Microservicio de Autenticación JWT

**Auth Service** es un servicio REST desacoplado de autenticación y autorización desarrollado con **Spring Boot**, **Spring Security** y **PostgreSQL**. Está diseñado específicamente para ser integrado como el módulo central de seguridad en múltiples sistemas institucionales u hospitalarios.

---

## 📈 Estado del Proyecto
La capa de autenticación se encuentra **100% funcional y probada**, ofreciendo las siguientes capacidades:
* **Autenticación y registro** con contraseñas encriptadas de forma segura mediante **BCrypt**.
* **Persistencia robusta** en PostgreSQL utilizando relaciones Muchos a Muchos (N:M) para `usuario_roles` y `usuario_servicios`.
* **Generación, firma y validación** de Tokens JWT activa en cada petición.
* **Protección de rutas** mediante un filtro personalizado interceptor (`JwtAuthenticationFilter`).
* **Control administrativo** total de usuarios (Creación, Listado y Toggle de Estado Activo/Inactivo).

---

## 🛠️ Tecnologías Utilizadas
* **Java 17+**
* **Spring Boot 3.x**
* **Spring Security** (Autenticación y Control de Acceso basado en Roles)
* **JJWT (io.jsonwebtoken)** (Manejo y firma de Tokens JWT)
* **Spring Data JPA / Hibernate** (Persistencia de Datos)
* **PostgreSQL** (Base de Datos Relacional)
* **Lombok** (Reducción de código boilerplate)
* **Maven** (Gestión de dependencias)

---

## 📁 Estructura Principal del Proyecto

```text
src/main/java/com/loginhgco/auth_service/
├── config/
│   ├── ApplicationConfig.java        # UserDetailsService y Beans de Spring
│   ├── DataInitializer.java          # Sembrado automático de roles, servicios y admin inicial
│   ├── JwtAuthenticationFilter.java  # Filtro que intercepta y valida el Token JWT
│   ├── JwtUtils.java                 # Generación, firma y parseo de Tokens
│   ├── PasswordEncoderConfig.java    # Bean de encriptación BCrypt
│   └── SecurityConfig.java           # Configuración de URLs públicas/privadas, CORS y CSRF
├── controller/
│   ├── AuthController.java           # Endpoints de autenticación (/api/auth/login, /register)
│   ├── RoleController.java           # Endpoint de catálogo de roles
│   ├── ServiceController.java        # Endpoint de catálogo de servicios
│   └── UserController.java           # Endpoints administrativos de usuarios (/api/users)
├── dtos/
│   ├── AuthResponse.java             # DTO de respuesta con datos del usuario y token
│   ├── LoginRequest.java             # DTO de entrada para credenciales
│   └── RegisterRequest.java          # DTO de entrada para nuevos registros
├── exceptions/
│   └── GlobalExceptionHandler.java   # Manejador global de excepciones HTTP
├── models/
│   ├── Role.java                     # Entidad Rol (ADMINISTRADOR, MEDICO, etc.)
│   ├── ServiceEntity.java            # Entidad Servicio (TIC, QUIROFANO, PEDIATRIA, etc.)
│   └── User.java                     # Entidad Usuario
├── repositories/
│   ├── RoleRepository.java
│   ├── ServiceRepository.java
│   └── UserRepository.java
└── service/
    └── AuthService.java              # Lógica de negocio (Login, Registro y Mapeo DTO)
```

---

## 📋 Requisitos Previos

Antes de ejecutar el proyecto, asegúrate de tener instalado en tu entorno local:
* **JDK 17** o superior
* **PostgreSQL 12+** en ejecución
* **Maven 3.8+** (o utilizar el ejecutable `./mvnw` incluido)
* Un IDE de tu preferencia (**IntelliJ IDEA**, **Eclipse** o **VS Code**)

---

## 🚀 Cómo Levantarlo y Ejecutarlo

### 1. Clonar el repositorio
```bash
git clone https://github.com/Ivan-S93/auth-service.git
cd auth-service
```

### 2. Configurar la Base de Datos PostgreSQL
Crea una base de datos en tu servidor local de PostgreSQL llamada `auth_db`:
```sql
CREATE DATABASE auth_db;
```

Asegúrate de ajustar tus credenciales y configuración del JWT en el archivo `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/auth_db
spring.datasource.username=tu_usuario_postgres
spring.datasource.password=tu_contraseña_postgres

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# Clave secreta para la firma del JWT (Mínimo 256 bits / 32 caracteres)
jwt.secret=TU_CLAVE_SECRETA_JWT_SUPER_SEGURA_CON_MAS_DE_256_BITS
jwt.expiration=86400000
```

### 3. Compilar y Ejecutar

**Usando la línea de comandos (Maven Wrapper):**
* **En Linux / macOS:**
  ```bash
  ./mvnw clean spring-boot:run
  ```
* **En Windows (PowerShell / CMD):**
  ```bash
  mvnw clean spring-boot:run
  ```

**Desde tu IDE:**
* Abre el proyecto y ejecuta directamente la clase principal `AuthServiceApplication.java`.

💡 **Carga Automática:** Al iniciar el servidor por primera vez, el componente `DataInitializer` creará automáticamente las tablas necesarias en la base de datos, sembrará los roles principales, la lista de servicios institucionales y el usuario administrador por defecto si aún no existen.

---

## 🔗 Endpoints Principales

| Método | Ruta | Acceso | Descripción |
| :--- | :--- | :--- | :--- |
| **POST** | `/api/auth/login` | Público | Autentica al usuario y retorna el JWT Token. |
| **POST** | `/api/auth/register` | Público / Admin | Registro inicial de un nuevo usuario. |
| **GET** | `/api/users` | ADMINISTRADOR | Lista todos los usuarios registrados. |
| **POST** | `/api/users` | ADMINISTRADOR | Crea un usuario asignando Rol y Servicio. |
| **PATCH** | `/api/users/{id}/status` | ADMINISTRADOR | Alterna el estado (Activo/Inactivo) del usuario. |
| **GET** | `/api/roles` | Autenticado | Obtiene la lista de roles disponibles. |
| **GET** | `/api/servicios` | Autenticado | Obtiene la lista de servicios del hospital. |
