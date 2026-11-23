#!/usr/bin/env bash
# RRFF-SSM: comprobaciones locales SIN modificar datos.
set -u
echo '=== Java ==='
java -version 2>&1 | head -3 || echo 'Java no encontrado'
echo '=== Maven ==='
if command -v mvn >/dev/null; then mvn -version | head -4; else echo 'Instalar Maven (sudo apt install maven)'; fi
echo '=== Tablas existentes en rrff_ssm (solicita clave por terminal) ==='
if command -v mysql >/dev/null; then
  mysql -u rrff_app -p -N -e 'SELECT table_name FROM information_schema.tables WHERE table_schema="rrff_ssm" ORDER BY table_name;'
else
  echo 'Cliente mysql no encontrado'
fi
echo 'ATENCION: si ya hay tablas, NO arrancar Flyway sin revisar la base existente.'
