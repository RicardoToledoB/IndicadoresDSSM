# Mapa de API — `/api/v1`

Todas las rutas salvo `POST /auth/login` requieren `Authorization: Bearer JWT`.

## Autenticación

| Método | Ruta | Permiso |
|---|---|---|
| POST | `/auth/login` | Público, usuario activo |
| GET | `/auth/me` | Autenticado |
| GET | `/catalogos/modulos` | Autenticado (informativo) |

## Lectura de entidades (GET lista y detalle)

`GET /{recurso}?page=0&size=20&q=texto&ref=id` y `GET /{recurso}/{id}`.

- Módulo 7: `minutas`, `gantt`, `gantt-tareas`, `reportes-ejecucion`.
- Módulo 3: `proyectos`, `inversiones`, `convenios`, `adendas`, `hitos`, `convenios-proyectos`.
- Módulo 1: `correspondencia`, `correlativos`, `crc`, `derivaciones`, `instrumentos-organicos`.
- Administrador solamente: `documentos`, `documentos-versiones`, `permisos`, `auditoria` y candidatos `bienes`, `mantenciones`, `pac-items`, `indicadores`.

`q` opera solo donde está configurado un campo de texto consultable. `ref` aplica a relaciones conocidas (por ejemplo, minutas `ref=proyectoId`). No se realizan joins con relaciones no aprobadas.

## Escritura de ensayo (403 inicialmente)

| Método | Ruta | Módulo | Nota |
|---|---|---|---|
| POST | `/proyectos` | 3 | Nombre requerido; BIP opcional |
| PATCH | `/proyectos/{id}` | 3 | `expectedRowVersion` requerido; 409 si obsoleta |
| POST | `/inversiones` | 3 | Monto no se agrega automáticamente |
| POST | `/convenios` | 3 | Estado inicial `NO_ACREDITADO` |
| POST | `/adendas` | 3 | No autoriza formalización |
| POST | `/hitos` | 3 | Fechas consistentes si ambas están informadas |
| POST | `/convenios-proyectos` | 3 | Referencias existentes y par único |
| POST | `/minutas` | 7 | Versión nueva sin sobrescribir anterior |
| POST | `/gantt` | 7 | Una versión con tareas, validación fecha fin >= inicio |
| POST | `/correspondencia` | 1 | Número original literal, no genera correlativo |
| POST | `/derivaciones` | 1 | Cada destino permanece separado |

## Reportabilidad

`GET /reportabilidad/proyectos/{id}/vista-ejecutiva?fechaCorte=2026-11-18` devuelve proyecto, minutas, cronogramas con tareas, partidas, advertencias y `exportacionAutorizada=false`. No certifica montos ni fecha de vigencia.

## Administración de usuarios

| Método | Ruta | Permiso |
|---|---|---|
| GET | `/admin/usuarios` | ROLE_ADMIN |
| POST | `/admin/usuarios` | ROLE_ADMIN |
| PATCH | `/admin/usuarios/{id}/activo` | ROLE_ADMIN |

## Códigos esperados

200 (consulta), 400 (datos inválidos), 401 (token ausente/inválido), 403 (rol/acción no habilitada), 404 (recurso o ID no existente), 409 (integridad/versión), 500 (error servidor). Las operaciones `POST` retornan 200 por diseño MVP; se puede incorporar `201 Location` en la versión del contrato.
