# CorscheCars API

Backend REST separado del frontend React. Usa Java 21, Spring Boot, PostgreSQL (Supabase), Flyway y OpenAPI/Swagger.

## Requisitos

- JDK 21
- Maven 3.9+
- Un proyecto Supabase PostgreSQL

## Configuración en PowerShell

En Supabase, selecciona **Session pooler** y copia el host, puerto y usuario exactos. El usuario normalmente tiene el formato `postgres.<PROJECT_REF>`. No publiques la contraseña ni la guardes en el repositorio.

```powershell
$env:SUPABASE_DB_URL = "jdbc:postgresql://HOST:5432/postgres?sslmode=require"
$env:SUPABASE_DB_USERNAME = "postgres.<PROJECT_REF>"
$env:APP_CORS_ALLOWED_ORIGIN = "http://localhost:5173"
$securePassword = Read-Host "Contraseña de la base de datos" -AsSecureString
$pointer = [Runtime.InteropServices.Marshal]::SecureStringToBSTR($securePassword)
try {
    $env:SUPABASE_DB_PASSWORD = [Runtime.InteropServices.Marshal]::PtrToStringBSTR($pointer)
} finally {
    [Runtime.InteropServices.Marshal]::ZeroFreeBSTR($pointer)
}
mvn spring-boot:run
```

La aplicación ejecuta las migraciones versionadas de Flyway al iniciar dentro del esquema `corschecars`, separado de `public`. Flyway crea ese esquema si todavía no existe. La primera migración crea `vehicles` y carga el catálogo de demostración. Hibernate valida el esquema, pero no lo modifica automáticamente.

## Endpoints de vehículos

| Método | Ruta | Acción |
|---|---|---|
| GET | `/api/vehiculos` | Lista el catálogo |
| GET | `/api/vehiculos/{id}` | Consulta un vehículo |
| POST | `/api/vehiculos` | Crea un vehículo |
| PUT | `/api/vehiculos/{id}` | Actualiza un vehículo |
| DELETE | `/api/vehiculos/{id}` | Elimina un vehículo |

Ejemplo de cuerpo para `POST` y `PUT`:

```json
{
  "nombre": "Toyota Corolla 2023",
  "precio": 15990000,
  "categoria": "Seminuevos",
  "imagen": "https://example.com/vehiculo.jpg",
  "descripcion": "Sedán seminuevo."
}
```

Swagger UI queda disponible en `http://localhost:8080/swagger-ui.html`; el documento OpenAPI JSON, en `http://localhost:8080/v3/api-docs`.

> En esta primera etapa las rutas CRUD todavía no tienen autenticación. No publiques esta API ni la uses con datos reales hasta completar la etapa de JWT y roles.
