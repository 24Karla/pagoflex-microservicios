# PagoFlex — microservicios iniciales

Este repositorio incluye ocho servicios independientes, siguiendo la organización del ejemplo MiRegistro:

La plataforma también incluye `eureka-server` para descubrimiento de servicios y `api-gateway` como punto de entrada HTTP.

| Servicio | Puerto local | Base de datos | Responsabilidad |
|---|---:|---|---|
| `roles-service` | 8081 | `pagoflex_roles` | Catálogo de roles de la aplicación |
| `usuarios-service` | 8082 | `pagoflex_usuarios` | Registro, consulta e inicio de sesión de usuarios |
| `auditoria-service` | 8083 | `pagoflex_auditoria` | Registro histórico de acciones |
| `compromisos-service` | 8084 | `pagoflex_compromisos` | Compromisos de pago y sus cuotas |
| `empresas-service` | 8085 | `pagoflex_empresas` | Empresas asociadas a ejecutivos |
| `pagos-service` | 8086 | `pagoflex_pagos` | Pagos registrados y comprobantes |
| `recomendaciones-service` | 8087 | `pagoflex_recomendaciones` | Recomendaciones de organizaciones |
| `reportes-service` | 8088 | `pagoflex_reportes` | Incidencias y gestión de reportes |
| `eureka-server` | 8761 | — | Registro y descubrimiento de servicios |
| `api-gateway` | 8080 | — | Enrutamiento hacia los microservicios registrados |

Cada servicio tiene su propia base. `usuarios-service` guarda el identificador `rolId` y consulta `roles-service` por HTTP cuando necesita validar o mostrar el rol. No se declara una relación JPA entre bases de servicios distintos.

## Cómo iniciar

1. Inicia MySQL en `localhost:3306`.
2. Revisa `DB_USERNAME` y `DB_PASSWORD` en cada servicio si tus credenciales son distintas a `root` sin contraseña.
3. Inicia `eureka-server` y luego los ocho microservicios. Cada microservicio se registra automáticamente en Eureka. Inicia `api-gateway` después de Eureka; puede iniciarse antes que los servicios.

Inicia Eureka primero, desde la raíz del repositorio:

```powershell
cd eureka-server
mvn spring-boot:run
```

En otra terminal, inicia `roles-service`:

```powershell
cd roles-service
.\mvnw.cmd spring-boot:run
```

Inicia los demás servicios en terminales separadas (roles primero para atender las consultas actuales de usuarios) y finalmente `api-gateway`, usando `mvn spring-boot:run` en los proyectos nuevos. El panel de Eureka está disponible en `http://localhost:8761`.

Los puertos, conexión y URL del servicio de roles se pueden cambiar con las variables `SERVER_PORT`, `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`, `ROLES_SERVICE_URL` y `EUREKA_URL` (por defecto `http://localhost:8761/eureka/`).

El gateway escucha en `http://localhost:8080` y conserva las rutas `/api/...` de cada servicio. Por ejemplo, `GET http://localhost:8080/api/roles` se enruta a `roles-service`; las rutas de los ocho servicios se balancean usando su nombre registrado en Eureka.

Al iniciar, `roles-service` crea los roles usados por PagoFlex si todavía no existen: `1 Agente`, `2 Ejecutivo` y `3 Usuario Final`.

## Endpoints disponibles

### Roles — `http://localhost:8081`

| Método | Ruta | Resultado |
|---|---|---|
| GET | `/api/roles` | Lista de roles |
| GET | `/api/roles/{id}` | Rol solicitado; 404 si no existe |

### Usuarios — `http://localhost:8082`

| Método | Ruta | Resultado |
|---|---|---|
| GET | `/api/usuarios?buscar=texto` | Lista usuarios; búsqueda opcional por nombre, cuenta o RUT |
| GET | `/api/usuarios/{id}` | Usuario sin contraseña; 404 si no existe |
| POST | `/api/usuarios` | Registra un usuario; 201, 400 por datos incompletos, 409 por cuenta o RUT repetido |
| POST | `/api/usuarios/login` | Inicia sesión; 200 o 401 por credenciales inválidas |
| PUT | `/api/usuarios/{id}` | Actualiza nombre, cuenta, ciudad, RUT y rol |
| PUT | `/api/usuarios/{id}/contrasena` | Cambia la contraseña verificando la actual; JSON `{ "actual": "...", "nueva": "..." }` |
| DELETE | `/api/usuarios/{id}` | Elimina el usuario y su avatar |
| POST | `/api/usuarios/{id}/avatar` | Sube una imagen multipart en el campo `archivo` (máximo 5 MB) |
| GET | `/api/usuarios/{id}/avatar` | Devuelve la fotografía JPEG del perfil |

### Auditoría — `http://localhost:8083`

| Método | Ruta | Resultado |
|---|---|---|
| GET | `/api/auditoria?actorUsuarioId=1` | Eventos recientes; filtro opcional por actor |
| GET | `/api/auditoria/{id}` | Evento; 404 si no existe |
| POST | `/api/auditoria` | Añade un evento inmutable; el servicio asigna la fecha |

### Compromisos — `http://localhost:8084`

