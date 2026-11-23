-- CONDOMINIO SIRUS — RRFF-SSM / 7.2 CARTA GANTT

-- DDL PROPUESTO, NO EJECUTADO EN SERVIDOR

-- IDs TEXT preservan exactamente las referencias del Excel (PR001, CR001-01, etc.)

-- Personas abreviadas y users provisionales no conceden autenticación ni permisos.

BEGIN;

CREATE SCHEMA IF NOT EXISTS rrff;

SET search_path TO rrff, public;

CREATE TABLE IF NOT EXISTS "projects" (
  "project_id" TEXT PRIMARY KEY,
  "source_name" TEXT,
  "source_bip_code" TEXT,
  "source_file_id" TEXT,
  "project_type_id" TEXT,
  "facility_id" TEXT,
  "province_id" TEXT,
  "commune_id" TEXT,
  "funding_type_id" TEXT,
  "notes" TEXT,
  "active" BOOLEAN,
  "created_at" TIMESTAMPTZ,
  "updated_at" TIMESTAMPTZ,
  "updated_by_user_id" TEXT
);

CREATE TABLE IF NOT EXISTS "source_files" (
  "source_file_id" TEXT PRIMARY KEY,
  "project_id" TEXT,
  "original_file_name" TEXT,
  "sha256" TEXT,
  "sheet_count" INTEGER
);

CREATE TABLE IF NOT EXISTS "schedules" (
  "schedule_id" TEXT PRIMARY KEY,
  "project_id" TEXT,
  "source_file_id" TEXT,
  "source_sheet_name" TEXT,
  "cutoff_date" DATE,
  "source_overall_progress" NUMERIC(18,6),
  "header_row" INTEGER,
  "activity_count" INTEGER,
  "status_id" TEXT,
  "version_published" BOOLEAN,
  "created_at" TIMESTAMPTZ,
  "updated_at" TIMESTAMPTZ,
  "updated_by_user_id" TEXT
);

CREATE TABLE IF NOT EXISTS "schedule_tasks" (
  "task_id" TEXT PRIMARY KEY,
  "schedule_id" TEXT,
  "project_id" TEXT,
  "code_hierarchical" TEXT,
  "parent_code" TEXT,
  "record_type" TEXT,
  "stage_id" TEXT,
  "source_stage_text" TEXT,
  "activity_o_substage" TEXT,
  "source_verifier" TEXT,
  "status_id" TEXT,
  "source_status" TEXT,
  "source_responsible_id" TEXT,
  "source_responsible_text" TEXT,
  "start_date" DATE,
  "duration_days" TEXT,
  "end_date" DATE,
  "source_row_number" INTEGER,
  "actual_progress_percent" NUMERIC(18,6),
  "weight" NUMERIC(18,6),
  "parent_task_id" TEXT,
  "created_by_user_id" TEXT,
  "updated_by_user_id" TEXT,
  "verifier_id" TEXT
);

CREATE TABLE IF NOT EXISTS "source_responsibles" (
  "source_responsible_id" TEXT PRIMARY KEY,
  "original_text" TEXT,
  "validated_person_id" TEXT,
  "note" TEXT
);

CREATE TABLE IF NOT EXISTS "source_task_responsibles" (
  "assignment_id" TEXT PRIMARY KEY,
  "task_id" TEXT,
  "source_responsible_id" TEXT,
  "person_id" TEXT,
  "assignment_role_id" TEXT
);

CREATE TABLE IF NOT EXISTS "statuses" (
  "status_id" TEXT PRIMARY KEY,
  "code_original" TEXT,
  "pending_description" TEXT,
  "created_at" TIMESTAMPTZ,
  "updated_at" TIMESTAMPTZ,
  "updated_by_user_id" TEXT
);

CREATE TABLE IF NOT EXISTS "stages" (
  "stage_id" TEXT PRIMARY KEY,
  "source_name" TEXT,
  "description" TEXT,
  "created_at" TIMESTAMPTZ,
  "updated_at" TIMESTAMPTZ,
  "updated_by_user_id" TEXT
);

CREATE TABLE IF NOT EXISTS "source_rows" (
  "source_row_id" TEXT PRIMARY KEY,
  "schedule_id" TEXT,
  "number_row" INTEGER,
  "source_col_b_code" TEXT,
  "source_col_c_stage" TEXT,
  "source_col_d_substage" TEXT,
  "source_col_e_verifier" TEXT,
  "source_col_f_status" TEXT,
  "source_col_g_responsible" TEXT,
  "source_col_h_start" TEXT,
  "source_col_i_duration" TEXT,
  "source_col_j_end" TEXT
);

CREATE TABLE IF NOT EXISTS "source_metadata" (
  "source_metadata_id" TEXT PRIMARY KEY,
  "schedule_id" TEXT,
  "original_row" TEXT,
  "original_column" TEXT,
  "original_value" TEXT
);

CREATE TABLE IF NOT EXISTS "source_cells" (
  "source_cell_id" TEXT PRIMARY KEY,
  "schedule_id" TEXT,
  "cell" TEXT,
  "original_value" TEXT
);

CREATE TABLE IF NOT EXISTS "task_dependencies" (
  "dependency_id" TEXT PRIMARY KEY,
  "schedule_id" TEXT,
  "predecessor_task_id" TEXT,
  "successor_task_id" TEXT,
  "dependency_type" TEXT,
  "lag_days" INTEGER,
  "note" TEXT
);

CREATE TABLE IF NOT EXISTS "milestones" (
  "milestone_id" TEXT PRIMARY KEY,
  "schedule_id" TEXT,
  "task_id" TEXT,
  "name" TEXT,
  "planned_date" DATE,
  "actual_date" DATE,
  "status_id" TEXT,
  "verifier_id" TEXT
);

