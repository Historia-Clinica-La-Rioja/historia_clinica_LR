#!/bin/sh
set -o errexit

PROJECT_DIR=$(dirname "$0")"/.."
cd "$PROJECT_DIR"

REMOTE=${1:-"origin"}

echo "
###1. Se mergeará el commit actual en branch \"produccion\". Asegurate estar en el commit correcto
"
git log --oneline -n 1
echo "Si los datos estan mal cancele ahora con Ctrl+C "
printf "Press [ENTER] to continue"
read _

git branch -D produccion || true #si existe borra el branch
git branch produccion ${REMOTE}/produccion
git checkout @ #se para en el mismo commit desacoplado del branch para el proximo merge
git merge produccion -s ours --no-edit # descarta cualquier cambio en producción
git checkout produccion
git merge - --no-edit #dado que hicimos un merge antes esto es Fast foward y no tiene conflictos
git push ${REMOTE} produccion
echo "Ahora debes esperar el build y hacer el deploy en los ambientes"

printf "Press [ENTER] to continue"
read _

echo "-----------------------------------------------------------------"
echo "Avisar por Slack: "
echo "  @channel Deploy terminado."
printf "Press [ENTER] to continue"
read _
