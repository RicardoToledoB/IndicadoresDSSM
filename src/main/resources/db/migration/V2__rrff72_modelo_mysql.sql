-- RRFF-SSM 7.2 - Modelo MySQL 8.0 adaptado de R05 PostgreSQL.

-- Datos R04 auditados; conserva ids literales; no confundir rrff72.users con autenticacion.

-- Prefijo r72_ para coexistir con el backend M05 y evitar conflictos de nombres.

SET NAMES utf8mb4;

CREATE TABLE IF NOT EXISTS `r72_projects` (
  `project_id` VARCHAR(80) NOT NULL,
  `source_name` TEXT NULL,
  `source_bip_code` TEXT NULL,
  `source_file_id` VARCHAR(80) NULL,
  `project_type_id` VARCHAR(80) NULL,
  `facility_id` VARCHAR(80) NULL,
  `province_id` VARCHAR(80) NULL,
  `commune_id` VARCHAR(80) NULL,
  `funding_type_id` VARCHAR(80) NULL,
  `notes` TEXT NULL,
  `active` TINYINT(1) NULL,
  `created_at` DATETIME(6) NULL,
  `updated_at` DATETIME(6) NULL,
  `updated_by_user_id` VARCHAR(80) NULL,
  PRIMARY KEY (`project_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `r72_source_files` (
  `source_file_id` VARCHAR(80) NOT NULL,
  `project_id` VARCHAR(80) NULL,
  `original_file_name` TEXT NULL,
  `sha256` TEXT NULL,
  `sheet_count` INT NULL,
  PRIMARY KEY (`source_file_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `r72_schedules` (
  `schedule_id` VARCHAR(80) NOT NULL,
  `project_id` VARCHAR(80) NULL,
  `source_file_id` VARCHAR(80) NULL,
  `source_sheet_name` TEXT NULL,
  `cutoff_date` DATE NULL,
  `source_overall_progress` DECIMAL(18,6) NULL,
  `header_row` INT NULL,
  `activity_count` INT NULL,
  `status_id` VARCHAR(80) NULL,
  `version_published` TINYINT(1) NULL,
  `created_at` DATETIME(6) NULL,
  `updated_at` DATETIME(6) NULL,
  `updated_by_user_id` VARCHAR(80) NULL,
  PRIMARY KEY (`schedule_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `r72_schedule_tasks` (
  `task_id` VARCHAR(80) NOT NULL,
  `schedule_id` VARCHAR(80) NULL,
  `project_id` VARCHAR(80) NULL,
  `code_hierarchical` TEXT NULL,
  `parent_code` TEXT NULL,
  `record_type` TEXT NULL,
  `stage_id` VARCHAR(80) NULL,
  `source_stage_text` TEXT NULL,
  `activity_o_substage` TEXT NULL,
  `source_verifier` TEXT NULL,
  `status_id` VARCHAR(80) NULL,
  `source_status` TEXT NULL,
  `source_responsible_id` VARCHAR(80) NULL,
  `source_responsible_text` TEXT NULL,
  `start_date` DATE NULL,
  `duration_days` TEXT NULL,
  `end_date` DATE NULL,
  `source_row_number` INT NULL,
  `actual_progress_percent` DECIMAL(18,6) NULL,
  `weight` DECIMAL(18,6) NULL,
  `parent_task_id` VARCHAR(80) NULL,
  `created_by_user_id` VARCHAR(80) NULL,
  `updated_by_user_id` VARCHAR(80) NULL,
  `verifier_id` VARCHAR(80) NULL,
  PRIMARY KEY (`task_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `r72_source_responsibles` (
  `source_responsible_id` VARCHAR(80) NOT NULL,
  `original_text` TEXT NULL,
  `validated_person_id` VARCHAR(80) NULL,
  `note` TEXT NULL,
  PRIMARY KEY (`source_responsible_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `r72_source_task_responsibles` (
  `assignment_id` VARCHAR(80) NOT NULL,
  `task_id` VARCHAR(80) NULL,
  `source_responsible_id` VARCHAR(80) NULL,
  `person_id` VARCHAR(80) NULL,
  `assignment_role_id` VARCHAR(80) NULL,
  PRIMARY KEY (`assignment_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `r72_statuses` (
  `status_id` VARCHAR(80) NOT NULL,
  `code_original` TEXT NULL,
  `pending_description` TEXT NULL,
  `created_at` DATETIME(6) NULL,
  `updated_at` DATETIME(6) NULL,
  `updated_by_user_id` VARCHAR(80) NULL,
  PRIMARY KEY (`status_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `r72_stages` (
  `stage_id` VARCHAR(80) NOT NULL,
  `source_name` TEXT NULL,
  `description` TEXT NULL,
  `created_at` DATETIME(6) NULL,
  `updated_at` DATETIME(6) NULL,
  `updated_by_user_id` VARCHAR(80) NULL,
  PRIMARY KEY (`stage_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `r72_source_rows` (
  `source_row_id` VARCHAR(80) NOT NULL,
  `schedule_id` VARCHAR(80) NULL,
  `number_row` INT NULL,
  `source_col_b_code` TEXT NULL,
  `source_col_c_stage` TEXT NULL,
  `source_col_d_substage` TEXT NULL,
  `source_col_e_verifier` TEXT NULL,
  `source_col_f_status` TEXT NULL,
  `source_col_g_responsible` TEXT NULL,
  `source_col_h_start` TEXT NULL,
  `source_col_i_duration` TEXT NULL,
  `source_col_j_end` TEXT NULL,
  PRIMARY KEY (`source_row_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `r72_source_metadata` (
  `source_metadata_id` VARCHAR(80) NOT NULL,
  `schedule_id` VARCHAR(80) NULL,
  `original_row` TEXT NULL,
  `original_column` TEXT NULL,
  `original_value` TEXT NULL,
  PRIMARY KEY (`source_metadata_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `r72_source_cells` (
  `source_cell_id` VARCHAR(80) NOT NULL,
  `schedule_id` VARCHAR(80) NULL,
  `cell` TEXT NULL,
  `original_value` TEXT NULL,
  PRIMARY KEY (`source_cell_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `r72_task_dependencies` (
  `dependency_id` VARCHAR(80) NOT NULL,
  `schedule_id` VARCHAR(80) NULL,
  `predecessor_task_id` VARCHAR(80) NULL,
  `successor_task_id` VARCHAR(80) NULL,
  `dependency_type` TEXT NULL,
  `lag_days` INT NULL,
  `note` TEXT NULL,
  PRIMARY KEY (`dependency_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `r72_milestones` (
  `milestone_id` VARCHAR(80) NOT NULL,
  `schedule_id` VARCHAR(80) NULL,
  `task_id` VARCHAR(80) NULL,
  `name` TEXT NULL,
  `planned_date` DATE NULL,
  `actual_date` DATE NULL,
  `status_id` VARCHAR(80) NULL,
  `verifier_id` VARCHAR(80) NULL,
  PRIMARY KEY (`milestone_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `r72_task_updates` (
  `update_id` VARCHAR(80) NOT NULL,
  `task_id` VARCHAR(80) NULL,
  `cutoff_date` DATE NULL,
  `progress_percent` DECIMAL(18,6) NULL,
  `note` TEXT NULL,
  `user_id` VARCHAR(80) NULL,
  `recorded_at` DATETIME(6) NULL,
  PRIMARY KEY (`update_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `r72_persons` (
  `person_id` VARCHAR(80) NOT NULL,
  `full_name` TEXT NULL,
  `institutional_identifier` TEXT NULL,
  `email` TEXT NULL,
  `active` TINYINT(1) NULL,
  `source_initials` TEXT NULL,
  `actor_id` VARCHAR(80) NULL,
  `identification_status` TEXT NULL,
  `note` TEXT NULL,
  `created_at` DATETIME(6) NULL,
  `updated_at` DATETIME(6) NULL,
  `updated_by_user_id` VARCHAR(80) NULL,
  PRIMARY KEY (`person_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `r72_assignment_roles` (
  `assignment_role_id` VARCHAR(80) NOT NULL,
  `role_name` TEXT NULL,
  `description` TEXT NULL,
  PRIMARY KEY (`assignment_role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `r72_users` (
  `user_id` VARCHAR(80) NOT NULL,
  `person_id` VARCHAR(80) NULL,
  `identifier_source` TEXT NULL,
  `login_actual` TEXT NULL,
  `active` TINYINT(1) NULL,
  `status` TEXT NULL,
  `note` TEXT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `r72_profiles` (
  `profile_id` VARCHAR(80) NOT NULL,
  `code` TEXT NULL,
  `description` TEXT NULL,
  `active` TINYINT(1) NULL,
  PRIMARY KEY (`profile_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `r72_user_profiles` (
  `user_profile_id` VARCHAR(80) NOT NULL,
  `user_id` VARCHAR(80) NULL,
  `profile_id` VARCHAR(80) NULL,
  `valid_from` DATE NULL,
  `valid_to` DATE NULL,
  PRIMARY KEY (`user_profile_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `r72_project_people` (
  `project_person_id` VARCHAR(80) NOT NULL,
  `project_id` VARCHAR(80) NULL,
  `person_id` VARCHAR(80) NULL,
  `assignment_role_id` VARCHAR(80) NULL,
  `valid_from` DATE NULL,
  `valid_to` DATE NULL,
  `active` TINYINT(1) NULL,
  `user_id` VARCHAR(80) NULL,
  PRIMARY KEY (`project_person_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `r72_facilities` (
  `facility_id` VARCHAR(80) NOT NULL,
  `name` TEXT NULL,
  `facility_type_id` VARCHAR(80) NULL,
  `commune_id` VARCHAR(80) NULL,
  `active` TINYINT(1) NULL,
  `note_source` TEXT NULL,
  `created_at` DATETIME(6) NULL,
  `updated_at` DATETIME(6) NULL,
  `updated_by_user_id` VARCHAR(80) NULL,
  PRIMARY KEY (`facility_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `r72_facility_types` (
  `facility_type_id` VARCHAR(80) NOT NULL,
  `code` TEXT NULL,
  `description` TEXT NULL,
  `created_at` DATETIME(6) NULL,
  `updated_at` DATETIME(6) NULL,
  `updated_by_user_id` VARCHAR(80) NULL,
  PRIMARY KEY (`facility_type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `r72_provinces` (
  `province_id` VARCHAR(80) NOT NULL,
  `name` TEXT NULL,
  PRIMARY KEY (`province_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `r72_communes` (
  `commune_id` VARCHAR(80) NOT NULL,
  `province_id` VARCHAR(80) NULL,
  `name` TEXT NULL,
  PRIMARY KEY (`commune_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `r72_funding_types` (
  `funding_id` VARCHAR(80) NOT NULL,
  `description` TEXT NULL,
  `active` TINYINT(1) NULL,
  `created_at` DATETIME(6) NULL,
  `updated_at` DATETIME(6) NULL,
  `updated_by_user_id` VARCHAR(80) NULL,
  PRIMARY KEY (`funding_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `r72_documents` (
  `document_id` VARCHAR(80) NOT NULL,
  `project_id` VARCHAR(80) NULL,
  `schedule_id` VARCHAR(80) NULL,
  `task_id` VARCHAR(80) NULL,
  `file_name` TEXT NULL,
  `document_type` TEXT NULL,
  `document_date` DATE NULL,
  `location_repository` TEXT NULL,
  PRIMARY KEY (`document_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `r72_photos` (
  `photo_id` VARCHAR(80) NOT NULL,
  `project_id` VARCHAR(80) NULL,
  `schedule_id` VARCHAR(80) NULL,
  `task_id` VARCHAR(80) NULL,
  `file_name` TEXT NULL,
  `photo_date` DATE NULL,
  `description` TEXT NULL,
  `location_repository` TEXT NULL,
  PRIMARY KEY (`photo_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `r72_verifiers` (
  `verifier_id` VARCHAR(80) NOT NULL,
  `verifier_type` TEXT NULL,
  `description` TEXT NULL,
  `created_at` DATETIME(6) NULL,
  `updated_at` DATETIME(6) NULL,
  `updated_by_user_id` VARCHAR(80) NULL,
  PRIMARY KEY (`verifier_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `r72_budget_items` (
  `budget_item_id` VARCHAR(80) NOT NULL,
  `project_id` VARCHAR(80) NULL,
  `schedule_id` VARCHAR(80) NULL,
  `description` TEXT NULL,
  `initial_amount` DECIMAL(18,6) NULL,
  `current_amount` DECIMAL(18,6) NULL,
  `currency_unit` TEXT NULL,
  `reference_date` DATE NULL,
  PRIMARY KEY (`budget_item_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `r72_budget_movements` (
  `budget_movement_id` VARCHAR(80) NOT NULL,
  `budget_item_id` VARCHAR(80) NULL,
  `date` DATE NULL,
  `amount` DECIMAL(18,6) NULL,
  `movement_type` TEXT NULL,
  `note` TEXT NULL,
  `user_id` VARCHAR(80) NULL,
  PRIMARY KEY (`budget_movement_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `r72_audit_log` (
  `audit_id` VARCHAR(80) NOT NULL,
  `table` TEXT NULL,
  `record_id` VARCHAR(80) NULL,
  `operation` TEXT NULL,
  `user_id` VARCHAR(80) NULL,
  `date_time` DATETIME(6) NULL,
  `detail` TEXT NULL,
  PRIMARY KEY (`audit_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `r72_source_actors` (
  `actor_id` VARCHAR(80) NOT NULL,
  `source_alias` TEXT NULL,
  `provisional_type` TEXT NULL,
  `candidate_person_id` VARCHAR(80) NULL,
  `note` TEXT NULL,
  PRIMARY KEY (`actor_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `r72_task_actors` (
  `relation_id` VARCHAR(80) NOT NULL,
  `task_id` VARCHAR(80) NULL,
  `schedule_id` VARCHAR(80) NULL,
  `project_id` VARCHAR(80) NULL,
  `source_responsible_id` VARCHAR(80) NULL,
  `actor_id` VARCHAR(80) NULL,
  `candidate_person_id` VARCHAR(80) NULL,
  `source_assignment_role` TEXT NULL,
  `source_column` TEXT NULL,
  `notes` TEXT NULL,
  `candidate_user_id` VARCHAR(80) NULL,
  PRIMARY KEY (`relation_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `r72_task_people` (
  `relation_id` VARCHAR(80) NOT NULL,
  `task_id` VARCHAR(80) NULL,
  `person_id` VARCHAR(80) NULL,
  `role` TEXT NULL,
  `source_responsible_id` VARCHAR(80) NULL,
  `valid_from` DATE NULL,
  `valid_to` DATE NULL,
  `user_id` VARCHAR(80) NULL,
  PRIMARY KEY (`relation_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `r72_person_users` (
  `person_id` VARCHAR(80) NOT NULL,
  `user_id` VARCHAR(80) NULL,
  `initials` TEXT NULL,
  `actor_id` VARCHAR(80) NULL,
  `status` TEXT NULL,
  PRIMARY KEY (`person_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `r72_actor_users` (
  `actor_id` VARCHAR(80) NOT NULL,
  `source_alias` TEXT NULL,
  `candidate_person_id` VARCHAR(80) NULL,
  `user_id` VARCHAR(80) NULL,
  `provisional_type` TEXT NULL,
  PRIMARY KEY (`actor_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `r72_project_types` (
  `project_type_id` VARCHAR(80) NOT NULL,
  `code` TEXT NULL,
  `description` TEXT NULL,
  `active` TINYINT(1) NULL,
  PRIMARY KEY (`project_type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
