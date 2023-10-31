#!/bin/bash
set -o errexit

archivo_xml="back-end/pom-parent.xml"
usuario="$CF_USER" # variables de gitlab CI/CD
token="$CF_TOKEN"

git config --global user.email "telescolucas19@gmail.com"
git config --global user.name "lgtelesco"

# seteo usuario
git remote set-url origin "https://$usuario:$token@gitlab.lamansys.ar/minsal/hsi.git"

# modifica el pom-parent
# Verifica si el archivo XML existe
if [ -e "$archivo_xml" ]; then
  revision=$(grep -o "<revision>[^<]*" "$archivo_xml" | sed "s/<revision>//")

  if [[ $revision == *"RELEASECANDIDATE"* ]]; then
    nueva_revision=$(echo "$revision" | sed "s/-RELEASECANDIDATE//")
  else
    IFS='.' read -ra partes <<< "$revision"
    ultimo_parte=${partes[${#partes[@]}-1]}

    # Incrementa el último valor en 1
    nuevo_ultimo_parte=$((ultimo_parte + 1))
    partes[${#partes[@]}-1]=$nuevo_ultimo_parte
    nueva_revision=$(IFS='.'; echo "${partes[*]}" | sed 's/[^0-9.]//g')
  fi

  # Actualiza la etiqueta <revision> en el archivo XML
  sed -i.bak "s|<revision>[^<]*|<revision>$nueva_revision|g" "$archivo_xml"
  new_tag=v$nueva_revision
  echo "Revisión actualizada a: $nueva_revision"
  echo "Nuevo tag: $new_tag"
else
  echo "El archivo XML no existe."
  exit 1
fi

# Comitea agregando modificaciones del pom-parent
git checkout -b ${CI_COMMIT_REF_NAME}
git add back-end/pom-parent.xml
git commit -m "Release $new_tag"
git push origin ${CI_COMMIT_REF_NAME}
# crea Tag
git tag ${new_tag}
git push origin ${new_tag}

echo "Tag generado: $new_tag"
echo "-----------------------------------------------------------------"
echo "En Repository > Tags, va a ver el nuevo tag $new_tag. Accede al pipeline que está en ejecución, espera a que los Jobs de Build finalicen y publica la imagen requerida en Docker Hub. "
echo "Si correspone, actualiza el ambiente externo seleccionando el branch $CI_COMMIT_REF_NAME (En el repo de ambientes)."
echo "Avisar por Slack: "
echo "  @here Entrega terminada. Imagen de la versión $new_tag publicada. Ambiente externo actualizado."
