FROM openjdk:11-jre-slim

LABEL org.opencontainers.image.authors="historia_clinica@pladema.exa.unicen.edu.ar"

WORKDIR /app
COPY .docker/app.jar /app/app.jar

ENV SERVER_PORT=80
EXPOSE 80

ENTRYPOINT ["java","-XX:+UseSerialGC","-verbose:gc","-jar","/app/app.jar"]