CREATE TABLE IF NOT EXISTS "task_updates" (
  "update_id" TEXT PRIMARY KEY,
  "task_id" TEXT,
  "cutoff_date" DATE,
  "progress_percent" NUMERIC(18,6),
  "note" TEXT,
  "user_id" TEXT,
  "recorded_at" TIMESTAMPTZ
);

CREATE TABLE IF NOT EXISTS "persons" (
  "person_id" TEXT PRIMARY KEY,
  "full_name" TEXT,
  "institutional_identifier" TEXT,
  "email" TEXT,
  "active" BOOLEAN,
  "source_initials" TEXT,
  "actor_id" TEXT,
  "identification_status" TEXT,
  "note" TEXT,
  "created_at" TIMESTAMPTZ,
  "updated_at" TIMESTAMPTZ,
  "updated_by_user_id" TEXT
);

CREATE TABLE IF NOT EXISTS "assignment_roles" (
  "assignment_role_id" TEXT PRIMARY KEY,
  "role_name" TEXT,
  "description" TEXT
);

CREATE TABLE IF NOT EXISTS "users" (
  "user_id" TEXT PRIMARY KEY,
  "person_id" TEXT,
  "identifier_source" TEXT,
  "login_actual" TEXT,
  "active" BOOLEAN DEFAULT FALSE,
  "status" TEXT,
  "note" TEXT
);

CREATE TABLE IF NOT EXISTS "profiles" (
  "profile_id" TEXT PRIMARY KEY,
  "code" TEXT,
  "description" TEXT,
  "active" BOOLEAN
);

CREATE TABLE IF NOT EXISTS "user_profiles" (
  "user_profile_id" TEXT PRIMARY KEY,
  "user_id" TEXT,
  "profile_id" TEXT,
  "valid_from" DATE,
  "valid_to" DATE
);

CREATE TABLE IF NOT EXISTS "project_people" (
  "project_person_id" TEXT PRIMARY KEY,
  "project_id" TEXT,
  "person_id" TEXT,
  "assignment_role_id" TEXT,
  "valid_from" DATE,
  "valid_to" DATE,
  "active" BOOLEAN,
  "user_id" TEXT
);

CREATE TABLE IF NOT EXISTS "facilities" (
  "facility_id" TEXT PRIMARY KEY,
  "name" TEXT,
  "facility_type_id" TEXT,
  "commune_id" TEXT,
  "active" BOOLEAN,
  "note_source" TEXT,
  "created_at" TIMESTAMPTZ,
  "updated_at" TIMESTAMPTZ,
  "updated_by_user_id" TEXT
);

CREATE TABLE IF NOT EXISTS "facility_types" (
  "facility_type_id" TEXT PRIMARY KEY,
  "code" TEXT,
  "description" TEXT,
  "created_at" TIMESTAMPTZ,
  "updated_at" TIMESTAMPTZ,
  "updated_by_user_id" TEXT
);

CREATE TABLE IF NOT EXISTS "provinces" (
  "province_id" TEXT PRIMARY KEY,
  "name" TEXT
);

CREATE TABLE IF NOT EXISTS "communes" (
  "commune_id" TEXT PRIMARY KEY,
  "province_id" TEXT,
  "name" TEXT
);

CREATE TABLE IF NOT EXISTS "funding_types" (
  "funding_id" TEXT PRIMARY KEY,
  "description" TEXT,
  "active" BOOLEAN,
  "created_at" TIMESTAMPTZ,
  "updated_at" TIMESTAMPTZ,
  "updated_by_user_id" TEXT
);

CREATE TABLE IF NOT EXISTS "documents" (
  "document_id" TEXT PRIMARY KEY,
  "project_id" TEXT,
  "schedule_id" TEXT,
  "task_id" TEXT,
  "file_name" TEXT,
  "document_type" TEXT,
  "document_date" DATE,
  "location_repository" TEXT
);

CREATE TABLE IF NOT EXISTS "photos" (
  "photo_id" TEXT PRIMARY KEY,
  "project_id" TEXT,
  "schedule_id" TEXT,
  "task_id" TEXT,
  "file_name" TEXT,
  "photo_date" DATE,
  "description" TEXT,
  "location_repository" TEXT
);

CREATE TABLE IF NOT EXISTS "verifiers" (
  "verifier_id" TEXT PRIMARY KEY,
  "verifier_type" TEXT,
  "description" TEXT,
  "created_at" TIMESTAMPTZ,
  "updated_at" TIMESTAMPTZ,
  "updated_by_user_id" TEXT
);

CREATE TABLE IF NOT EXISTS "budget_items" (
  "budget_item_id" TEXT PRIMARY KEY,
  "project_id" TEXT,
  "schedule_id" TEXT,
  "description" TEXT,
  "initial_amount" NUMERIC(18,6),
  "current_amount" NUMERIC(18,6),
  "currency_unit" TEXT,
  "reference_date" DATE
);

CREATE TABLE IF NOT EXISTS "budget_movements" (
  "budget_movement_id" TEXT PRIMARY KEY,
  "budget_item_id" TEXT,
  "date" DATE,
  "amount" NUMERIC(18,6),
  "movement_type" TEXT,
  "note" TEXT,
  "user_id" TEXT
);

CREATE TABLE IF NOT EXISTS "audit_log" (
  "audit_id" TEXT PRIMARY KEY,
  "table" TEXT,
  "record_id" TEXT,
  "operation" TEXT,
  "user_id" TEXT,
  "date_time" TIMESTAMPTZ,
  "detail" TEXT
);

CREATE TABLE IF NOT EXISTS "source_actors" (
  "actor_id" TEXT PRIMARY KEY,
  "source_alias" TEXT,
  "provisional_type" TEXT,
  "candidate_person_id" TEXT,
  "note" TEXT
);

CREATE TABLE IF NOT EXISTS "task_actors" (
  "relation_id" TEXT PRIMARY KEY,
  "task_id" TEXT,
  "schedule_id" TEXT,
  "project_id" TEXT,
  "source_responsible_id" TEXT,
  "actor_id" TEXT,
  "candidate_person_id" TEXT,
  "source_assignment_role" TEXT,
  "source_column" TEXT,
  "notes" TEXT,
  "candidate_user_id" TEXT
);

