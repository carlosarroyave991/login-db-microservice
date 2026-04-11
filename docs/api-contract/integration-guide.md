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
  "createdAt": "2024-01-15T10:30:00Z",
  "lastLogin": null,
  "roles": ["user"]
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

### **D. Validar Token**
`POST /api/auth/validate`

**Request Body:**
```json
{
  "token": "550e8400-e29b-41d4-a716-446655440000"
}
```

**Response (200 OK):** `true` si el token es válido, `false` si no lo es.

---

## 👥 3. Gestión de Usuarios (`/api/users`)
*Todos los endpoints requieren Header `Authorization: Bearer <accessToken>`*

---

### **A. Listar Todos los Usuarios**
`GET /api/users`

*Sin Request Body ni parámetros.*

**Response (200 OK):**
```json
[
  {
    "id": 1,
    "email": "admin@empresa.com",
    "name": "Admin",
    "lastname": "System",
    "phone": "3001234567",
    "dni": "11111111",
    "isActive": true,
    "createdAt": "2024-01-01T08:00:00Z",
    "lastLogin": "2024-06-10T14:22:00Z",
    "roles": ["admin"]
  },
  {
    "id": 2,
    "email": "juan@mail.com",
    "name": "Juan",
    "lastname": "Pérez",
    "phone": "3009998877",
    "dni": "1020304050",
    "isActive": true,
    "createdAt": "2024-02-20T09:15:00Z",
    "lastLogin": "2024-06-09T11:00:00Z",
    "roles": ["user"]
  }
]
```

**Error posible:**
- `404 ERR_DB_EMPTY` — No hay usuarios registrados en la base de datos.

---

### **B. Obtener Usuario por ID**
`GET /api/users/id/{id}`

**Path Variable:** `id` (Long) — ID del usuario.

**Ejemplo:** `GET /api/users/id/5`

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
  "createdAt": "2024-03-10T07:45:00Z",
  "lastLogin": "2024-06-08T16:30:00Z",
  "roles": ["user"]
}
```

**Error posible:**
- `404 ERR_USER_NOT_FOUND` — No existe un usuario con ese ID.

---

### **C. Buscar por DNI**
`GET /api/users/dni/{dni}`

**Path Variable:** `dni` (String) — DNI del usuario.

**Ejemplo:** `GET /api/users/dni/10203040`

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
  "createdAt": "2024-03-10T07:45:00Z",
  "lastLogin": "2024-06-08T16:30:00Z",
  "roles": ["user"]
}
```

**Error posible:**
- `404 ERR_DNI_NOT_FOUND` — No existe un usuario con ese DNI.

---

### **D. Buscar Usuarios por Nombre**
`GET /api/users/search?name={name}`

**Query Param:** `name` (String) — Nombre a buscar (búsqueda parcial).

**Ejemplo:** `GET /api/users/search?name=Juan`

**Response (200 OK):**
```json
[
  {
    "id": 2,
    "email": "juan@mail.com",
    "name": "Juan",
    "lastname": "Pérez",
    "phone": "3009998877",
    "dni": "1020304050",
    "isActive": true,
    "createdAt": "2024-02-20T09:15:00Z",
    "lastLogin": "2024-06-09T11:00:00Z",
    "roles": ["user"]
  }
]
```

**Error posible:**
- `404 ERR_USER_NAME_NOT_FOUND` — No se encontraron usuarios con ese nombre.

---

### **E. Actualizar Usuario**
`PUT /api/users/{id}`

**Path Variable:** `id` (Long) — ID del usuario a actualizar.

**Request Body (todos los campos son opcionales, solo enviar los que se quieren modificar):**
```json
{
  "email": "nuevo@correo.com",
  "name": "Juan Editado",
  "lastname": "Pérez Nuevo",
  "phone": "3009998877",
  "dni": "1020304050",
  "isActive": false
}
```

**Response (200 OK):** Devuelve el objeto de usuario actualizado.
```json
{
  "id": 2,
  "email": "nuevo@correo.com",
  "name": "Juan Editado",
  "lastname": "Pérez Nuevo",
  "phone": "3009998877",
  "dni": "1020304050",
  "isActive": false,
  "createdAt": "2024-02-20T09:15:00Z",
  "lastLogin": "2024-06-09T11:00:00Z",
  "roles": ["user"]
}
```

**Errores posibles:**
- `404 ERR_USER_NOT_FOUND` — No existe un usuario con ese ID.
- `409 ERR_EMAIL_EXISTS` — El nuevo email ya está registrado por otro usuario.
- `400 ERR_DNI_INVALID` — El DNI no cumple el formato requerido.
- `400 ERR_PHONE_INVALID` — El teléfono no cumple el formato requerido.

---

### **F. Eliminar Usuario**
`DELETE /api/users/{id}`

**Path Variable:** `id` (Long) — ID del usuario a eliminar.

**Ejemplo:** `DELETE /api/users/5`

*Sin Request Body.*

**Response (204 No Content):** Sin cuerpo de respuesta.

