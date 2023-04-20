FROM openjdk:11-jre

LABEL org.opencontainers.image.authors="historia_clinica@pladema.exa.unicen.edu.ar"

RUN apt-get update && apt-get install -y nginx
COPY nginx.conf /etc/nginx/sites-enabled/default 

WORKDIR /app

COPY app.jar        /app/app.jar
COPY front-end     /app/front-end/
COPY backoffice    /app/backoffice/
COPY docker.properties /app/env.properties

EXPOSE 8280
EXPOSE 8080
ENTRYPOINT [ "/entrypoint.sh" ]

CMD ["java","-jar","/app/app.jar","--spring.config.additional-location=file:./env.properties"]   