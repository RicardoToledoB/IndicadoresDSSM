# ENTREGA PARA SERVIDOR UBUNTU — 23/11/2026

Se configuró una clave MySQL nueva y privada en `src/main/resources/application.properties`,
y la **misma clave** en `scripts/01_CREAR_BD_Y_USUARIO.sql`.
Las credenciales completas se entregan en `ACCESO_INICIAL.txt`.

**Instrucciones vigentes:** `docs/INSTALAR_UBUNTU_MYSQL_SWAGGER.md`.
Esta entrega escucha en `127.0.0.1:8097` por seguridad, preparada para Apache ProxyPass sobre el mismo servidor.

Migraciones Flyway V1–V4 originales intactas, datos R04 no modificados.
Swagger UI integrado para documentacion local/intranet. JWT integrado.

El proyecto pre-Swagger compiló y arrancó en macOS, con 6 pruebas verdes y 4 migraciones exitosas
según las verificaciones aportadas por el usuario. Esta nueva entrega debe verificarse en Ubuntu.

---

# RRFF-SSM | Backend Spring Boot

## 0. NUEVO — prioridad máxima: módulo 7.2 Carta Gantt R04/R05

Esta edición **amplía** el backend M05 anterior sin borrar sus API ni tablas. Incluye:

- Migraciones Flyway `V2` estructura de **37 tablas**, `V3` **8.712 registros R04 originales**, y `V4` **87 claves foráneas** con índices automáticos; todas en MySQL 8.0 bajo prefijo `r72_`.
- Se respetan íntegramente los IDs Excel (`PR001`, `CR001-01`, `T000001`, `PE0001`, `US0001`, etc.). Los usuarios `r72_users` **NO** son las cuentas JWT del M05 y siguen inactivos/provisionales.
- **25 recursos REST** del OpenAPI R05 (GET paginado, GET por ID, POST y PUT condicionado a autorización), bajo `/api/v1/rrff/`. DTO JSON camelCase y columnas MySQL snake_case.
- `GET /api/v1/rrff/projects/{projectId}/gantt?scheduleId={scheduleId}` entrega proyecto, corte, tareas, responsables/personas, catálogos y dependencias en una sola respuesta.
- `GET /api/v1/rrff/projects/{projectId}/gantt/{scheduleId}/progress` expone valor de fuente en bruto, cobertura de avances y `computedProgressPercent=null` hasta validación de fórmula y signos.
- `POST /api/v1/rrff/projects/{projectId}/gantt/{scheduleId}/clone` crea una NUEVA versión, con nuevos IDs técnicos, jerarquía y asignaciones; jamás sobrescribe originales R04. Es una operación condicionada a permiso de escritura.
- Los 12 libros únicos, 73 cortes, 4.425 tareas, 3.597 asignaciones y demás datos del Excel se cargan realmente. No se inventan valores ausentes; 17 tablas quedan sin filas de origen.
- Archivo original maestro R04, PostgreSQL R05 de referencia y OpenAPI R05 en `referencias_r04_r05/`; auditoría de recuentos y referencias en `validacion_importacion_r04.json`.

**Diferencia deliberada frente al R05 SQL recibido:** el original es PostgreSQL (`CREATE SCHEMA`, `TIMESTAMPTZ`, `DEFERRABLE`) y no se debe ejecutar en MySQL. Nuestra implementación MySQL 8.0 usa `r72_` como prefijo, sin esquema separado. Primero se crean tablas sin FKs; después se importa R04; por último se agregan las FKs para conservar la relación circular `projects ↔ source_files` sin deshabilitar la integridad.

**Seguridad:** misma autenticación JWT M05. `ROLE_ADMIN` para catálogo de personas y usuarios provisionales; lectura 7.2 para roles de Reportabilidad; escritura `app.features.writes-enabled=false` por defecto, y requiere `ROLE_EDITOR_7` o `ROLE_ADMIN` después de habilitar. La aprobación institucional es un requisito previo. Los cortes `CR...-..` y las tareas `T000...` importadas son **inmutables** incluso después de habilitar escritura; clonar para crear una nueva versión. Las actualizaciones de avance se agregan, no se sobrescriben.

