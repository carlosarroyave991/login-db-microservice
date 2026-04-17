CREATE SCHEMA IF NOT EXISTS security;

-- 1. Aseguramos que los roles existan (necesario para el SELECT de abajo)
INSERT INTO security.role (name)
VALUES
    ('admin'),
    ('user')
ON CONFLICT (name) DO NOTHING;

-- 2. INSERT del usuario administrador
-- Ajustado a los campos de tu UserEntity: email, password, name, lastname, phone, dni, is_active, created_at
INSERT INTO security.users (email, password, name, lastname, phone, dni, is_active, created_at)
VALUES (
    'admin@mail.com',
    '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.7u41W3u', -- admin123
    'Admin',
    'System',
    '123456789',
    '10203040',
    true,
    NOW()
)
ON CONFLICT (email) DO NOTHING;

-- 3. Asociación del usuario con el rol en la tabla 'user_role'
-- Nota: Tu tabla se llama 'user_role' según @Table en UserRoleEntity
INSERT INTO security.user_role (user_id, role_id)
SELECT u.id, r.id
FROM security.users u, security.role r
WHERE u.email = 'admin@mail.com'
  AND r.name = 'admin'
-- Evita duplicar la relación si ya existe
ON CONFLICT DO NOTHING;