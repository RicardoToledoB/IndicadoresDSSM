-- V1: Modelo inicial RRFF-SSM. Identidades tecnicas, sin importar datos institucionales.
-- M05 V1.1 es propuesta: permisos de escritura y estados formales NO autorizados por este esquema.
SET NAMES utf8mb4;

CREATE TABLE proyecto (
  id VARCHAR(36) NOT NULL PRIMARY KEY,
  row_version BIGINT NOT NULL DEFAULT 0,
  created_at DATETIME(6) NOT NULL,
  updated_at DATETIME(6) NOT NULL,
  codigo_bip VARCHAR(40) NULL,
  nombre VARCHAR(300) NOT NULL,
  comuna VARCHAR(120) NULL,
  etapa VARCHAR(80) NULL,
  prioridad VARCHAR(80) NULL,
  fuente_referencia VARCHAR(255) NULL,
  periodo_fuente INT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE minuta (
  id VARCHAR(36) NOT NULL PRIMARY KEY,
  row_version BIGINT NOT NULL DEFAULT 0,
  created_at DATETIME(6) NOT NULL,
  updated_at DATETIME(6) NOT NULL,
  proyecto_id VARCHAR(36) NOT NULL,
  fecha_corte DATE NULL,
  titulo VARCHAR(300) NULL,
  resumen TEXT NULL,
  monto_informado DECIMAL(18,2) NULL,
  version_documental INT NOT NULL,
  UNIQUE KEY uq_minuta_version (proyecto_id, version_documental)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE gantt (
  id VARCHAR(36) NOT NULL PRIMARY KEY,
  row_version BIGINT NOT NULL DEFAULT 0,
  created_at DATETIME(6) NOT NULL,
  updated_at DATETIME(6) NOT NULL,
  proyecto_id VARCHAR(36) NOT NULL,
  nombre VARCHAR(255) NOT NULL,
  version_documental INT NOT NULL,
  fecha_corte DATE NULL,
  UNIQUE KEY uq_gantt_version (proyecto_id, version_documental)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE gantt_tarea (
  id VARCHAR(36) NOT NULL PRIMARY KEY,
  row_version BIGINT NOT NULL DEFAULT 0,
  created_at DATETIME(6) NOT NULL,
  updated_at DATETIME(6) NOT NULL,
  gantt_id VARCHAR(36) NOT NULL,
  denominacion VARCHAR(255) NOT NULL,
  inicio DATE NULL,
  fin DATE NULL,
  avance DECIMAL(5,2) NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE inversion (
  id VARCHAR(36) NOT NULL PRIMARY KEY,
  row_version BIGINT NOT NULL DEFAULT 0,
  created_at DATETIME(6) NOT NULL,
  updated_at DATETIME(6) NOT NULL,
  proyecto_id VARCHAR(36) NOT NULL,
  periodo INT NULL,
  fuente VARCHAR(150) NULL,
  moneda VARCHAR(10) NULL,
  monto DECIMAL(18,2) NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE convenio (
  id VARCHAR(36) NOT NULL PRIMARY KEY,
  row_version BIGINT NOT NULL DEFAULT 0,
  created_at DATETIME(6) NOT NULL,
  updated_at DATETIME(6) NOT NULL,
  titulo VARCHAR(300) NOT NULL,
  inicio_periodo INT NULL,
  fin_periodo INT NULL,
  estatus_formal VARCHAR(40) NULL,
  referencia_documento VARCHAR(255) NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE adenda (
  id VARCHAR(36) NOT NULL PRIMARY KEY,
  row_version BIGINT NOT NULL DEFAULT 0,
  created_at DATETIME(6) NOT NULL,
  updated_at DATETIME(6) NOT NULL,
  convenio_id VARCHAR(36) NOT NULL,
  fecha_instrumento DATE NULL,
  estatus_formal VARCHAR(40) NULL,
  documento_id VARCHAR(36) NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE correspondencia (
  id VARCHAR(36) NOT NULL PRIMARY KEY,
  row_version BIGINT NOT NULL DEFAULT 0,
  created_at DATETIME(6) NOT NULL,
  updated_at DATETIME(6) NOT NULL,
  anio INT NULL,
  fecha_recepcion DATE NULL,
  hora_documento TIME NULL,
  fecha_documento DATE NULL,
  origen VARCHAR(255) NULL,
  tipo_documento VARCHAR(60) NULL,
  numero_documento VARCHAR(60) NULL,
  materia TEXT NULL,
  responsable VARCHAR(200) NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE correlativo (
  id VARCHAR(36) NOT NULL PRIMARY KEY,
  row_version BIGINT NOT NULL DEFAULT 0,
  created_at DATETIME(6) NOT NULL,
  updated_at DATETIME(6) NOT NULL,
  tipo VARCHAR(40) NOT NULL,
  serie VARCHAR(80) NULL,
  unidad VARCHAR(120) NULL,
  anio INT NULL,
  numero VARCHAR(60) NULL,
  responsable VARCHAR(200) NULL,
  fecha DATE NULL,
  materia TEXT NULL,
  destino VARCHAR(255) NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE crc (
  id VARCHAR(36) NOT NULL PRIMARY KEY,
  row_version BIGINT NOT NULL DEFAULT 0,
  created_at DATETIME(6) NOT NULL,
  updated_at DATETIME(6) NOT NULL,
  numero VARCHAR(60) NULL,
  fecha DATE NULL,
  proveedor VARCHAR(255) NULL,
  materia TEXT NULL,
  documento_factura_id VARCHAR(36) NULL,
  documento_certificado_id VARCHAR(36) NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE documento (
  id VARCHAR(36) NOT NULL PRIMARY KEY,
  row_version BIGINT NOT NULL DEFAULT 0,
  created_at DATETIME(6) NOT NULL,
  updated_at DATETIME(6) NOT NULL,
  titulo VARCHAR(300) NOT NULL,
  tipo VARCHAR(80) NULL,
  clasificacion VARCHAR(60) NULL,
  origen VARCHAR(255) NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE documento_version (
  id VARCHAR(36) NOT NULL PRIMARY KEY,
  row_version BIGINT NOT NULL DEFAULT 0,
  created_at DATETIME(6) NOT NULL,
  updated_at DATETIME(6) NOT NULL,
  documento_id VARCHAR(36) NOT NULL,
  numero INT NOT NULL,
  referencia_archivo VARCHAR(500) NOT NULL,
  hash_integridad VARCHAR(128) NULL,
  UNIQUE KEY uq_documento_version (documento_id, numero)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE perfil_permiso (
  id VARCHAR(36) NOT NULL PRIMARY KEY,
  row_version BIGINT NOT NULL DEFAULT 0,
  created_at DATETIME(6) NOT NULL,
  updated_at DATETIME(6) NOT NULL,
  actor_id VARCHAR(36) NOT NULL,
  recurso VARCHAR(100) NOT NULL,
  accion VARCHAR(50) NOT NULL,
  ambito VARCHAR(255) NULL,
  vigente_hasta DATETIME(6) NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE auditoria_evento (
  id VARCHAR(36) NOT NULL PRIMARY KEY,
  row_version BIGINT NOT NULL DEFAULT 0,
  created_at DATETIME(6) NOT NULL,
  updated_at DATETIME(6) NOT NULL,
  actor_id VARCHAR(36) NULL,
  recurso_id VARCHAR(120) NOT NULL,
  accion VARCHAR(60) NOT NULL,
  fecha_hora DATETIME(6) NOT NULL,
  resultado VARCHAR(40) NOT NULL,
  detalle VARCHAR(500) NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE bien (
  id VARCHAR(36) NOT NULL PRIMARY KEY,
  row_version BIGINT NOT NULL DEFAULT 0,
  created_at DATETIME(6) NOT NULL,
  updated_at DATETIME(6) NOT NULL,
  tipo VARCHAR(80) NULL,
  ubicacion VARCHAR(255) NULL,
  identificador_institucional VARCHAR(100) NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE mantencion (
  id VARCHAR(36) NOT NULL PRIMARY KEY,
  row_version BIGINT NOT NULL DEFAULT 0,
  created_at DATETIME(6) NOT NULL,
  updated_at DATETIME(6) NOT NULL,
  bien_id VARCHAR(36) NULL,
  fecha_programada DATE NULL,
  actividad VARCHAR(255) NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE pac_item (
  id VARCHAR(36) NOT NULL PRIMARY KEY,
  row_version BIGINT NOT NULL DEFAULT 0,
  created_at DATETIME(6) NOT NULL,
  updated_at DATETIME(6) NOT NULL,
  area VARCHAR(100) NULL,
  periodo INT NULL,
  descripcion VARCHAR(255) NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE indicador (
  id VARCHAR(36) NOT NULL PRIMARY KEY,
  row_version BIGINT NOT NULL DEFAULT 0,
  created_at DATETIME(6) NOT NULL,
  updated_at DATETIME(6) NOT NULL,
  nombre VARCHAR(255) NULL,
  periodo VARCHAR(30) NULL,
  meta DECIMAL(18,4) NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE hito (
  id VARCHAR(36) NOT NULL PRIMARY KEY,
  row_version BIGINT NOT NULL DEFAULT 0,
  created_at DATETIME(6) NOT NULL,
  updated_at DATETIME(6) NOT NULL,
  proyecto_id VARCHAR(36) NOT NULL,
  denominacion VARCHAR(255) NULL,
  inicio DATE NULL,
  fin DATE NULL,
  estado VARCHAR(60) NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE derivacion (
  id VARCHAR(36) NOT NULL PRIMARY KEY,
  row_version BIGINT NOT NULL DEFAULT 0,
  created_at DATETIME(6) NOT NULL,
  updated_at DATETIME(6) NOT NULL,
  correspondencia_id VARCHAR(36) NOT NULL,
  destino VARCHAR(255) NULL,
  fecha DATE NULL,
  observacion TEXT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE convenio_proyecto (
  id VARCHAR(36) NOT NULL PRIMARY KEY,
  row_version BIGINT NOT NULL DEFAULT 0,
  created_at DATETIME(6) NOT NULL,
  updated_at DATETIME(6) NOT NULL,
  convenio_id VARCHAR(36) NOT NULL,
  proyecto_id VARCHAR(36) NOT NULL,
  tipo_vinculo VARCHAR(80) NULL,
  UNIQUE KEY uq_convenio_proyecto (convenio_id, proyecto_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE instrumento_organico (
  id VARCHAR(36) NOT NULL PRIMARY KEY,
  row_version BIGINT NOT NULL DEFAULT 0,
  created_at DATETIME(6) NOT NULL,
  updated_at DATETIME(6) NOT NULL,
  documento_id VARCHAR(36) NULL,
  tipo_acto VARCHAR(80) NULL,
  numero VARCHAR(80) NULL,
  anio INT NULL,
  fecha DATE NULL,
  emisor VARCHAR(255) NULL,
  vigencia VARCHAR(60) NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE reporte_ejecucion (
  id VARCHAR(36) NOT NULL PRIMARY KEY,
  row_version BIGINT NOT NULL DEFAULT 0,
  created_at DATETIME(6) NOT NULL,
  updated_at DATETIME(6) NOT NULL,
  tipo VARCHAR(80) NOT NULL,
  fecha_corte DATE NULL,
  proyecto_id VARCHAR(36) NULL,
  filtros TEXT NULL,
  referencia_fuente VARCHAR(255) NULL,
  documento_id VARCHAR(36) NULL,
  estado VARCHAR(40) NULL,
  creado_por VARCHAR(100) NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE usuario (
  id VARCHAR(36) NOT NULL PRIMARY KEY,
  row_version BIGINT NOT NULL DEFAULT 0,
  created_at DATETIME(6) NOT NULL,
  updated_at DATETIME(6) NOT NULL,
  username VARCHAR(100) NOT NULL,
  password_hash VARCHAR(255) NOT NULL,
  activo BOOLEAN NOT NULL,
  UNIQUE KEY uq_usuario_username (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE usuario_rol (
  usuario_id VARCHAR(36) NOT NULL,
  rol VARCHAR(80) NOT NULL,
  PRIMARY KEY(usuario_id,rol),
  CONSTRAINT fk_ur_usuario FOREIGN KEY(usuario_id) REFERENCES usuario(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX ix_minuta_proyecto_id ON minuta(proyecto_id);
ALTER TABLE minuta ADD CONSTRAINT fk_minuta_proyecto_id FOREIGN KEY (proyecto_id) REFERENCES proyecto(id);
CREATE INDEX ix_gantt_proyecto_id ON gantt(proyecto_id);
ALTER TABLE gantt ADD CONSTRAINT fk_gantt_proyecto_id FOREIGN KEY (proyecto_id) REFERENCES proyecto(id);
CREATE INDEX ix_gantt_tarea_gantt_id ON gantt_tarea(gantt_id);
ALTER TABLE gantt_tarea ADD CONSTRAINT fk_gantt_tarea_gantt_id FOREIGN KEY (gantt_id) REFERENCES gantt(id);
CREATE INDEX ix_inversion_proyecto_id ON inversion(proyecto_id);
ALTER TABLE inversion ADD CONSTRAINT fk_inversion_proyecto_id FOREIGN KEY (proyecto_id) REFERENCES proyecto(id);
CREATE INDEX ix_adenda_convenio_id ON adenda(convenio_id);
ALTER TABLE adenda ADD CONSTRAINT fk_adenda_convenio_id FOREIGN KEY (convenio_id) REFERENCES convenio(id);
CREATE INDEX ix_adenda_documento_id ON adenda(documento_id);
ALTER TABLE adenda ADD CONSTRAINT fk_adenda_documento_id FOREIGN KEY (documento_id) REFERENCES documento(id);
CREATE INDEX ix_crc_documento_factura_id ON crc(documento_factura_id);
ALTER TABLE crc ADD CONSTRAINT fk_crc_documento_factura_id FOREIGN KEY (documento_factura_id) REFERENCES documento(id);
CREATE INDEX ix_crc_documento_certificado_id ON crc(documento_certificado_id);
ALTER TABLE crc ADD CONSTRAINT fk_crc_documento_certificado_id FOREIGN KEY (documento_certificado_id) REFERENCES documento(id);
CREATE INDEX ix_documento_version_documento_id ON documento_version(documento_id);
ALTER TABLE documento_version ADD CONSTRAINT fk_documento_version_documento_id FOREIGN KEY (documento_id) REFERENCES documento(id);
CREATE INDEX ix_perfil_permiso_actor_id ON perfil_permiso(actor_id);
ALTER TABLE perfil_permiso ADD CONSTRAINT fk_perfil_permiso_actor_id FOREIGN KEY (actor_id) REFERENCES usuario(id);
CREATE INDEX ix_auditoria_evento_actor_id ON auditoria_evento(actor_id);
ALTER TABLE auditoria_evento ADD CONSTRAINT fk_auditoria_evento_actor_id FOREIGN KEY (actor_id) REFERENCES usuario(id);
CREATE INDEX ix_mantencion_bien_id ON mantencion(bien_id);
ALTER TABLE mantencion ADD CONSTRAINT fk_mantencion_bien_id FOREIGN KEY (bien_id) REFERENCES bien(id);
CREATE INDEX ix_hito_proyecto_id ON hito(proyecto_id);
ALTER TABLE hito ADD CONSTRAINT fk_hito_proyecto_id FOREIGN KEY (proyecto_id) REFERENCES proyecto(id);
CREATE INDEX ix_derivacion_correspondencia_id ON derivacion(correspondencia_id);
ALTER TABLE derivacion ADD CONSTRAINT fk_derivacion_correspondencia_id FOREIGN KEY (correspondencia_id) REFERENCES correspondencia(id);
CREATE INDEX ix_convenio_proyecto_convenio_id ON convenio_proyecto(convenio_id);
ALTER TABLE convenio_proyecto ADD CONSTRAINT fk_convenio_proyecto_convenio_id FOREIGN KEY (convenio_id) REFERENCES convenio(id);
CREATE INDEX ix_convenio_proyecto_proyecto_id ON convenio_proyecto(proyecto_id);
ALTER TABLE convenio_proyecto ADD CONSTRAINT fk_convenio_proyecto_proyecto_id FOREIGN KEY (proyecto_id) REFERENCES proyecto(id);
CREATE INDEX ix_instrumento_organico_documento_id ON instrumento_organico(documento_id);
ALTER TABLE instrumento_organico ADD CONSTRAINT fk_instrumento_organico_documento_id FOREIGN KEY (documento_id) REFERENCES documento(id);
CREATE INDEX ix_reporte_ejecucion_proyecto_id ON reporte_ejecucion(proyecto_id);
ALTER TABLE reporte_ejecucion ADD CONSTRAINT fk_reporte_ejecucion_proyecto_id FOREIGN KEY (proyecto_id) REFERENCES proyecto(id);
CREATE INDEX ix_reporte_ejecucion_documento_id ON reporte_ejecucion(documento_id);
ALTER TABLE reporte_ejecucion ADD CONSTRAINT fk_reporte_ejecucion_documento_id FOREIGN KEY (documento_id) REFERENCES documento(id);
CREATE INDEX ix_proyecto_nombre ON proyecto(nombre);
CREATE INDEX ix_correspondencia_fecha ON correspondencia(fecha_recepcion);
CREATE INDEX ix_inversion_periodo ON inversion(periodo);
