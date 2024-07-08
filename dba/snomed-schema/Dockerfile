# Usa la imagen oficial de PostgreSQL como base
FROM postgres:16

# Establece el directorio de trabajo
WORKDIR /usr/src/app

# Copia los archivos SQL al contenedor
COPY create-schema.sql ./
COPY concept_term.sql ./
COPY views.sql ./

# Copia archivos de Release
# COPY Release112023 ./

# Copia el script de entrada al contenedor
COPY entrypoint.sh /usr/local/bin/

# Da permisos de ejecución al script de entrada
RUN chmod +x /usr/local/bin/entrypoint.sh

# Establece el script de entrada como el comando por defecto
ENTRYPOINT ["entrypoint.sh"]