**Error posible:**
- `404 ERR_USER_NOT_FOUND` — No existe un usuario con ese ID.

---

## ⚠️ 4. Manejo de Errores (Error Schema)

Todas las respuestas de error (4xx y 5xx) siguen este formato:

```json
{
  "code": "ERR_EMAIL_EXISTS",
  "message": "Email already registered",
  "timestamp": "2024-06-10T10:00:00"
}
```

> El campo `details` puede aparecer opcionalmente con información adicional del error.

### **Tabla Completa de Códigos de Error:**

| HTTP | Código Interno | Descripción | Acción del Front |
| :--- | :--- | :--- | :--- |
| **400** | `ERR_VALIDATION` | Error de validación genérico | Revisar los campos del formulario. |
| **400** | `ERR_INVALID_INPUT` | Datos de entrada inválidos | Revisar el formato de los datos enviados. |
| **400** | `ERR_EMAIL_INVALID` | Formato de email inválido | Validar formato de email antes de enviar. |
| **400** | `ERR_PHONE_INVALID` | Teléfono inválido | Validar que sea numérico y de 6-10 dígitos. |
| **400** | `ERR_PASSWORD_INVALID` | Contraseña inválida | Mostrar requisitos: 8+ chars, mayúscula, minúscula, número. |
| **400** | `ERR_DNI_INVALID` | DNI inválido | Validar que sea numérico, 6-10 dígitos, no inicia en 0. |
| **400** | `ERR_ROLE_INVALID` | Rol inválido | Usar solo roles permitidos: `user`, `admin`. |
| **401** | `ERR_UNAUTHORIZED` | Acceso no autorizado | Redirigir al Login. |
| **401** | `ERR_INVALID_CREDENTIALS` | Credenciales incorrectas | Mostrar "Correo o clave incorrecta". |
| **401** | `ERR_INVALID_TOKEN` | Token inválido | Redirigir al Login. |
| **401** | `ERR_EXPIRED_TOKEN` | Token expirado | Intentar Refresh Token o redirigir al Login. |
| **403** | `ERR_SECURITY` | Acceso prohibido | Mostrar "No tienes permisos para esta acción". |
| **404** | `ERR_NOT_FOUND` | Recurso no encontrado | Mostrar mensaje genérico de no encontrado. |
| **404** | `ERR_USER_NOT_FOUND` | Usuario no encontrado | Mostrar "No se encontró el usuario". |
| **404** | `ERR_USER_NAME_NOT_FOUND` | Sin coincidencias por nombre | Mostrar "No se encontraron resultados". |
| **404** | `ERR_DNI_NOT_FOUND` | DNI no encontrado | Mostrar "No se encontró el usuario con ese DNI". |
| **404** | `ERR_DB_EMPTY` | Base de datos vacía | Mostrar "No hay usuarios registrados". |
| **409** | `ERR_EMAIL_EXISTS` | Email ya registrado | Mostrar "Este correo ya está registrado". |
| **409** | `ERR_DUPLICATE_RESOURCE` | Recurso duplicado | Mostrar mensaje de duplicado genérico. |
| **500** | `ERR_INTERNAL_SERVER` | Error interno del servidor | Mostrar "Error inesperado, intenta más tarde". |
| **500** | `ERR_DATA_ACCESS` | Error de acceso a datos | Mostrar "Error inesperado, intenta más tarde". |

---

## 🛠️ 5. Restricciones de Validación (Formularios)

| Campo | Validación | Regex Sugerido |
| :--- | :--- | :--- |
| **DNI** | 6-10 dígitos, no inicia en 0 | `^[1-9][0-9]{5,9}$` |
| **Phone** | 6-10 dígitos numéricos | `^[0-9]{6,10}$` |
| **Email** | Formato email estándar | `^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$` |
| **Password** | Mín. 8 chars, 1 mayúscula, 1 minúscula, 1 número | `^(?=.*[a-z])(?=.*[A-Z])(?=.*\d).{8,}$` |
| **Roles** | Solo valores permitidos | `user` \| `admin` |

---

## 📋 6. Resumen de Endpoints

| Método | Endpoint | Auth | Descripción |
| :--- | :--- | :---: | :--- |
| `POST` | `/api/auth/login` | ❌ | Login, obtiene tokens |
| `POST` | `/api/auth/register` | ❌ | Registro de nuevo usuario |
| `POST` | `/api/auth/revoke` | ❌ | Logout, invalida el refresh token |
| `POST` | `/api/auth/validate` | ❌ | Verifica si un token es válido |
| `GET` | `/api/users` | ✅ | Lista todos los usuarios |
| `GET` | `/api/users/id/{id}` | ✅ | Obtiene un usuario por ID |
| `GET` | `/api/users/dni/{dni}` | ✅ | Obtiene un usuario por DNI |
| `GET` | `/api/users/search?name=` | ✅ | Busca usuarios por nombre |
| `PUT` | `/api/users/{id}` | ✅ | Actualiza datos de un usuario |
| `DELETE` | `/api/users/{id}` | ✅ | Elimina un usuario |