**Ejemplos** (reemplazar TOKEN obtenido con `/api/v1/auth/login`):

```bash
curl -H 'Authorization: Bearer TOKEN' 'http://localhost:8097/api/v1/rrff/projects?page=0&size=25'
curl -H 'Authorization: Bearer TOKEN' 'http://localhost:8097/api/v1/rrff/schedules?projectId=PR001'
curl -H 'Authorization: Bearer TOKEN' 'http://localhost:8097/api/v1/rrff/projects/PR001/gantt?scheduleId=CR001-01'
curl -H 'Authorization: Bearer TOKEN' 'http://localhost:8097/api/v1/rrff/projects/PR001/gantt/CR001-01/progress'
```

Abrir Postman `postman/RRFF-72-R05.postman_collection.json`. El contrato de origen R05 contiene **53 paths** y **26 esquemas**; la implementación cubre los 25 recursos base y tres rutas Gantt especiales. Las reglas de negocio y permisos PD no se presentan como aprobados.

**Validación ejecutada:** estructura Excel, PK/FK y datos importados comprobados estáticamente; ejecución de V2+V3 sobre motor SQLite de ensayo para confirmar 8.712 filas. Esto **no equivale a migración probada en MySQL**, ni a una compilación Maven; ambas deben realizarse en ambiente de pruebas, sin utilizar aún bases productivas.

---


**Entrega inicial de código, no despliegue institucional ni producto terminado.**

Fuente de diseño: **AG-RRFF-001/M05, V1.1 consolidada (05-11-2026)**. Esta entrega adopta las correcciones de la sección 22 con precedencia sobre secciones 1 a 21: 23 entidades **candidatas**, consulta diferenciada de escritura PD, y prioridad 7→3→1. No altera datos existentes ni afirma que sus contratos REST coincidan con APIs ya desplegadas.

## Stack

- Java 21, Spring Boot **3.5.16**, Maven.
- MySQL 8.0+, Spring Data JPA, Flyway (scripts `src/main/resources/db/migration`), UTC en timestamps persistidos.
- Spring Security, JWT **HS256** de duración corta (30 minutos), contraseñas **BCrypt**.
- URLs `/api/v1/`, `ProblemDetail` para errores, paginación y filtros, auditoría de escrituras exitosas.
- Sin Docker, sin exportar variables en terminal, configuración **directa** en `application.properties`.

## 1. Preparación MySQL (ambiente local/de pruebas)

```sql
CREATE DATABASE rrff_ssm CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER 'rrff_app'@'localhost' IDENTIFIED BY 'CAMBIAR_CONTRASENA_MYSQL';
GRANT SELECT, INSERT, UPDATE, DELETE, CREATE, ALTER, INDEX, REFERENCES ON rrff_ssm.* TO 'rrff_app'@'localhost';
-- Para operación institucional, separar usuario migrador (DDL) del usuario de aplicación (DML).
```

Editar `src/main/resources/application.properties` (sin subir contraseñas reales a Git):

- `spring.datasource.url`, `spring.datasource.username`, `spring.datasource.password`.
- `app.jwt.secret`: valor **único y aleatorio**, privado, de al menos 32 bytes. La aplicación **rechaza** la clave de ejemplo.
- `server.port=8097` (puerto de ejemplo configurable).
- `app.cors.allowed-origin=http://localhost:4200` para pruebas Angular. Configurar el dominio HTTPS realmente autorizado para producción.
- `spring.jpa.hibernate.ddl-auto=validate`: las tablas las crea Flyway, nunca `update` automático.

Para crear el primer administrador **solo en una base nueva**, configurar temporalmente:

```properties
app.bootstrap.enabled=true
app.bootstrap.username=admin
app.bootstrap.password=ESCRIBIR_AQUI_UNA_CONTRASENA_PRIVADA_DE_AL_MENOS_14_CARACTERES
```

Iniciar una vez, comprobar acceso y volver a dejar `app.bootstrap.enabled=false`. Las contraseñas no se guardan en texto plano en la base: se almacenan como hash BCrypt. **No incorporar el properties con claves reales en un repositorio.** Restringir permisos de lectura del archivo en el servidor.

## 2. Compilar e iniciar

Desde el directorio raíz del proyecto:

```bash
mvn clean verify
mvn spring-boot:run
```

También se puede ejecutar el JAR obtenido desde Maven:

```bash
java -jar target/rrff-ssm-backend-0.1.0-SNAPSHOT.jar
```

> Esta entrega de código se ha revisado estáticamente; **no se ejecutó una compilación Maven ni una prueba contra MySQL en este entorno**. Es imprescindible compilar, probar migraciones y revisar seguridad antes de considerar el backend operativo.

Prueba HTTP básica:

```bash
curl http://localhost:8097/actuator/health
```

## 3. Login JWT

```bash
curl -X POST 'http://localhost:8097/api/v1/auth/login' \
  -H 'Content-Type: application/json' \
  -d '{"username":"admin","password":"SU_CLAVE_PRIVADA"}'
```

La respuesta contiene `accessToken`, `expiresInSeconds`, `tokenType` y `roles`. Usar en las demás operaciones:

```http
Authorization: Bearer <token>
```

También se incluyen llamadas en `postman/RRFF-SSM.postman_collection.json` (importar a Postman y configurar `password`).

## 4. Primeras consultas y flujo de ensayo

Con token de un usuario que tenga `ROLE_ADMIN` o roles de lectura de la materia:

```http
GET /api/v1/catalogos/modulos
GET /api/v1/proyectos?page=0&size=20&q=salud
GET /api/v1/proyectos/{id}
GET /api/v1/minutas?ref={proyectoId}
GET /api/v1/gantt?ref={proyectoId}
GET /api/v1/inversiones?ref={proyectoId}
GET /api/v1/convenios
GET /api/v1/correspondencia
GET /api/v1/correlativos
GET /api/v1/crc
GET /api/v1/reportabilidad/proyectos/{id}/vista-ejecutiva?fechaCorte=2026-11-18
```

**Nota:** las listas comienzan vacías: no se entregaron bases institucionales para cargar. Las cantidades ausentes no se transforman en montos cero.

## 5. Habilitar escrituras solo para QA autorizado

Por defecto:

```properties
app.features.writes-enabled=false
```

De este modo, POST/PATCH de negocio devuelve **403**. La autorización institucional **no se sustituye por esta bandera**: antes de encenderla, validaciones funcionales, alcance por registro, roles, carga de datos, y plan de pruebas. Tras aprobación formal y solo en ambiente de ensayo, establecer `true` para activar estas operaciones candidatas:

- `POST /api/v1/proyectos`, `PATCH /api/v1/proyectos/{id}` con `expectedRowVersion`.
- `POST /api/v1/inversiones`, `POST /api/v1/convenios`, `POST /api/v1/adendas`.
- `POST /api/v1/hitos`, `POST /api/v1/convenios-proyectos`.
- `POST /api/v1/minutas`, `POST /api/v1/gantt` (crean versión; no sobrescriben otra).
- `POST /api/v1/correspondencia`, `POST /api/v1/derivaciones`.

**No existen** endpoints para reservar/asignar correlativos automáticamente, emitir documentos oficiales, publicar minutas, certificar vigencia de convenios, cambiar estados formales, exportar reportes oficiales o subir/descargar archivos. Todos requieren decisiones y controles adicionales (sección 22).

### Ejemplo POST proyecto (solo QA autorizado)