CREATE TABLE IF NOT EXISTS "task_people" (
  "relation_id" TEXT PRIMARY KEY,
  "task_id" TEXT,
  "person_id" TEXT,
  "role" TEXT,
  "source_responsible_id" TEXT,
  "valid_from" DATE,
  "valid_to" DATE,
  "user_id" TEXT
);

CREATE TABLE IF NOT EXISTS "person_users" (
  "person_id" TEXT PRIMARY KEY,
  "user_id" TEXT,
  "initials" TEXT,
  "actor_id" TEXT,
  "status" TEXT
);

CREATE TABLE IF NOT EXISTS "actor_users" (
  "actor_id" TEXT PRIMARY KEY,
  "source_alias" TEXT,
  "candidate_person_id" TEXT,
  "user_id" TEXT,
  "provisional_type" TEXT
);

ALTER TABLE rrff."projects" ADD CONSTRAINT "fk_proyectos_fuente_id" FOREIGN KEY ("source_file_id") REFERENCES rrff."source_files"("source_file_id") DEFERRABLE INITIALLY DEFERRED;

CREATE INDEX IF NOT EXISTS "idx_proyectos_fuente_id" ON rrff."projects"("source_file_id");

ALTER TABLE rrff."projects" ADD CONSTRAINT "fk_proyectos_establecimiento_id" FOREIGN KEY ("facility_id") REFERENCES rrff."facilities"("facility_id") DEFERRABLE INITIALLY DEFERRED;

CREATE INDEX IF NOT EXISTS "idx_proyectos_establecimiento_id" ON rrff."projects"("facility_id");

ALTER TABLE rrff."projects" ADD CONSTRAINT "fk_proyectos_provincia_id" FOREIGN KEY ("province_id") REFERENCES rrff."provinces"("province_id") DEFERRABLE INITIALLY DEFERRED;

CREATE INDEX IF NOT EXISTS "idx_proyectos_provincia_id" ON rrff."projects"("province_id");

ALTER TABLE rrff."projects" ADD CONSTRAINT "fk_proyectos_comuna_id" FOREIGN KEY ("commune_id") REFERENCES rrff."communes"("commune_id") DEFERRABLE INITIALLY DEFERRED;

CREATE INDEX IF NOT EXISTS "idx_proyectos_comuna_id" ON rrff."projects"("commune_id");

ALTER TABLE rrff."projects" ADD CONSTRAINT "fk_proyectos_tipo_financiamiento_id" FOREIGN KEY ("funding_type_id") REFERENCES rrff."funding_types"("funding_id") DEFERRABLE INITIALLY DEFERRED;

CREATE INDEX IF NOT EXISTS "idx_proyectos_tipo_financiamiento_" ON rrff."projects"("funding_type_id");

ALTER TABLE rrff."projects" ADD CONSTRAINT "fk_proyectos_updated_by_user_id" FOREIGN KEY ("updated_by_user_id") REFERENCES rrff."users"("user_id") DEFERRABLE INITIALLY DEFERRED;

CREATE INDEX IF NOT EXISTS "idx_proyectos_updated_by_user_id" ON rrff."projects"("updated_by_user_id");

ALTER TABLE rrff."source_files" ADD CONSTRAINT "fk_fuentes_proyecto_id" FOREIGN KEY ("project_id") REFERENCES rrff."projects"("project_id") DEFERRABLE INITIALLY DEFERRED;

CREATE INDEX IF NOT EXISTS "idx_fuentes_proyecto_id" ON rrff."source_files"("project_id");

ALTER TABLE rrff."schedules" ADD CONSTRAINT "fk_cronogramas_proyecto_id" FOREIGN KEY ("project_id") REFERENCES rrff."projects"("project_id") DEFERRABLE INITIALLY DEFERRED;

CREATE INDEX IF NOT EXISTS "idx_cronogramas_proyecto_id" ON rrff."schedules"("project_id");

ALTER TABLE rrff."schedules" ADD CONSTRAINT "fk_cronogramas_fuente_id" FOREIGN KEY ("source_file_id") REFERENCES rrff."source_files"("source_file_id") DEFERRABLE INITIALLY DEFERRED;

CREATE INDEX IF NOT EXISTS "idx_cronogramas_fuente_id" ON rrff."schedules"("source_file_id");

ALTER TABLE rrff."schedules" ADD CONSTRAINT "fk_cronogramas_estado_id" FOREIGN KEY ("status_id") REFERENCES rrff."statuses"("status_id") DEFERRABLE INITIALLY DEFERRED;

CREATE INDEX IF NOT EXISTS "idx_cronogramas_estado_id" ON rrff."schedules"("status_id");

ALTER TABLE rrff."schedules" ADD CONSTRAINT "fk_cronogramas_updated_by_user_id" FOREIGN KEY ("updated_by_user_id") REFERENCES rrff."users"("user_id") DEFERRABLE INITIALLY DEFERRED;

CREATE INDEX IF NOT EXISTS "idx_cronogramas_updated_by_user_id" ON rrff."schedules"("updated_by_user_id");

ALTER TABLE rrff."schedule_tasks" ADD CONSTRAINT "fk_tareas_gantt_cronograma_id" FOREIGN KEY ("schedule_id") REFERENCES rrff."schedules"("schedule_id") DEFERRABLE INITIALLY DEFERRED;

CREATE INDEX IF NOT EXISTS "idx_tareas_gantt_cronograma_id" ON rrff."schedule_tasks"("schedule_id");

ALTER TABLE rrff."schedule_tasks" ADD CONSTRAINT "fk_tareas_gantt_proyecto_id" FOREIGN KEY ("project_id") REFERENCES rrff."projects"("project_id") DEFERRABLE INITIALLY DEFERRED;

