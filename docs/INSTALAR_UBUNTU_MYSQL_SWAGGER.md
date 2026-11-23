# RRFF-SSM — instalacion inicial Ubuntu con MySQL y Swagger (23/11/2026)

Esta entrega usa **Java 21, Spring Boot 3.5.16, MySQL y Flyway V1–V4**, conserva el backend M05 y
los 25 recursos conceptuales R05 del modulo **7.2 Carta Gantt** (incluyendo las rutas genericas implementadas).
Contiene credenciales generadas para esta instalacion en `ACCESO_INICIAL.txt`.

## 1. Ubicacion y dependencias

Colocar la carpeta `rrff-ssm-backend` en el servidor Ubuntu. Entrar a esa carpeta:

```bash
java -version
mvn -version
```

Requiere Java 21 y Maven 3.6.3 o superior, y MySQL 8.x local con permisos administrativos.

## 2. Crear la base de datos SIN ejecutar SQL R05 de PostgreSQL

**Antes de crear:** si `rrff_ssm` ya existe, verificar si tiene datos y no reemplazarlos.

```bash
sudo mysql -e "SHOW DATABASES LIKE 'rrff_ssm';"
sudo mysql < scripts/01_CREAR_BD_Y_USUARIO.sql
```

El script crea la base (si falta), el usuario (si falta) y concede permisos **solo en rrff_ssm**.
No modifica la clave de un usuario `rrff_app` ya existente.

**Verificar conexión sin poner la clave en terminal/historial**:

```bash
mysql --protocol=TCP -h 127.0.0.1 -P 3306 -u rrff_app -p rrff_ssm -e 'SHOW TABLES;'
```

La contraseña está en `ACCESO_INICIAL.txt`. **Si aparecen tablas previamente instaladas**,
verificar `flyway_schema_history` y no recrearlas ni borrar la base.

## 3. Compilar, iniciar y validar

```bash
mvn clean verify
mvn spring-boot:run
```

El servidor escuchara en **127.0.0.1:8097** (solo acceso local por seguridad).
En otra terminal **del mismo Ubuntu**:

```bash
curl -i http://127.0.0.1:8097/actuator/health
mysql -u rrff_app -p rrff_ssm -e "SELECT installed_rank,version,description,success FROM flyway_schema_history ORDER BY installed_rank; SELECT COUNT(*) FROM r72_projects; SELECT COUNT(*) FROM r72_schedules; SELECT COUNT(*) FROM r72_schedule_tasks;"
```

Se esperan Flyway V1,V2,V3,V4 y 12 proyectos, 73 cronogramas, 4425 tareas para una base nueva.
Si la app falla por migracion, no borres las tablas ni ejecutes manualmente los SQL: conserva el log.

## 4. Acceso a Swagger / JWT

- Health: http://127.0.0.1:8097/actuator/health
- Swagger: http://127.0.0.1:8097/swagger-ui/index.html
- OpenAPI implementada: http://127.0.0.1:8097/v3/api-docs
- Especificacion R05 de referencia: http://127.0.0.1:8097/docs/rrff-7-2-r05-openapi.json

Desde tu Mac, acceder al servicio en Ubuntu mediante tunel SSH (sustituir `usuario@servidor`):

```bash
ssh -L 8097:127.0.0.1:8097 usuario@servidor
```

Luego en el navegador del Mac: http://localhost:8097/swagger-ui/index.html
Si ya hay Apache como proxy inverso, publicar por HTTPS con control de acceso antes de exponer Swagger.

El admin inicial se crea solo en nueva base. Credenciales en `ACCESO_INICIAL.txt`.
Ejemplo curl: se recomienda usar Swagger o Postman para no registrar la contraseña en history.

## 5. Seguridad posterior al primer acceso

- `app.bootstrap.enabled=true` se usa solo para dar de alta al primer admin. Tras probar el login,
  cambiar a `false` en `src/main/resources/application.properties` y **recompilar el JAR**.
- `app.features.writes-enabled=false` sigue bloqueando escrituras institucionales pendientes de autorizacion.
- El mismo usuario MySQL dispone de permisos de migracion solo durante la instalacion. Antes de produccion,
  separar cuenta de migraciones y cuenta de ejecucion con privilegios minimos.
- Cambiar contrasena MySQL, admin y secreto JWT antes de produccion; no exponer `ACCESO_INICIAL.txt`,
  el ZIP ni el JAR (contiene claves embebidas) en `DocumentRoot`, repositorios o rutas publicas.
- Las cuentas provisionales `r72_users` no se convierten en cuentas JWT.
- **No ejecutar de nuevo una carga R04 sobre una base que ya tenga tablas y datos.** Flyway V3 registra ejecucion unica.

## 6. Notas de despliegue

Cuando uses `java -jar target/rrff-ssm-backend-0.1.0-SNAPSHOT.jar`, el JAR ya contiene
`application.properties` compilado: editar el archivo fuente exige volver a empaquetarlo
con `mvn clean package` para reflejar cambios.

La ruta `/v3/api-docs` documenta controladores realmente implementados; la especificacion R05
adjunta es referencia y no implica que cada ruta conceptual tenga un controlador separado.
