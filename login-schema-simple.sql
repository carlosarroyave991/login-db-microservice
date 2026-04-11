-- =============================================================================
-- SCHEMA: security — Login Microservice (Simple / Sin Cognito)
-- Base de datos: PostgreSQL
-- Autenticación: JWT propio (sin proveedor externo)
-- Orientado a: competencia de programación / prueba técnica
-- =============================================================================

CREATE SCHEMA IF NOT EXISTS security;

-- -----------------------------------------------------------------------------
-- security.users
-- -----------------------------------------------------------------------------
CREATE TABLE security.users (
    id            SERIAL          PRIMARY KEY,
    email         VARCHAR(255)    NOT NULL UNIQUE,
    password      VARCHAR(255)    NOT NULL,               -- BCrypt
    name          VARCHAR(100)    NOT NULL,
    lastname      VARCHAR(100)    NOT NULL,
    phone         VARCHAR(20)     NOT NULL,
    dni           VARCHAR(10)     NOT NULL,
    is_active     BOOLEAN         NOT NULL DEFAULT true,
    created_at    TIMESTAMPTZ     NOT NULL DEFAULT now(),
    last_login    TIMESTAMPTZ
);

COMMENT ON COLUMN security.users.password IS 'Hash BCrypt de la contraseña. Nunca almacenar en claro.';

-- -----------------------------------------------------------------------------
-- security.role
-- -----------------------------------------------------------------------------
CREATE TABLE security.role (
    id   SERIAL       PRIMARY KEY,
    name VARCHAR(50)  NOT NULL UNIQUE                     -- ROLE_ADMIN, ROLE_USER
);

INSERT INTO security.role (name) VALUES
    ('admin'),
    ('user');

-- -----------------------------------------------------------------------------
-- security.user_role
-- -----------------------------------------------------------------------------
CREATE TABLE security.user_role (
    id   SERIAL       PRIMARY KEY,
    user_id INTEGER NOT NULL REFERENCES security.users(id),
    role_id INTEGER NOT NULL REFERENCES security.role(id)
);

-- -----------------------------------------------------------------------------
-- security.refresh_token
-- Almacena los refresh tokens activos para permitir renovación y revocación.
-- El access token (JWT) NO se persiste — se valida con la clave secreta.
-- -----------------------------------------------------------------------------
CREATE TABLE security.refresh_token (
    id         SERIAL          PRIMARY KEY,
    user_id    INTEGER         NOT NULL REFERENCES security.users(id),
    token_hash VARCHAR(255)    NOT NULL UNIQUE,            -- SHA-256 del token
    expires_at TIMESTAMPTZ     NOT NULL,
    revoked    BOOLEAN         NOT NULL DEFAULT false,
    created_at TIMESTAMPTZ     NOT NULL DEFAULT now()
);

CREATE INDEX idx_refresh_token_user ON security.refresh_token(user_id);

-- =============================================================================
-- FLUJO DEL MICROSERVICIO (referencia Java / Spring Boot)
-- =============================================================================
--
--  POST /auth/register
--     - Recibe: { email, password, name, lastname }
--     - Guarda en users con password = BCrypt.encode(password)
--     - Asigna ROLE_USER por defecto en user_role
--
--  POST /auth/login
--     - Recibe: { email, password }
--     - Busca el usuario por email
--     - Valida: BCrypt.matches(password, password)
--     - Genera: access_token (JWT, exp 15min) + refresh_token (UUID, exp 7d)
--     - Guarda: refresh_token.token_hash = SHA-256(refresh_token)
--     - Actualiza: users.last_login = now()
--     - Responde: { access_token, refresh_token, expires_in }
--
--  POST /auth/refresh
--     - Recibe: { refresh_token }
--     - Busca en refresh_token por token_hash y valida: no revocado, no expirado
--     - Genera nuevo access_token
--     - Responde: { access_token, expires_in }
--
--  POST /auth/logout
--     - Recibe: Bearer access_token (header)
--     - Marca refresh_token.revoked = true para ese usuario
--     - Responde: 200 OK
--
--  GET /auth/me
--     - Recibe: Bearer access_token (header)
--     - Valida JWT con la clave secreta
--     - Responde: { id, email, name, lastname, roles }
--
-- =============================================================================
--
-- DEPENDENCIAS JAVA SUGERIDAS (Spring Boot 3 / Java 21)
--
--   - spring-boot-starter-security
--   - spring-boot-starter-data-jpa
--   - io.jsonwebtoken:jjwt-api:0.12.x
--   - io.jsonwebtoken:jjwt-impl:0.12.x
--   - org.springframework.security:spring-security-crypto  (BCrypt)
--   - postgresql (driver)
--
-- =============================================================================
