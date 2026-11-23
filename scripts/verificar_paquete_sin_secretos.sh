#!/usr/bin/env bash
set -euo pipefail
cd "$(dirname "$0")/.."
printf '%s\n' '=== Versiones ==='
java -version
mvn -version | head -3
printf '%s\n' '=== Configuracion requerida ==='
grep -E '^(server.port|server.address|spring.datasource.url|spring.datasource.username|spring.flyway.enabled|app.features.writes-enabled)=' src/main/resources/application.properties
printf '%s\n' '=== Comprobacion de contenido (sin imprimir secretos) ==='
for f in scripts/01_CREAR_BD_Y_USUARIO.sql ACCESO_INICIAL.txt docs/INSTALAR_UBUNTU_MYSQL_SWAGGER.md; do
  test -s "$f" || { echo "Falta $f"; exit 1; }
done
echo OK
