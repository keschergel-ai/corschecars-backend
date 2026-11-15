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
$jwtBytes = New-Object byte[] 32
$rng = [Security.Cryptography.RandomNumberGenerator]::Create()
$rng.GetBytes($jwtBytes)
$env:APP_JWT_SECRET = [Convert]::ToBase64String($jwtBytes)
$rng.Dispose()
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

La clave JWT debe ser Base64 y contener al menos 32 bytes aleatorios. El ejemplo la genera en la sesión actual de PowerShell; no la publiques. Si cambias la clave, los tokens emitidos con la anterior dejan de ser válidos.

## Endpoints

| Método | Ruta | Acción |
|---|---|---|
| POST | `/api/auth/register` | Registra una cuenta y devuelve un JWT |
| POST | `/api/auth/login` | Inicia sesión y devuelve un JWT |
| GET | `/api/vehiculos` | Lista el catálogo; público |
| GET | `/api/vehiculos/{id}` | Consulta un vehículo; público |
| POST | `/api/vehiculos` | Crea un vehículo; requiere JWT |
| PUT | `/api/vehiculos/{id}` | Actualiza un vehículo; requiere JWT |
| DELETE | `/api/vehiculos/{id}` | Elimina un vehículo; requiere JWT |
| POST | `/api/contactos` | Guarda una consulta de contacto; público |

Para las rutas protegidas, envía el token con el encabezado `Authorization: Bearer <token>`. Swagger permite autorizarlo con el botón **Authorize**.

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

El registro asigna el rol `USER` por defecto. El JWT incluye el rol y vence después del tiempo configurado en `APP_JWT_EXPIRATION_SECONDS` (una hora por defecto).