CREATE INDEX IF NOT EXISTS "idx_tareas_gantt_proyecto_id" ON rrff."schedule_tasks"("project_id");

ALTER TABLE rrff."schedule_tasks" ADD CONSTRAINT "fk_tareas_gantt_etapa_id" FOREIGN KEY ("stage_id") REFERENCES rrff."stages"("stage_id") DEFERRABLE INITIALLY DEFERRED;

CREATE INDEX IF NOT EXISTS "idx_tareas_gantt_etapa_id" ON rrff."schedule_tasks"("stage_id");

ALTER TABLE rrff."schedule_tasks" ADD CONSTRAINT "fk_tareas_gantt_estado_id" FOREIGN KEY ("status_id") REFERENCES rrff."statuses"("status_id") DEFERRABLE INITIALLY DEFERRED;

CREATE INDEX IF NOT EXISTS "idx_tareas_gantt_estado_id" ON rrff."schedule_tasks"("status_id");

ALTER TABLE rrff."schedule_tasks" ADD CONSTRAINT "fk_tareas_gantt_responsable_fuente_id" FOREIGN KEY ("source_responsible_id") REFERENCES rrff."source_responsibles"("source_responsible_id") DEFERRABLE INITIALLY DEFERRED;

CREATE INDEX IF NOT EXISTS "idx_tareas_gantt_responsable_fuente_i" ON rrff."schedule_tasks"("source_responsible_id");

ALTER TABLE rrff."schedule_tasks" ADD CONSTRAINT "fk_tareas_gantt_tarea_padre_id" FOREIGN KEY ("parent_task_id") REFERENCES rrff."schedule_tasks"("task_id") DEFERRABLE INITIALLY DEFERRED;

CREATE INDEX IF NOT EXISTS "idx_tareas_gantt_tarea_padre_id" ON rrff."schedule_tasks"("parent_task_id");

ALTER TABLE rrff."schedule_tasks" ADD CONSTRAINT "fk_tareas_gantt_creado_por_usuario_id" FOREIGN KEY ("created_by_user_id") REFERENCES rrff."users"("user_id") DEFERRABLE INITIALLY DEFERRED;

CREATE INDEX IF NOT EXISTS "idx_tareas_gantt_creado_por_usuario_i" ON rrff."schedule_tasks"("created_by_user_id");

ALTER TABLE rrff."schedule_tasks" ADD CONSTRAINT "fk_tareas_gantt_actualizado_por_usuario_id" FOREIGN KEY ("updated_by_user_id") REFERENCES rrff."users"("user_id") DEFERRABLE INITIALLY DEFERRED;

CREATE INDEX IF NOT EXISTS "idx_tareas_gantt_actualizado_por_usua" ON rrff."schedule_tasks"("updated_by_user_id");

ALTER TABLE rrff."schedule_tasks" ADD CONSTRAINT "fk_tareas_gantt_verificador_id" FOREIGN KEY ("verifier_id") REFERENCES rrff."verifiers"("verifier_id") DEFERRABLE INITIALLY DEFERRED;

CREATE INDEX IF NOT EXISTS "idx_tareas_gantt_verificador_id" ON rrff."schedule_tasks"("verifier_id");

ALTER TABLE rrff."source_task_responsibles" ADD CONSTRAINT "fk_tarea_resp_tarea_id" FOREIGN KEY ("task_id") REFERENCES rrff."schedule_tasks"("task_id") DEFERRABLE INITIALLY DEFERRED;

CREATE INDEX IF NOT EXISTS "idx_tarea_resp_tarea_id" ON rrff."source_task_responsibles"("task_id");

ALTER TABLE rrff."source_task_responsibles" ADD CONSTRAINT "fk_tarea_resp_responsable_fuente_id" FOREIGN KEY ("source_responsible_id") REFERENCES rrff."source_responsibles"("source_responsible_id") DEFERRABLE INITIALLY DEFERRED;

CREATE INDEX IF NOT EXISTS "idx_tarea_resp_responsable_fuente_i" ON rrff."source_task_responsibles"("source_responsible_id");

ALTER TABLE rrff."source_task_responsibles" ADD CONSTRAINT "fk_tarea_resp_persona_id" FOREIGN KEY ("person_id") REFERENCES rrff."persons"("person_id") DEFERRABLE INITIALLY DEFERRED;

CREATE INDEX IF NOT EXISTS "idx_tarea_resp_persona_id" ON rrff."source_task_responsibles"("person_id");

ALTER TABLE rrff."source_task_responsibles" ADD CONSTRAINT "fk_tarea_resp_tipo_funcion_id" FOREIGN KEY ("assignment_role_id") REFERENCES rrff."assignment_roles"("assignment_role_id") DEFERRABLE INITIALLY DEFERRED;

CREATE INDEX IF NOT EXISTS "idx_tarea_resp_tipo_funcion_id" ON rrff."source_task_responsibles"("assignment_role_id");

ALTER TABLE rrff."statuses" ADD CONSTRAINT "fk_estados_updated_by_user_id" FOREIGN KEY ("updated_by_user_id") REFERENCES rrff."users"("user_id") DEFERRABLE INITIALLY DEFERRED;

CREATE INDEX IF NOT EXISTS "idx_estados_updated_by_user_id" ON rrff."statuses"("updated_by_user_id");

ALTER TABLE rrff."stages" ADD CONSTRAINT "fk_etapas_updated_by_user_id" FOREIGN KEY ("updated_by_user_id") REFERENCES rrff."users"("user_id") DEFERRABLE INITIALLY DEFERRED;

CREATE INDEX IF NOT EXISTS "idx_etapas_updated_by_user_id" ON rrff."stages"("updated_by_user_id");

ALTER TABLE rrff."source_rows" ADD CONSTRAINT "fk_filas_fuente_cronograma_id" FOREIGN KEY ("schedule_id") REFERENCES rrff."schedules"("schedule_id") DEFERRABLE INITIALLY DEFERRED;

