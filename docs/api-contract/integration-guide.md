# 📘 Guía de Integración para Frontend - Microservicio de Login & Users

Esta guía es la referencia definitiva para el equipo de Frontend. Aquí encontrarás los contratos de datos (Request/Response) y ejemplos reales para cada endpoint.

---

## 🚀 1. Flujo de Autenticación (Quick Start)

1. **Login:** Envía credenciales a `/api/auth/login`.
2. **Token:** Recibe un `accessToken` (JWT) y un `refreshToken` (UUID).
3. **Uso:** Incluye el token en cada petición protegida en el Header:  
   `Authorization: Bearer <accessToken>`

---

## 🔐 2. Endpoints de Autenticación (`/api/auth`)

### **A. Login de Usuario**
`POST /api/auth/login`

**Request Body:**
```json
{
  "email": "user@example.com",
  "password": "Password123"
}
```

**Response (200 OK):**
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiJ9...",
  "refreshToken": "550e8400-e29b-41d4-a716-446655440000",
  "expiresIn": 86400
}
```

---

### **B. Registro de Nuevo Usuario**
`POST /api/auth/register`

**Request Body:**
```json
{
  "email": "nuevo@correo.com",
  "password": "SecurePassword1",
  "name": "Juan",
  "lastname": "Pérez",
  "phone": "3001234567",
  "dni": "1020304050",
  "roles": ["user"]
}
```

**Response (200 OK):**
```json
{
  "id": 1,
  "email": "nuevo@correo.com",
  "name": "Juan",
  "lastname": "Pérez",
  "phone": "3001234567",
  "dni": "1020304050",
  "isActive": true,
  "roles": [
    { "id": 1, "name": "user" }
  ]
}
```

---

### **C. Revocar Token (Logout)**
`POST /api/auth/revoke`

**Request Body:**
```json
{
  "token": "550e8400-e29b-41d4-a716-446655440000" 
}
```
*(Se debe enviar el Refresh Token para invalidar la sesión en BD)*

**Response (200 OK):** `true`

---

## 👥 3. Gestión de Usuarios (`/api/users`)
*Requieren Header `Authorization: Bearer <token>`*

### **A. Listar Todos los Usuarios**
`GET /api/users`

**Response (200 OK):**
```json
[
  {
    "id": 1,
    "email": "admin@empresa.com",
    "name": "Admin",
    "lastname": "System",
    "phone": "00000000",
    "dni": "11111111",
    "isActive": true,
    "roles": [{ "id": 2, "name": "admin" }]
  }
]
```

---

### **B. Buscar por DNI**
`GET /api/users/dni/{dni}`

**Response (200 OK):**
```json
{
  "id": 5,
  "email": "pedro@mail.com",
  "name": "Pedro",
  "lastname": "García",
  "phone": "3104445566",
  "dni": "10203040",
  "isActive": true,
  "roles": [{ "id": 1, "name": "user" }]
}
```

---

### **C. Actualizar Usuario**
`PUT /api/users/{id}`

**Request Body (Campos opcionales):**
```json
{
  "name": "Juan Editado",
  "phone": "3009998877",
  "isActive": false
}
```

**Response (200 OK):** Devuelve el objeto de usuario actualizado.

---

## ⚠️ 4. Manejo de Errores (Error Schema)

Todas las respuestas de error (4xx y 5xx) siguen este formato:

```json
{
  "code": "ERR_EMAIL_EXISTS",
  "message": "Email already registered",
  "timestamp": "2023-10-27T10:00:00Z"
}
```

### **Principales Códigos de Error:**

| HTTP | Código Interno | Acción del Front |
| :--- | :--- | :--- |
| **401** | `ERR_INVALID_CREDENTIALS` | Mostrar "Correo o clave incorrecta". |
| **401** | `ERR_EXPIRED_TOKEN` | Intentar Refresh Token o redirigir al Login. |
| **409** | `ERR_EMAIL_EXISTS` | Mostrar "Este correo ya está registrado". |
| **404** | `ERR_USER_NOT_FOUND` | Mostrar "No se encontró el usuario". |
| **400** | `ERR_PASSWORD_INVALID` | Validar requisitos: 8+ caracteres, Mayúscula, Minúscula, Número. |

---

## 🛠️ 5. Restricciones de Validación (Formularios)

| Campo | Validación | Regex Sugerido |
| :--- | :--- | :--- |
| **DNI** | 6-10 dígitos, no inicia en 0 | `^[1-9][0-9]{5,9}$` |
| **Phone** | 6-10 dígitos numéricos | `^[0-9]{6,10}$` |
| **Email** | Formato email estándar | `^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$` |