| Método | Ruta | Resultado |
|---|---|---|
| GET | `/api/compromisos?usuarioId=1` | Lista; admite filtro por `agenteId` o `empresaId` |
| GET | `/api/compromisos/{id}` | Compromiso; 404 si no existe |
| POST | `/api/compromisos` | Crea compromiso; estado `Pendiente` por defecto |
| PUT | `/api/compromisos/{id}` | Actualiza el compromiso |
| DELETE | `/api/compromisos/{id}` | Elimina el compromiso |

### Empresas — `http://localhost:8085`

| Método | Ruta | Resultado |
|---|---|---|
| GET | `/api/empresas?ejecutivoId=1` | Lista; filtro opcional por ejecutivo o cuenta demo |
| GET | `/api/empresas/{id}` | Empresa; 404 si no existe |
| POST | `/api/empresas` | Crea empresa; recargo fijo cero por defecto |
| PUT | `/api/empresas/{id}` | Actualiza la empresa |
| DELETE | `/api/empresas/{id}` | Elimina la empresa |

### Pagos y comprobantes — `http://localhost:8086`

| Método | Ruta | Resultado |
|---|---|---|
| GET | `/api/pagos?usuarioId=1` | Lista; filtro opcional por usuario o compromiso |
| GET | `/api/pagos/{id}` | Pago; 404 si no existe |
| POST | `/api/pagos` | Registra el pago y crea un comprobante con folio |
| GET | `/api/pagos/{id}/comprobante` | Comprobante del pago |
| GET | `/api/pagos/comprobantes?usuarioId=1` | Comprobantes del usuario |

Este servicio registra operaciones y emite folios para el flujo actual de la app; no se conecta a un banco ni a una pasarela de pago.

### Recomendaciones — `http://localhost:8087`

| Método | Ruta | Resultado |
|---|---|---|
| GET | `/api/recomendaciones?agenteId=1` | Lista; filtro opcional por agente |
| GET | `/api/recomendaciones/{id}` | Recomendación; 404 si no existe |
| POST | `/api/recomendaciones` | Registra recomendación con autorización de contacto |
| PUT | `/api/recomendaciones/{id}` | Actualiza recomendación y estado |
| DELETE | `/api/recomendaciones/{id}` | Elimina la recomendación |

### Reportes — `http://localhost:8088`

| Método | Ruta | Resultado |
|---|---|---|
| GET | `/api/reportes?usuarioId=1` | Lista; filtro opcional por usuario o agente asignado |
| GET | `/api/reportes/{id}` | Reporte; 404 si no existe |
| POST | `/api/reportes` | Crea reporte con estado inicial `Abierto` |
| PUT | `/api/reportes/{id}` | Asigna agente y actualiza estado y respuesta |

El registro espera `nombre`, `rut`, `cuenta`, `ciudad` y `contrasena`; puede incluir `rolId` (1 Agente, 2 Ejecutivo, 3 Usuario Final; por defecto 3). Se usa `cuenta` porque así se llama el identificador en el modelo Android; en la pantalla actual representa un correo electrónico. El RUT se envía como texto, por ejemplo `12345678-9`. El login puede incluir `rolId` para comprobar que coincide con el rol de la cuenta. Las contraseñas nuevas se guardan con BCrypt y nunca se incluyen en las respuestas.

Ejemplo de registro:

```json
{
  "nombre": "Ana Torres",
  "rut": "12345678-9",
  "cuenta": "ana@example.com",
  "ciudad": "Santiago",
  "contrasena": "claveSegura123",
  "rolId": 3
}
```

La app Android puede apuntar a `http://10.0.2.2:8080/` desde el emulador Android para usar el gateway. En un teléfono físico, usa la dirección IP local del computador. Las URLs directas de los servicios (8081–8088) siguen disponibles para desarrollo. Los avatares se almacenan en `uploads/avatars` por defecto; se puede cambiar con `AVATAR_STORAGE_DIR`.

## Para qué sirve cada carpeta

- `model`: clases que representan las tablas persistidas con JPA.
- `repository`: operaciones de acceso a datos generadas por Spring Data.
- `service`: validaciones, reglas de negocio y conversión a respuestas.
- `controller`: rutas HTTP y códigos de estado.
- `dto`: contratos JSON de entrada y salida, separados de las entidades.
- `client`: llamadas HTTP entre servicios; aquí usuarios consulta roles.
- `config`: configuración técnica y datos iniciales.
- `src/main/resources/application.properties`: configuración externa de puerto, base de datos y servicios relacionados.
- `pom.xml`: dependencias y compilación Maven.
- `*ServiceApplication.java`: punto de entrada de cada aplicación Spring Boot.

El ZIP de referencia contiene ejemplos adicionales (por ejemplo actualización, cambio de contraseña y fotos). Esas funciones no se copian aún porque PagoFlex y los servicios actuales todavía no tienen contratos definidos para ellas. La app Android protege contraseñas locales con PBKDF2 y salt; las cuentas existentes necesitarán una estrategia de migración antes de autenticarse contra este backend, que guarda cuentas nuevas con BCrypt. En una versión de producción, el alta pública tampoco debería permitir escoger roles privilegiados.