CREATE INDEX IF NOT EXISTS "idx_filas_fuente_cronograma_id" ON rrff."source_rows"("schedule_id");

ALTER TABLE rrff."source_metadata" ADD CONSTRAINT "fk_metadatos_cronograma_id" FOREIGN KEY ("schedule_id") REFERENCES rrff."schedules"("schedule_id") DEFERRABLE INITIALLY DEFERRED;

CREATE INDEX IF NOT EXISTS "idx_metadatos_cronograma_id" ON rrff."source_metadata"("schedule_id");

ALTER TABLE rrff."source_cells" ADD CONSTRAINT "fk_celdas_extra_cronograma_id" FOREIGN KEY ("schedule_id") REFERENCES rrff."schedules"("schedule_id") DEFERRABLE INITIALLY DEFERRED;

CREATE INDEX IF NOT EXISTS "idx_celdas_extra_cronograma_id" ON rrff."source_cells"("schedule_id");

ALTER TABLE rrff."task_dependencies" ADD CONSTRAINT "fk_dependencias_cronograma_id" FOREIGN KEY ("schedule_id") REFERENCES rrff."schedules"("schedule_id") DEFERRABLE INITIALLY DEFERRED;

CREATE INDEX IF NOT EXISTS "idx_dependencias_cronograma_id" ON rrff."task_dependencies"("schedule_id");

ALTER TABLE rrff."task_dependencies" ADD CONSTRAINT "fk_dependencias_tarea_predecesora_id" FOREIGN KEY ("predecessor_task_id") REFERENCES rrff."schedule_tasks"("task_id") DEFERRABLE INITIALLY DEFERRED;

CREATE INDEX IF NOT EXISTS "idx_dependencias_tarea_predecesora_id" ON rrff."task_dependencies"("predecessor_task_id");

ALTER TABLE rrff."task_dependencies" ADD CONSTRAINT "fk_dependencias_tarea_sucesora_id" FOREIGN KEY ("successor_task_id") REFERENCES rrff."schedule_tasks"("task_id") DEFERRABLE INITIALLY DEFERRED;

CREATE INDEX IF NOT EXISTS "idx_dependencias_tarea_sucesora_id" ON rrff."task_dependencies"("successor_task_id");

ALTER TABLE rrff."milestones" ADD CONSTRAINT "fk_hitos_cronograma_id" FOREIGN KEY ("schedule_id") REFERENCES rrff."schedules"("schedule_id") DEFERRABLE INITIALLY DEFERRED;

CREATE INDEX IF NOT EXISTS "idx_hitos_cronograma_id" ON rrff."milestones"("schedule_id");

ALTER TABLE rrff."milestones" ADD CONSTRAINT "fk_hitos_tarea_id" FOREIGN KEY ("task_id") REFERENCES rrff."schedule_tasks"("task_id") DEFERRABLE INITIALLY DEFERRED;

CREATE INDEX IF NOT EXISTS "idx_hitos_tarea_id" ON rrff."milestones"("task_id");

ALTER TABLE rrff."milestones" ADD CONSTRAINT "fk_hitos_estado_id" FOREIGN KEY ("status_id") REFERENCES rrff."statuses"("status_id") DEFERRABLE INITIALLY DEFERRED;

CREATE INDEX IF NOT EXISTS "idx_hitos_estado_id" ON rrff."milestones"("status_id");

ALTER TABLE rrff."milestones" ADD CONSTRAINT "fk_hitos_verificador_id" FOREIGN KEY ("verifier_id") REFERENCES rrff."verifiers"("verifier_id") DEFERRABLE INITIALLY DEFERRED;

CREATE INDEX IF NOT EXISTS "idx_hitos_verificador_id" ON rrff."milestones"("verifier_id");

ALTER TABLE rrff."task_updates" ADD CONSTRAINT "fk_actualizaciones_tarea_id" FOREIGN KEY ("task_id") REFERENCES rrff."schedule_tasks"("task_id") DEFERRABLE INITIALLY DEFERRED;

CREATE INDEX IF NOT EXISTS "idx_actualizaciones_tarea_id" ON rrff."task_updates"("task_id");

ALTER TABLE rrff."task_updates" ADD CONSTRAINT "fk_actualizaciones_usuario_id" FOREIGN KEY ("user_id") REFERENCES rrff."users"("user_id") DEFERRABLE INITIALLY DEFERRED;

CREATE INDEX IF NOT EXISTS "idx_actualizaciones_usuario_id" ON rrff."task_updates"("user_id");

ALTER TABLE rrff."persons" ADD CONSTRAINT "fk_personas_actor_id" FOREIGN KEY ("actor_id") REFERENCES rrff."source_actors"("actor_id") DEFERRABLE INITIALLY DEFERRED;

CREATE INDEX IF NOT EXISTS "idx_personas_actor_id" ON rrff."persons"("actor_id");

ALTER TABLE rrff."persons" ADD CONSTRAINT "fk_personas_updated_by_user_id" FOREIGN KEY ("updated_by_user_id") REFERENCES rrff."users"("user_id") DEFERRABLE INITIALLY DEFERRED;

CREATE INDEX IF NOT EXISTS "idx_personas_updated_by_user_id" ON rrff."persons"("updated_by_user_id");

ALTER TABLE rrff."users" ADD CONSTRAINT "fk_usuarios_persona_id" FOREIGN KEY ("person_id") REFERENCES rrff."persons"("person_id") DEFERRABLE INITIALLY DEFERRED;

CREATE INDEX IF NOT EXISTS "idx_usuarios_persona_id" ON rrff."users"("person_id");

ALTER TABLE rrff."user_profiles" ADD CONSTRAINT "fk_usuario_perfil_usuario_id" FOREIGN KEY ("user_id") REFERENCES rrff."users"("user_id") DEFERRABLE INITIALLY DEFERRED;

CREATE INDEX IF NOT EXISTS "idx_usuario_perfil_usuario_id" ON rrff."user_profiles"("user_id");