```bash
curl -X POST http://localhost:8097/api/v1/proyectos \
  -H 'Content-Type: application/json' -H 'Authorization: Bearer TOKEN' \
  -d '{"nombre":"PROYECTO DE PRUEBA","codigoBip":null,"comuna":"Punta Arenas","etapa":null,"prioridad":null,"fuenteReferencia":"DATOS DE ENSAYO","periodoFuente":2026}'
```

El `id` retornado es técnico: **BIP no es PK** ni se aplica UNIQUE global. `rowVersion` acompaña a la ficha para evitar actualizaciones concurrentes obsoletas.

## 6. Roles y autorización

| Rol de consulta | Módulos de lectura (según destinatarios documentados) |
| --- | --- |
| `ROLE_DIRECTOR` | 7, 3 |
| `ROLE_SUBDIRECTOR_RRFF` | 7, 3, 1 |
| `ROLE_JEFE_RRFF` | 3, 1 |
| `ROLE_ARTICULACION` | 7, 3, 1 |
| `ROLE_SECRETARIA` | 1 |
| `ROLE_ADMIN` | administración técnica; acceso amplio **de ensayo** |

`ROLE_EDITOR_1`, `ROLE_EDITOR_3` y `ROLE_EDITOR_7` son roles **propuestos** y no autorizaciones conferidas por el documento. Requieren además `writes-enabled=true`. Los módulos 2,4,5,6 no tienen perfiles institucionales acreditados: consulta técnica limitada a administrador en este prototipo.

La tabla conceptual `perfil_permiso` existe en MySQL, pero **todavía no se implementa un motor de permisos por objeto/ámbito/fecha**; los documentos permanecen restringidos a administrador. Por tanto, este backend **no debe usarse con información reservada de producción** hasta completar ACL, clasificación y revisión normativa.

El JWT expira a 30 minutos; el cambio de roles o inhabilitación de un usuario **no revoca un token ya emitido**. Implementar revocación/rotación y límites de intentos antes de producción.

## 7. Estructura

```
src/main/java/cl/dssm/rrff/
  auth/         login y token JWT
  config/       Spring Security, Bootstrap admin
  domain/       23 entidades candidatas + Usuario y BaseEntity
  repository/   consultas JPA
  service/      control de acceso, auditoría, validaciones
  api/          consultas, escritura condicionada, vistas, administración
src/main/resources/
  application.properties
  db/migration/V1__schema_inicial.sql
postman/
docs/
```

Se modelaron las entidades separadas: **CORRESPONDENCIA, CORRELATIVO y CRC**; **INSTRUMENTO_ORGANICO** solo referencia DOCUMENTO; y **PROYECTO** centraliza los vínculos de Reportabilidad e Inversiones. Las 23 entidades son una **interpretación técnica**, no tablas que se hayan verificado preexistentes.

## 8. Despliegue Apache (orientativo, no ejecutar sin aprobación)

En un virtual host HTTPS propio, y con el backend escuchando solamente en loopback o protegido por firewall:

```apache
ProxyPreserveHost On
ProxyPass        "/api/" "http://127.0.0.1:8097/api/"
ProxyPassReverse "/api/" "http://127.0.0.1:8097/api/"
```

El uso de un nombre DNS o vhost debe definirse con TIC. Antes de exponer la API a Internet: TLS, límites de petición, registro seguro, anti-fuerza-bruta, configuración de CORS real, respaldo probado, revisión del código y pruebas de permisos.

## 9. Límites y próximos hitos

Consultar `docs/PENDIENTES_Y_ALCANCE.md` y `docs/ENDPOINTS.md`. Esta entrega es **primera base del backend**; no se considera homologación definitiva del diseño ni código listo para producción. La etapa 1 requiere pruebas funcionales con datos de ensayo; etapas 2 y 3 consolidarán catálogos, flujos, adjuntos y política de autorización. La sección 9 del documento condiciona el pase productivo a pruebas institucionales y aprobación expresa.
