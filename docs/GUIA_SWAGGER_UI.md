# RRFF-SSM — Swagger UI (versión integrada)

## Arranque

En la carpeta `rrff-ssm-backend`:

```bash
mvn clean verify
mvn spring-boot:run
```

No necesitas ejecutar scripts SQL ni volver a cargar el R04. Flyway no debe repetir las migraciones aplicadas.

## Enlaces

* Swagger UI: http://localhost:8097/swagger-ui/index.html
* OpenAPI de los controladores implementados: http://localhost:8097/v3/api-docs
* Especificación R05 de referencia: http://localhost:8097/docs/rrff-7-2-r05-openapi.json
* Salud: http://localhost:8097/actuator/health

En la parte superior de Swagger UI hay dos especificaciones:

* **API implementada (desde Spring Boot)**: refleja las rutas reales de los controladores. El 7.2 usa rutas genéricas `/{resource}` para los recursos del catálogo.
* **R05 propuesta (referencia del maestro)**: reproduce el contrato OpenAPI suministrado; documenta recursos por separado, pero **no prueba que cada operación esté implementada**.

## Probar con JWT

1. En Swagger UI, selecciona **API implementada**.
2. Ejecuta `POST /api/v1/auth/login` con tu usuario y contraseña institucionales configurados en el arranque inicial.
3. Copia `accessToken` de la respuesta, sin compartirlo.
4. Pulsa **Authorize**, pega solo el token JWT (sin prefijo Bearer si la interfaz lo añade) y confirma.
5. Ejecuta una consulta como `GET /api/v1/rrff/projects/PR001/gantt?scheduleId=CR001-01`.

**Seguridad:** no publicar Swagger y `/v3/api-docs` directamente en internet. Limitar acceso al servidor o a la red privada de TIC. No almacenar contraseñas ni tokens en capturas. Las APIs de negocio siguen exigiendo JWT.

## Cambio frente al ZIP anterior

Se agregó `springdoc-openapi-starter-webmvc-ui` versión 2.10.1, un bean OpenAPI con esquema Bearer JWT y rutas de documentación en Spring Security. Se preservaron las migraciones V1 a V4, los datos, el JWT y la configuración de MySQL.

La sintaxis de Java / XML se verificó estáticamente. Compilación Maven y apertura real de Swagger deben comprobarse en el entorno de desarrollo. 