ALTER TABLE rrff."user_profiles" ADD CONSTRAINT "fk_usuario_perfil_perfil_id" FOREIGN KEY ("profile_id") REFERENCES rrff."profiles"("profile_id") DEFERRABLE INITIALLY DEFERRED;

CREATE INDEX IF NOT EXISTS "idx_usuario_perfil_perfil_id" ON rrff."user_profiles"("profile_id");

ALTER TABLE rrff."project_people" ADD CONSTRAINT "fk_proy_persona_proyecto_id" FOREIGN KEY ("project_id") REFERENCES rrff."projects"("project_id") DEFERRABLE INITIALLY DEFERRED;

CREATE INDEX IF NOT EXISTS "idx_proy_persona_proyecto_id" ON rrff."project_people"("project_id");

ALTER TABLE rrff."project_people" ADD CONSTRAINT "fk_proy_persona_persona_id" FOREIGN KEY ("person_id") REFERENCES rrff."persons"("person_id") DEFERRABLE INITIALLY DEFERRED;

CREATE INDEX IF NOT EXISTS "idx_proy_persona_persona_id" ON rrff."project_people"("person_id");

ALTER TABLE rrff."project_people" ADD CONSTRAINT "fk_proy_persona_tipo_funcion_id" FOREIGN KEY ("assignment_role_id") REFERENCES rrff."assignment_roles"("assignment_role_id") DEFERRABLE INITIALLY DEFERRED;

CREATE INDEX IF NOT EXISTS "idx_proy_persona_tipo_funcion_id" ON rrff."project_people"("assignment_role_id");

ALTER TABLE rrff."project_people" ADD CONSTRAINT "fk_proy_persona_usuario_id" FOREIGN KEY ("user_id") REFERENCES rrff."users"("user_id") DEFERRABLE INITIALLY DEFERRED;

CREATE INDEX IF NOT EXISTS "idx_proy_persona_usuario_id" ON rrff."project_people"("user_id");

ALTER TABLE rrff."facilities" ADD CONSTRAINT "fk_establecimientos_tipo_establecimiento_id" FOREIGN KEY ("facility_type_id") REFERENCES rrff."facility_types"("facility_type_id") DEFERRABLE INITIALLY DEFERRED;

CREATE INDEX IF NOT EXISTS "idx_establecimientos_tipo_establecimiento" ON rrff."facilities"("facility_type_id");

ALTER TABLE rrff."facilities" ADD CONSTRAINT "fk_establecimientos_comuna_id" FOREIGN KEY ("commune_id") REFERENCES rrff."communes"("commune_id") DEFERRABLE INITIALLY DEFERRED;

CREATE INDEX IF NOT EXISTS "idx_establecimientos_comuna_id" ON rrff."facilities"("commune_id");

ALTER TABLE rrff."facilities" ADD CONSTRAINT "fk_establecimientos_updated_by_user_id" FOREIGN KEY ("updated_by_user_id") REFERENCES rrff."users"("user_id") DEFERRABLE INITIALLY DEFERRED;

CREATE INDEX IF NOT EXISTS "idx_establecimientos_updated_by_user_id" ON rrff."facilities"("updated_by_user_id");

ALTER TABLE rrff."facility_types" ADD CONSTRAINT "fk_tipos_establec_updated_by_user_id" FOREIGN KEY ("updated_by_user_id") REFERENCES rrff."users"("user_id") DEFERRABLE INITIALLY DEFERRED;

CREATE INDEX IF NOT EXISTS "idx_tipos_establec_updated_by_user_id" ON rrff."facility_types"("updated_by_user_id");

ALTER TABLE rrff."communes" ADD CONSTRAINT "fk_comunas_provincia_id" FOREIGN KEY ("province_id") REFERENCES rrff."provinces"("province_id") DEFERRABLE INITIALLY DEFERRED;

CREATE INDEX IF NOT EXISTS "idx_comunas_provincia_id" ON rrff."communes"("province_id");

ALTER TABLE rrff."funding_types" ADD CONSTRAINT "fk_financiamiento_updated_by_user_id" FOREIGN KEY ("updated_by_user_id") REFERENCES rrff."users"("user_id") DEFERRABLE INITIALLY DEFERRED;

CREATE INDEX IF NOT EXISTS "idx_financiamiento_updated_by_user_id" ON rrff."funding_types"("updated_by_user_id");

ALTER TABLE rrff."documents" ADD CONSTRAINT "fk_documentos_proyecto_id" FOREIGN KEY ("project_id") REFERENCES rrff."projects"("project_id") DEFERRABLE INITIALLY DEFERRED;

CREATE INDEX IF NOT EXISTS "idx_documentos_proyecto_id" ON rrff."documents"("project_id");

ALTER TABLE rrff."documents" ADD CONSTRAINT "fk_documentos_cronograma_id" FOREIGN KEY ("schedule_id") REFERENCES rrff."schedules"("schedule_id") DEFERRABLE INITIALLY DEFERRED;

CREATE INDEX IF NOT EXISTS "idx_documentos_cronograma_id" ON rrff."documents"("schedule_id");

ALTER TABLE rrff."documents" ADD CONSTRAINT "fk_documentos_tarea_id" FOREIGN KEY ("task_id") REFERENCES rrff."schedule_tasks"("task_id") DEFERRABLE INITIALLY DEFERRED;

CREATE INDEX IF NOT EXISTS "idx_documentos_tarea_id" ON rrff."documents"("task_id");

ALTER TABLE rrff."photos" ADD CONSTRAINT "fk_fotos_proyecto_id" FOREIGN KEY ("project_id") REFERENCES rrff."projects"("project_id") DEFERRABLE INITIALLY DEFERRED;

CREATE INDEX IF NOT EXISTS "idx_fotos_proyecto_id" ON rrff."photos"("project_id");

