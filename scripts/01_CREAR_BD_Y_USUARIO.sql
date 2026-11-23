-- RRFF-SSM 7.2 / MySQL 8 — instalacion inicial en servidor Ubuntu
-- Ejecutar UNA sola vez como administrador MySQL:
--   sudo mysql < scripts/01_CREAR_BD_Y_USUARIO.sql
-- AVISO: no importa datos de negocio. Flyway cargara el maestro R04 en el primer arranque.
-- AVISO: si ya existe rrff_app, CREATE USER IF NOT EXISTS NO cambia su contrasena.
--         Si ya existe rrff_ssm con tablas, no iniciar hasta revisar su estado.
CREATE DATABASE IF NOT EXISTS `rrff_ssm`
  CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE USER IF NOT EXISTS 'rrff_app'@'localhost'
  IDENTIFIED BY 'dK4rYmEQb6vSWmPCV1PY7zJ0sv1uB902';

GRANT SELECT, INSERT, UPDATE, DELETE, CREATE, ALTER, INDEX, REFERENCES
  ON `rrff_ssm`.* TO 'rrff_app'@'localhost';

SHOW GRANTS FOR 'rrff_app'@'localhost';
SELECT SCHEMA_NAME FROM information_schema.SCHEMATA WHERE SCHEMA_NAME='rrff_ssm';
