# 🏗️ Estándar de Arquitectura: Microservicios Pandebon

Este documento define la arquitectura oficial para el ecosistema de microservicios de Pandebon. Está diseñado para ser escalable, testeable y fácil de replicar (ej: para servicios de Inventario, Pedidos, etc.).

---

## 1. Patrón Arquitectónico: Arquitectura Hexagonal
Separamos la lógica de negocio (Dominio) de la tecnología (Infraestructura).

### Estructura de Paquetes (Package Layout)
```text
microservice.[nombre-servicio]
├── application             # ⚙️ APLICACIÓN: Orquestación (Implementación de los puertos de entrada).
├── domain                  # 🧠 NÚCLEO: Lógica pura, sin frameworks.
│   ├── models              # Objetos de negocio (POJOs).
│   ├── ports               # Contratos (Interfaces).
│   │   ├── in              # Casos de Uso (Lo que entra al sistema).
│   │   └── out             # Persistencia/Externos (Lo que el sistema pide).
│   ├── services            # Implementación de los Casos de Uso.
│   └── exception           # Excepciones de negocio personalizadas.
│       └── error           # Enums con Códigos de Error y HTTP Status.
├── infrastructure          # 🛠️ ADAPTADORES: Implementación técnica.
│   ├── config              # Security, Beans, GlobalExceptionHandler.
│   ├── driver              # Entrada: REST Controllers, DTOs, Mappers.
│   └── adapter             # Salida: Implementación JPA, Clients, DB Entities.
└── resources               # application.properties, SQL scripts.
```

---

## 2. Gestión de Errores Centralizada (El Estándar)
Todos los microservicios deben reportar errores de la misma forma para facilitar la integración con el Frontend.

1.  **ErrorCode (Enum):** Cada error tiene un código (ej: `ERR_NOT_FOUND`), un mensaje legible y un código de estado HTTP.
2.  **BusinessException:** Una excepción base que captura el `ErrorCode`.
3.  **GlobalExceptionHandler:** Captura todas las excepciones y las devuelve en un JSON estándar:
    ```json
    {
      "code": "ERR_USER_NOT_FOUND",
      "message": "User not found"
    }
    ```

---

## 3. Mapeo de Datos (Lombok + MapStruct)
- **Lombok:** Para eliminar código repetitivo (Getters, Setters, Builders).
- **MapStruct:** Para transformar datos entre capas.
    - `Entity` ↔ `Model` (En la capa de Adapter).
    - `Model` ↔ `DTO` (En la capa de Driver).
    *Regla:* El Dominio nunca debe conocer los DTOs ni las Entidades de base de datos.

---

## 4. Despliegue y DevOps (Render/Docker)
Para que el despliegue en Render sea exitoso:
- **pom.xml:** Debe incluir el `spring-boot-maven-plugin` para generar un "Fat JAR".
- **Dockerfile:** Multi-stage build (Maven -> JRE 21).
- **Puerto:** Usar siempre `ENTRYPOINT ["sh", "-c", "java -Dserver.port=${PORT:-8080} -jar app.jar"]` para que Render asigne el puerto dinámicamente.
- **BD:** La URL debe incluir `sslmode=require` para PostgreSQL en la nube.

---

## 5. Checklist para un Nuevo Microservicio (Ej: Inventario)
1. [ ] Clonar estructura de carpetas.
2. [ ] Copiar `pom.xml` y actualizar `artifactId` y dependencias.
3. [ ] Definir el `ErrorCode` del nuevo dominio.
4. [ ] Implementar el `GlobalExceptionHandler` (puedes copiar el del micro de Login).
5. [ ] Configurar variables de entorno en el `application.properties`.
6. [ ] Crear Dockerfile basado en el estándar.

---
*Documento generado para la estandarización de procesos de desarrollo en PANDEBON.*