ALTER TABLE rrff."photos" ADD CONSTRAINT "fk_fotos_cronograma_id" FOREIGN KEY ("schedule_id") REFERENCES rrff."schedules"("schedule_id") DEFERRABLE INITIALLY DEFERRED;

CREATE INDEX IF NOT EXISTS "idx_fotos_cronograma_id" ON rrff."photos"("schedule_id");

ALTER TABLE rrff."photos" ADD CONSTRAINT "fk_fotos_tarea_id" FOREIGN KEY ("task_id") REFERENCES rrff."schedule_tasks"("task_id") DEFERRABLE INITIALLY DEFERRED;

CREATE INDEX IF NOT EXISTS "idx_fotos_tarea_id" ON rrff."photos"("task_id");

ALTER TABLE rrff."verifiers" ADD CONSTRAINT "fk_verificadores_updated_by_user_id" FOREIGN KEY ("updated_by_user_id") REFERENCES rrff."users"("user_id") DEFERRABLE INITIALLY DEFERRED;

CREATE INDEX IF NOT EXISTS "idx_verificadores_updated_by_user_id" ON rrff."verifiers"("updated_by_user_id");

ALTER TABLE rrff."budget_items" ADD CONSTRAINT "fk_items_montos_proyecto_id" FOREIGN KEY ("project_id") REFERENCES rrff."projects"("project_id") DEFERRABLE INITIALLY DEFERRED;

CREATE INDEX IF NOT EXISTS "idx_items_montos_proyecto_id" ON rrff."budget_items"("project_id");

ALTER TABLE rrff."budget_items" ADD CONSTRAINT "fk_items_montos_cronograma_id" FOREIGN KEY ("schedule_id") REFERENCES rrff."schedules"("schedule_id") DEFERRABLE INITIALLY DEFERRED;

CREATE INDEX IF NOT EXISTS "idx_items_montos_cronograma_id" ON rrff."budget_items"("schedule_id");

ALTER TABLE rrff."budget_movements" ADD CONSTRAINT "fk_movimientos_item_monto_id" FOREIGN KEY ("budget_item_id") REFERENCES rrff."budget_items"("budget_item_id") DEFERRABLE INITIALLY DEFERRED;

CREATE INDEX IF NOT EXISTS "idx_movimientos_item_monto_id" ON rrff."budget_movements"("budget_item_id");

ALTER TABLE rrff."budget_movements" ADD CONSTRAINT "fk_movimientos_usuario_id" FOREIGN KEY ("user_id") REFERENCES rrff."users"("user_id") DEFERRABLE INITIALLY DEFERRED;

CREATE INDEX IF NOT EXISTS "idx_movimientos_usuario_id" ON rrff."budget_movements"("user_id");

ALTER TABLE rrff."audit_log" ADD CONSTRAINT "fk_auditoria_usuario_id" FOREIGN KEY ("user_id") REFERENCES rrff."users"("user_id") DEFERRABLE INITIALLY DEFERRED;

CREATE INDEX IF NOT EXISTS "idx_auditoria_usuario_id" ON rrff."audit_log"("user_id");

ALTER TABLE rrff."source_actors" ADD CONSTRAINT "fk_actores_origen_persona_id_candidata" FOREIGN KEY ("candidate_person_id") REFERENCES rrff."persons"("person_id") DEFERRABLE INITIALLY DEFERRED;

CREATE INDEX IF NOT EXISTS "idx_actores_origen_persona_id_candidata" ON rrff."source_actors"("candidate_person_id");

ALTER TABLE rrff."task_actors" ADD CONSTRAINT "fk_tarea_actores_tarea_id" FOREIGN KEY ("task_id") REFERENCES rrff."schedule_tasks"("task_id") DEFERRABLE INITIALLY DEFERRED;

CREATE INDEX IF NOT EXISTS "idx_tarea_actores_tarea_id" ON rrff."task_actors"("task_id");

ALTER TABLE rrff."task_actors" ADD CONSTRAINT "fk_tarea_actores_cronograma_id" FOREIGN KEY ("schedule_id") REFERENCES rrff."schedules"("schedule_id") DEFERRABLE INITIALLY DEFERRED;

CREATE INDEX IF NOT EXISTS "idx_tarea_actores_cronograma_id" ON rrff."task_actors"("schedule_id");

ALTER TABLE rrff."task_actors" ADD CONSTRAINT "fk_tarea_actores_proyecto_id" FOREIGN KEY ("project_id") REFERENCES rrff."projects"("project_id") DEFERRABLE INITIALLY DEFERRED;

CREATE INDEX IF NOT EXISTS "idx_tarea_actores_proyecto_id" ON rrff."task_actors"("project_id");

ALTER TABLE rrff."task_actors" ADD CONSTRAINT "fk_tarea_actores_responsable_fuente_id" FOREIGN KEY ("source_responsible_id") REFERENCES rrff."source_responsibles"("source_responsible_id") DEFERRABLE INITIALLY DEFERRED;

CREATE INDEX IF NOT EXISTS "idx_tarea_actores_responsable_fuente_i" ON rrff."task_actors"("source_responsible_id");

ALTER TABLE rrff."task_actors" ADD CONSTRAINT "fk_tarea_actores_actor_id" FOREIGN KEY ("actor_id") REFERENCES rrff."source_actors"("actor_id") DEFERRABLE INITIALLY DEFERRED;

CREATE INDEX IF NOT EXISTS "idx_tarea_actores_actor_id" ON rrff."task_actors"("actor_id");

ALTER TABLE rrff."task_actors" ADD CONSTRAINT "fk_tarea_actores_persona_id_candidata" FOREIGN KEY ("candidate_person_id") REFERENCES rrff."persons"("person_id") DEFERRABLE INITIALLY DEFERRED;

CREATE INDEX IF NOT EXISTS "idx_tarea_actores_persona_id_candidata" ON rrff."task_actors"("candidate_person_id");

