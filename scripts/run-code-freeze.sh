#!/bin/sh
set -o errexit

usuario="$CF_USER" # variables de gitlab CI/CD
token="$CF_TOKEN"

# repos
repositorio="https://$usuario:$token@gitlab.lamansys.ar/minsal/hsi.git"
repositorio_ambientes="https://$usuario:$token@gitlab.lamansys.ar/minsal/ambientes.git"
# clona repos
git clone "$repositorio" 
git clone "$repositorio_ambientes"

echo "Repos clonados"

export PROJECT_DIR_HSI='hsi' # en el script original estas son rutas que se pasan por parametro
export PROJECT_DIR_AMBIENTES='../ambientes'

if   [ -z $PROJECT_DIR_HSI ]; then
  echo 'Falta especificar como parámetro la ruta completa del proyecto HSI'
  exit 1
fi

if   [ -z $PROJECT_DIR_AMBIENTES ]; then
  echo 'Falta especificar como parámetro la ruta completa del proyecto HSI AMBIENTES'
  exit 1
fi

cd "$PROJECT_DIR_HSI"
#REMOTE=${2:-"origin"}
REMOTE="origin"

echo "
###1. Pasarte a master
"
git checkout master
git fetch ${REMOTE} master
git reset --hard ${REMOTE}/master

echo "
### 2. Leer version
"
export CURR_VERSION=$(mvn -f=back-end/pom-parent.xml help:evaluate -Dexpression=revision -q -DforceStdout)
export M_VERSION=$(echo $CURR_VERSION | cut -d . -f 1)
export MIN_VERSION=$(echo $CURR_VERSION | cut -d . -f 2)

export MM_VERSION=$M_VERSION.$MIN_VERSION
export MM_VERSION_AMBIENTES=$M_VERSION-$MIN_VERSION
export MM_VERSION_AMBIENTES_ANTERIOR=$M_VERSION-$(($MIN_VERSION-1))
export NEXT_MM_VERSION=$M_VERSION.$(($MIN_VERSION+1))
echo "
  Versión actual: $CURR_VERSION
  Branch de regresión para version $MM_VERSION
  Proxima version $NEXT_MM_VERSION
  Acordate de tener el back-up de la DB de stage para usar en $MM_VERSION_AMBIENTES_ANTERIOR
"

# Hasta aca viene bien
git config --global user.email "telescolucas19@gmail.com"
git config --global user.name "lgtelesco"

echo "
### 3. Comenzar nuevo sprint
"
# Cambiar POM a version "0.14.0-SNAPSHOT"
git branch -D code-freeze || true #si existe borro el branch
git checkout -b code-freeze

# Define la ruta al archivo pom.xml
pom_file="back-end/pom-parent.xml"

# Define el nuevo valor de la propiedad 'revision'
nuevo_revision="$NEXT_MM_VERSION.0-SNAPSHOT"

# Verifica si el archivo pom.xml existe
if [ -f "$pom_file" ]; then
  # Utiliza 'awk' para buscar y reemplazar la propiedad 'revision'
  awk -v new_revision="$nuevo_revision" '/<revision>/{gsub(/<revision>.*<\/revision>/, "<revision>" new_revision "</revision>")}1' "$pom_file" > tmpfile && mv tmpfile "$pom_file"
  echo "Se ha actualizado la propiedad 'revision' a '$nuevo_revision' en '$pom_file'."
else
  echo "El archivo '$pom_file' no existe."
fi

git add back-end/pom-parent.xml
echo "databaseChangeLog:
    - logicalFilePath: incrementales-v${M_VERSION}_$(($MIN_VERSION+1))
" >> back-end/app/src/main/resources/db/changelog/incrementales-v${M_VERSION}_$(($MIN_VERSION+1)).yml

awk -v M_VERSION=${M_VERSION} -v MIN_VERSION=${MIN_VERSION} '
    /- includeAll:/ {
        print "- include:\n    file: incrementales-v" M_VERSION "_" MIN_VERSION+1 ".yml\n    relativeToChangelogFile: true\n"
    }
    { print }
' back-end/app/src/main/resources/db/changelog/db.changelog-master.yaml > temp && mv temp back-end/app/src/main/resources/db/changelog/db.changelog-master.yaml

git add back-end/app/src/main/resources/db/
git commit -m "Comienza desarrollo de $NEXT_MM_VERSION"
git push ${REMOTE} code-freeze -o merge_request.create -o merge_request.merge_when_pipeline_succeeds -o merge_request.remove_source_branch -o merge_request.title="Code Freeze  V${CURR_VERSION}"
echo "git push code freeze"
echo "
### 4. Generar branch de regresion (sin el último commit)
"
git checkout -b "rc-$MM_VERSION" @~1
# Cambiar POM a version "0.13.0-RELEASECANDIDATE"
mvn -B -f=back-end/pom-parent.xml versions:set-property \
    -Dproperty="revision" \
    -DnewVersion="$MM_VERSION.0-RELEASECANDIDATE" \
    -DgenerateBackupPoms=false
git add back-end/pom-parent.xml
git commit -m "Comienza regresion $MM_VERSION"
git push ${REMOTE} "rc-$MM_VERSION"
echo "git push rc-$MM_VERSION "
git checkout master

### Proyecto ambientes ###

cd "$PROJECT_DIR_AMBIENTES"
REMOTE="origin"

echo "
###### Proyecto de ambientes ######
###1. Pasarte a stage
"
git checkout stage
git fetch ${REMOTE} stage
git reset --hard ${REMOTE}/stage
git checkout -b "rc-$MM_VERSION_AMBIENTES_ANTERIOR"
git push ${REMOTE} "rc-$MM_VERSION_AMBIENTES_ANTERIOR"
git checkout stage

# Define el nuevo valor para 'tag'
nuevo_tag="rc-$MM_VERSION_AMBIENTES"

# Ruta al archivo 'values.yaml'
archivo="apps/hsi/values.yaml"

# Verifica si el archivo existe
if [ -f "$archivo" ]; then
  # Utiliza 'awk' para reemplazar el valor de 'tag' y formatearlo correctamente
  awk -v new_tag="$nuevo_tag" '/^  tag:/ { print "  tag:", new_tag; next } 1' "$archivo" > tmpfile && mv tmpfile "$archivo"
  echo "Se ha actualizado el valor de 'tag' a '$nuevo_tag' en '$archivo'."
else
  echo "El archivo '$archivo' no existe."
fi

git add .
git commit -m "Actualiza version stage"
git push ${REMOTE} stage

echo "Ahora debes esperar que deploye ambiente con version anterior y stage."
echo "-----------------------------------------------------------------"
echo "Avisar por Slack: "
echo "  @here Terminado el CF. Se puede empezar a incluir funcionalidad en master, y la regresion en stage. Tambien se creo ambiente con release anterior. hsi.stage-minsal.dev-env.lamansys.ar hsi.rc-$MM_VERSION_AMBIENTES_ANTERIOR-minsal.dev-env.lamansys.ar"
