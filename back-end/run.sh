#!/bin/sh
# Ejecuta el build del backend pasandole las propiedades personales

java -jar hospital-api/target/hospital*.jar --spring.config.additional-location=file:./personal.properties
