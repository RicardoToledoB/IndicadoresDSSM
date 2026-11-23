# Lista de comprobación de integración 7.2 R04/R05

## Datos importados (carga inicial, no son cifras de avance actual)
- `r72_projects`: 12; `r72_source_files`: 12.
- `r72_schedules`: 73; `r72_schedule_tasks`: 4.425.
- `r72_task_people`: 3.597; `r72_persons`: 34; `r72_users`: 34, **provisionales/inactivos**.
- `r72_source_actors`: 66; `r72_actor_users`: 66; `r72_person_users`: 34.
- Total en 20 tablas: 8.712. Las restantes 17 quedan vacías por falta de filas origen.
- Modelo: 37 tablas y 87 claves foráneas, auditadas contra el libro original.

## Despliegue de ensayo
1. Respaldar base `rrff_ssm` antes de actualizar un ambiente existente.
2. Confirmar `mysql --version` (MySQL 8.0+) y `java -version` (Java 21).
3. Configurar `application.properties` con claves privadas propias; `spring.jpa.hibernate.ddl-auto=validate`.
4. Ejecutar `mvn clean verify` y luego `mvn spring-boot:run` para aplicar V1..V4 en base nueva; en una base que ya tiene V1 se aplicarán sólo migraciones posteriores.
5. Confirmar Flyway con `SELECT installed_rank,version,description,success FROM flyway_schema_history ORDER BY installed_rank;`.
6. Confirmar `SELECT COUNT(*) FROM r72_projects;` = 12, cronogramas 73, tareas 4425, asignaciones 3597.
7. Con token JWT válido, ejecutar Postman GET `/api/v1/rrff/projects`, `/api/v1/rrff/schedules?projectId=PR001`, `/api/v1/rrff/projects/PR001/gantt?scheduleId=CR001-01`.
8. Verificar `POST .../clone` rechaza con 403 mientras escritura no esté autorizada.
9. Probar clonación **sólo en entorno de ensayo** después de aprobar accesos y configurar flags. Comprobar la integridad de tareas y padres, y que el corte original permanezca invariable.

## Restricciones y pendientes
- Las fechas y porcentajes del R04 representan los documentos/cortes originales, no datos en tiempo real.
- Las columnas o catálogos vacíos permanecen vacíos; no inventar estados, programación, ponderación ni fórmula de avance.
- La respuesta `progress` devuelve avance fuente bruto y `computedProgressPercent=null` hasta definir formalmente el cálculo.
- Usuario/profil R04 no habilita ni autentica a nadie; el sistema usa `usuario` JWT del backend M05.
- Las restricciones de roles y seguridad deben validarse con la institucionalidad antes de habilitar escritura en producción.
- PostgreSQL R05 es referencia, NO debe ejecutarse sobre MySQL. Las migraciones traducidas están en Flyway.
- La validación en SQLite y validación de referencias Excel no reemplazan una prueba MySQL real ni `mvn clean verify`.