ALTER TABLE rrff."task_actors" ADD CONSTRAINT "fk_tarea_actores_usuario_id_candidato" FOREIGN KEY ("candidate_user_id") REFERENCES rrff."users"("user_id") DEFERRABLE INITIALLY DEFERRED;

CREATE INDEX IF NOT EXISTS "idx_tarea_actores_usuario_id_candidato" ON rrff."task_actors"("candidate_user_id");

ALTER TABLE rrff."task_people" ADD CONSTRAINT "fk_tarea_personas_tarea_id" FOREIGN KEY ("task_id") REFERENCES rrff."schedule_tasks"("task_id") DEFERRABLE INITIALLY DEFERRED;

CREATE INDEX IF NOT EXISTS "idx_tarea_personas_tarea_id" ON rrff."task_people"("task_id");

ALTER TABLE rrff."task_people" ADD CONSTRAINT "fk_tarea_personas_persona_id" FOREIGN KEY ("person_id") REFERENCES rrff."persons"("person_id") DEFERRABLE INITIALLY DEFERRED;

CREATE INDEX IF NOT EXISTS "idx_tarea_personas_persona_id" ON rrff."task_people"("person_id");

ALTER TABLE rrff."task_people" ADD CONSTRAINT "fk_tarea_personas_responsable_fuente_id" FOREIGN KEY ("source_responsible_id") REFERENCES rrff."source_responsibles"("source_responsible_id") DEFERRABLE INITIALLY DEFERRED;

CREATE INDEX IF NOT EXISTS "idx_tarea_personas_responsable_fuente_i" ON rrff."task_people"("source_responsible_id");

ALTER TABLE rrff."task_people" ADD CONSTRAINT "fk_tarea_personas_usuario_id" FOREIGN KEY ("user_id") REFERENCES rrff."users"("user_id") DEFERRABLE INITIALLY DEFERRED;

CREATE INDEX IF NOT EXISTS "idx_tarea_personas_usuario_id" ON rrff."task_people"("user_id");

ALTER TABLE rrff."person_users" ADD CONSTRAINT "fk_persona_usuario_usuario_id" FOREIGN KEY ("user_id") REFERENCES rrff."users"("user_id") DEFERRABLE INITIALLY DEFERRED;

CREATE INDEX IF NOT EXISTS "idx_persona_usuario_usuario_id" ON rrff."person_users"("user_id");

ALTER TABLE rrff."person_users" ADD CONSTRAINT "fk_persona_usuario_actor_id" FOREIGN KEY ("actor_id") REFERENCES rrff."source_actors"("actor_id") DEFERRABLE INITIALLY DEFERRED;

CREATE INDEX IF NOT EXISTS "idx_persona_usuario_actor_id" ON rrff."person_users"("actor_id");

ALTER TABLE rrff."actor_users" ADD CONSTRAINT "fk_actor_usuario_persona_id_candidata" FOREIGN KEY ("candidate_person_id") REFERENCES rrff."persons"("person_id") DEFERRABLE INITIALLY DEFERRED;

CREATE INDEX IF NOT EXISTS "idx_actor_usuario_persona_id_candidata" ON rrff."actor_users"("candidate_person_id");

ALTER TABLE rrff."actor_users" ADD CONSTRAINT "fk_actor_usuario_usuario_id" FOREIGN KEY ("user_id") REFERENCES rrff."users"("user_id") DEFERRABLE INITIALLY DEFERRED;

CREATE INDEX IF NOT EXISTS "idx_actor_usuario_usuario_id" ON rrff."actor_users"("user_id");

-- Relaciones compuestas que garantizan consistencia entre proyecto, cronograma y tarea.
ALTER TABLE rrff.schedules ADD CONSTRAINT uq_schedule_project UNIQUE (schedule_id, project_id);
ALTER TABLE rrff.schedule_tasks ADD CONSTRAINT uq_task_schedule_project UNIQUE (task_id, schedule_id, project_id);
ALTER TABLE rrff.person_users ADD CONSTRAINT uq_person_user UNIQUE (person_id, user_id);
ALTER TABLE rrff.users ADD CONSTRAINT uq_user_person UNIQUE (person_id);
ALTER TABLE rrff.schedule_tasks ADD CONSTRAINT fk_task_schedule_project FOREIGN KEY (schedule_id, project_id) REFERENCES rrff.schedules(schedule_id, project_id) DEFERRABLE INITIALLY DEFERRED;
ALTER TABLE rrff.task_actors ADD CONSTRAINT fk_task_actor_consistency FOREIGN KEY (task_id, schedule_id, project_id) REFERENCES rrff.schedule_tasks(task_id, schedule_id, project_id) DEFERRABLE INITIALLY DEFERRED;
ALTER TABLE rrff.task_people ADD CONSTRAINT fk_task_person_user_consistency FOREIGN KEY (person_id, user_id) REFERENCES rrff.person_users(person_id, user_id) DEFERRABLE INITIALLY DEFERRED;
ALTER TABLE rrff.project_people ADD CONSTRAINT fk_project_person_user_consistency FOREIGN KEY (person_id, user_id) REFERENCES rrff.person_users(person_id, user_id) DEFERRABLE INITIALLY DEFERRED;
ALTER TABLE rrff.task_actors ADD CONSTRAINT fk_task_actor_person_user_consistency FOREIGN KEY (candidate_person_id, candidate_user_id) REFERENCES rrff.person_users(person_id, user_id) DEFERRABLE INITIALLY DEFERRED;

COMMENT ON TABLE rrff."users" IS 'Identificadores provisionales provenientes de initials; no representan cuentas con autenticacion habilitada';

CREATE TABLE IF NOT EXISTS rrff.project_types (project_type_id TEXT PRIMARY KEY, code TEXT, description TEXT, active BOOLEAN);
ALTER TABLE rrff.projects ADD CONSTRAINT fk_project_type FOREIGN KEY (project_type_id) REFERENCES rrff.project_types(project_type_id) DEFERRABLE INITIALLY DEFERRED;
COMMIT;
