CREATE SCHEMA IF NOT EXISTS security;

-- INSERT con protección contra duplicados
INSERT INTO security.role (name)
VALUES
    ('admin'),
    ('user')
ON CONFLICT (name) DO NOTHING;