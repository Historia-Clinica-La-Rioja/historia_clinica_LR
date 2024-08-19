#!/bin/sh
set -o errexit

BASEDIR=$(dirname "$0")
cd "$BASEDIR"

echo "Checking api-model 🕵️‍♂️"

if git diff --exit-code ../front-end/apps/projects/hospital/src/app/modules/api-rest/api-model.d.ts; then echo "✅ El archivo api-model.d.ts no ha sido modificado"; else echo "🚨 El archivo api-model.d.ts ha sido modificado"; exit 1; fi

# PRESUPUESTO
DATE_IN_DTO_BUDGET=74

DATE_IN_DTO_COUNT=$(grep -or ": Date;"  ../front-end/apps/projects/hospital/src/app/modules/api-rest/api-model.d.ts | wc -l)

if [ "$DATE_IN_DTO_COUNT" -gt "$DATE_IN_DTO_BUDGET" ]; then
  echo "🚨 Se supero con $DATE_IN_DTO_COUNT el presupuesto de $DATE_IN_DTO_BUDGET usos de 'Date' en la API, considere usar DateDto, TimeDto o DateTimeDto"
  exit 1
fi

echo ":::: Chequeos OK ::::"