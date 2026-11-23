# Alcance de esta entrega y decisiones pendientes

**Base:** AG-RRFF-001/M05 V1.1; en contradicciones prevalece **22.2/22.3/22.4/22.5**.

## Entregado en código

- Proyecto Spring Boot modular, MySQL, migración Flyway y entidades del modelo candidato conciliado.
- Login JWT HS256 + BCrypt + administración básica de usuarios sin exponer hash.
- Permisos de visualización por roles sobre módulos 1/3/7; denegación por defecto en 2/4/5/6 salvo admin técnico.
- Escrituras **desactivadas inicialmente**, con clases de endpoints para QA de los módulos priorizados.
- Consulta paginada/filtrada de entidades, incluyendo correspondencia, CRC y correlativos separados.
- Vista ejecutiva JSON de proyecto **no oficial** que referencia inversión, Gantt y minutas sin inventar sumatorias ni vigencia.
- Versiones nuevas de Minuta y Gantt, control optimista `rowVersion` en proyecto, UNIQUE contextual para versiones de minuta/Gantt.
- Auditoría básica de escrituras exitosas; sin borrados en cascada ni tablas inventadas a partir de código externo.

## Pendientes institucionales y técnicos **bloqueantes antes de producción**

1. Validar formalmente propuesta documental y requerimientos PD. Los destinatarios de lectura no se convierten en autorización de escritura.
2. Revisar la BD y el código rrff-ssm que pudiera existir para decidir migración vs sistema nuevo. Este ZIP no los inspecciona.
3. Definir alcance de roles por unidad, establecimiento, registro y clasificación; implementar ACL por objeto, caducidad y control de descarga documental.
4. Adendas/convenios: distinguir convenio 2021–2026, programación vigente y eventual adenda 2027–2030; verificar firma, actos y fecha. No inferir formalización por nombre.
5. Correlativos: acordar la unidad/serie/año y regla de unicidad antes de reservar, emitir o anular. CRC es independiente.
6. Reportabilidad: aprobar formatos y fórmulas de avance, cortes, versionado fuente, política de exportación y conciliación de inversión/monedas.
7. Archivos: clasificación, hash, retención, tamaño, antivirus, permisos por documento y versionado de binarios. Actualmente solo metadatos, sin subida ni descarga.
8. Integridad/duplicados: procedimientos de importación idempotentes, fuentes de verdad, versiones e históricos. El demo no importa Excel/Word.
9. Módulos 2, 4, 5, 6: faltan formularios operativos, reglas institucionales, catálogos, roles y documentos base. Sus tablas son **candidatas**, no procesos completados.
10. Ciberseguridad: gestión de secretos, MFA/SSO si corresponde, bloqueo por intentos, rotación/revocación JWT, monitoreo, pruebas de inyección/IDOR, tasa y logs centralizados.
11. Pruebas: compilación Maven, ejecución MySQL 8, Flyway limpia y con base existente (no aplicar sin copia), validaciones de campo, seguridad de extremo a extremo, concurrencia, rendimiento y aceptación de usuarios.
12. Aprobación institucional específica antes de pasar a producción; no ejecutar POST de ensayo en sistemas reales.

## Decisiones de implementación local (DT) claramente no acreditadas

- URLs REST diseñadas para el nuevo backend, no interfaces preexistentes confirmadas.
- Puerto 8097, nombre BD `rrff_ssm`, nombres de roles y caducidad de token.
- PK UUID almacenada como `VARCHAR(36)` + row_version; `UNIQUE` solamente en versiones documentales y pares de convenio/proyecto.
- Un admin técnico con acceso amplio en ambiente de ensayo; restringir por objeto en futuro.
- Captura de `estatusFormal=NO_ACREDITADO` para convenios/adendas nuevos, sin mecanismo de formalización.
- No sumamos montos de moneda/ejercicio distinto, ni mostramos cero al faltar fuentes.

## Trazabilidad 22.2

| Formulario | Representación en API |
|---|---|
| F-7-03 | `GET/POST /minutas` con proyecto/corte/resumen/versionado |
| F-7-04/05 | `GET/POST /gantt` y `GET /gantt-tareas`, fechas sin cálculo automático |
| F-7-07 | `GET /reportabilidad/proyectos/{id}/vista-ejecutiva` (sin exportación) |
| F-3-02 | `GET/POST/PATCH /proyectos` (BIP opcional; sin UNIQUE global) |
| F-3-04/05 | `GET/POST /inversiones` (moneda explícita; sin sumas oficiales) |
| F-3-06 | `GET/POST /convenios`, `GET/POST /adendas` (NO_ACREDITADO) |
| F-1-01/02 | `GET/POST /correspondencia` y `GET/POST /derivaciones` |
| F-1-03/04/05 | `GET /correlativos` (no asignación/reserva) |
| F-1-06/07 | `GET /crc` (CRC separado de correlativo) |
| F-1-08 | `GET /instrumentos-organicos` (sin vínculo con CRC ni correlativos) |

**Ningún endpoint no implementado debe presentarse como operable.**
