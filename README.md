# Consultorio Microservicios

Backend de gestión de un consultorio médico, dividido en microservicios independientes construidos con **Spring Boot 3** y **Java 17**.

## Arquitectura

| Servicio | Puerto | Base de datos | Descripción |
|---|---|---|---|
| `pacientes-service` | `9001` | `servicio_pacientes` (MySQL) | ABM de pacientes. |
| `turnos-service` | `9002` | `servicio_turnos` (MySQL) | ABM de turnos. Al crear/editar un turno, valida el DNI del paciente contra `pacientes-service` y guarda su nombre completo. |

`turnos-service` se comunica con `pacientes-service` vía HTTP (`RestClient`) usando la URL configurada en `pacientes.service.url` (por defecto `http://localhost:9001`). Si el DNI no corresponde a ningún paciente, `turnos-service` devuelve `404`.

```
turnos-service --(GET /api/v1/pacientes/dni/{dni})--> pacientes-service
```

## Requisitos

- Java 17
- Maven 3.9+
- MySQL 8 corriendo en `localhost:3306`

## Puesta en marcha

Crear las bases de datos (Hibernate genera las tablas con `ddl-auto=update`):

```sql
CREATE DATABASE servicio_pacientes;
CREATE DATABASE servicio_turnos;
```

Variables de entorno opcionales (por defecto `root`/`root`):

```
DB_USERNAME=root
DB_PASSWORD=root
PACIENTES_SERVICE_URL=http://localhost:9001
```

Levantar cada servicio (en terminales separadas):

```bash
cd pacientes-service
./mvnw spring-boot:run

cd turnos-service
./mvnw spring-boot:run
```

## API

### Pacientes (`pacientes-service`, puerto 9001)

Base path: `/api/v1/pacientes`

| Método | Endpoint | Descripción |
|---|---|---|
| `POST` | `/api/v1/pacientes` | Crear paciente |
| `GET` | `/api/v1/pacientes` | Listar pacientes |
| `GET` | `/api/v1/pacientes/{id}` | Obtener paciente por ID |
| `GET` | `/api/v1/pacientes/dni/{dni}` | Obtener paciente por DNI |
| `PUT` | `/api/v1/pacientes/{id}` | Editar paciente |
| `DELETE` | `/api/v1/pacientes/{id}` | Eliminar paciente |

**Crear paciente**

```http
POST /api/v1/pacientes
Content-Type: application/json

{
  "nombre": "Ana",
  "apellido": "García",
  "dni": "30123456",
  "fechaNac": "1990-05-14",
  "telefono": "+541122334455"
}
```

Respuesta `201 Created`:

```json
{
  "idPaciente": 1,
  "nombre": "Ana",
  "apellido": "García",
  "dni": "30123456",
  "fechaNac": "1990-05-14",
  "telefono": "+541122334455"
}
```

Validaciones: `nombre`/`apellido` requeridos (máx. 50 caracteres), `dni` requerido (7 u 8 dígitos, único), `fechaNac` requerida y debe ser pasada, `telefono` opcional (formato `+?\d{6,15}`).

### Turnos (`turnos-service`, puerto 9002)

Base path: `/api/v1/turnos`

| Método | Endpoint | Descripción |
|---|---|---|
| `POST` | `/api/v1/turnos` | Crear turno (valida el DNI contra `pacientes-service`) |
| `GET` | `/api/v1/turnos` | Listar turnos |
| `GET` | `/api/v1/turnos/{id}` | Obtener turno por ID |
| `PUT` | `/api/v1/turnos/{id}` | Editar turno |
| `DELETE` | `/api/v1/turnos/{id}` | Eliminar turno |

**Crear turno**

```http
POST /api/v1/turnos
Content-Type: application/json

{
  "fecha": "2026-08-01",
  "tratamiento": "Control general",
  "dniPaciente": "30123456"
}
```

Respuesta `201 Created`:

```json
{
  "idTurno": 1,
  "fecha": "2026-08-01",
  "tratamiento": "Control general",
  "dniPaciente": "30123456",
  "nombreCompletoPaciente": "Ana García"
}
```

Validaciones: `fecha` requerida y debe ser hoy o futura, `tratamiento` requerido (máx. 100 caracteres), `dniPaciente` requerido (7 u 8 dígitos).

**Errores**

Los errores se devuelven como [`ProblemDetail`](https://www.rfc-editor.org/rfc/rfc7807) (`application/problem+json`).

Turno con DNI de paciente inexistente → `404 Not Found`:

```json
{
  "type": "about:blank",
  "title": "Not Found",
  "status": 404,
  "detail": "No existe un paciente con DNI: 30123456",
  "timestamp": "2026-07-22T15:30:00Z"
}
```

Error de validación → `400 Bad Request`:

```json
{
  "type": "about:blank",
  "title": "Bad Request",
  "status": 400,
  "detail": "Error de validación en la solicitud",
  "timestamp": "2026-07-22T15:30:00Z",
  "errores": {
    "dniPaciente": "must match \"\\d{7,8}\""
  }
}
```
