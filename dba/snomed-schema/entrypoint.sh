#!/bin/bash

# Variables de entorno que se deben establecer en el job de Kubernetes:
# - POSTGRES_HOST: Dirección del servidor PostgreSQL
# - POSTGRES_PORT: Puerto del servidor PostgreSQL
# - POSTGRES_USER: Usuario de la base de datos
# - POSTGRES_PASSWORD: Contraseña del usuario de la base de datos
# - POSTGRES_DB: Nombre de la base de datos

# Verifica que todas las variables necesarias están establecidas
if [ -z "$POSTGRES_HOST" ] || [ -z "$POSTGRES_PORT" ] || [ -z "$POSTGRES_USER" ] || [ -z "$POSTGRES_PASSWORD" ] || [ -z "$POSTGRES_DB" ]; then
  echo "Falta una o más variables de entorno necesarias (POSTGRES_HOST, POSTGRES_PORT, POSTGRES_USER, POSTGRES_PASSWORD, POSTGRES_DB)."
  exit 1
fi

# Verifica que deba crearse el esquema commercial_medication
if [ -z "$CREATE_COMMERCIAL_MEDICATION_SCHEMA" ] || [ "$CREATE_COMMERCIAL_MEDICATION_SCHEMA" = "false" ]; then
  echo "Actualizando esquema"
  PGPASSWORD=$POSTGRES_PASSWORD psql -h $POSTGRES_HOST -p $POSTGRES_PORT -U $POSTGRES_USER -d $POSTGRES_DB -f "create-schema.sql"

  # Función para ejecutar el comando psql \copy con un archivo TXT
  run_copy() {
    local file=$1
    local table=$2
    echo "Copiando file $file en table $table"

    export PGPASSWORD=$POSTGRES_PASSWORD
    # Salteo primer fila
    tail -n +2 $file | psql -h $POSTGRES_HOST -p $POSTGRES_PORT -U $POSTGRES_USER -d $POSTGRES_DB -c "\copy $table FROM STDIN WITH (FORMAT text, DELIMITER E'\t', ENCODING 'UTF8')"
  }

  echo "Tabla concept"
  run_copy "Release2405/Snapshot/Terminology/sct2_Concept_Snapshot_ArgentinaEdition_20240531.txt" "snomedct.concept(id, effectivetime, active, moduleid, definitionstatusid)"
  echo "Tabla description"
  run_copy "Release2405/Snapshot/Terminology/sct2_Description_Snapshot_ArgentinaEdition_20240531.txt" "snomedct.description(id, effectivetime, active, moduleid, conceptid, languagecode, typeid, term, casesignificanceid)"
  echo "Tabla relationship"
  run_copy "Release2405/Snapshot/Terminology/sct2_Relationship_Snapshot_ArgentinaEdition_20240531.txt" "snomedct.relationship(id, effectivetime, active, moduleid, sourceid, destinationid, relationshipgroup, typeid,characteristictypeid, modifierid)"
  echo "Tabla langrefset"
  run_copy "Release2405/Snapshot/Refset/Language/der2_cRefset_LanguageSnapshot_ArgentinaEdition_20240531.txt" "snomedct.langrefset(id, effectivetime, active, moduleid, refsetid, referencedcomponentid, acceptabilityid)"
  echo "Tabla relationship_concrete_value"
  run_copy "Release2405/Snapshot/Terminology/sct2_RelationshipConcreteValues_Snapshot_ArgentinaEdition_20240531.txt" "snomedct.relationship_concrete_value(id, effectivetime, active, moduleid, sourceid, value, relationshipgroup, typeid, characteristictypeid, modifierid)"

  echo "Poblar tabla concept_term"
  PGPASSWORD=$POSTGRES_PASSWORD psql -h $POSTGRES_HOST -p $POSTGRES_PORT -U $POSTGRES_USER -d $POSTGRES_DB -f "concept_term.sql"

  echo "Redefiniendo las vistas..."
  # En este paso pisaríamos las vistas por defecto de HSI
  PGPASSWORD=$POSTGRES_PASSWORD psql -h $POSTGRES_HOST -p $POSTGRES_PORT -U $POSTGRES_USER -d $POSTGRES_DB -f "views.sql"
else
  CREATE_SCHEMA=$(PGPASSWORD=$POSTGRES_PASSWORD psql -h $POSTGRES_HOST -p $POSTGRES_PORT -U $POSTGRES_USER -d $POSTGRES_DB -t -A -c "SELECT 1 FROM information_schema.schemata WHERE schema_name = 'commercial_medication';")
  if [ -z "$CREATE_SCHEMA" ]; then
    echo "Creating commercial_medication schema..."
    PGPASSWORD=$POSTGRES_PASSWORD psql -h $POSTGRES_HOST -p $POSTGRES_PORT -U $POSTGRES_USER -d $POSTGRES_DB -f "create-commercial-medication-schema.sql"
  fi
fi

# Comandos psql adicionales (modifica o añade lo que necesites aquí)
# Ejemplo:
# PGPASSWORD=$POSTGRES_PASSWORD psql -h $POSTGRES_HOST -p $POSTGRES_PORT -U $POSTGRES_USER -d $POSTGRES_DB -c "SELECT 'Hola, mundo';"

# Para evitar que el contenedor termine inmediatamente, puedes dejar un comando en ejecución o simplemente salir.
# Aquí optamos por salir ya que asumimos que esto se ejecutará como un job que termina al completar.
echo "Ejecución de scripts SQL completada."
exit 0
