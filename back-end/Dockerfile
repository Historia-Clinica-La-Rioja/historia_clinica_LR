FROM openjdk:11-jre-slim

LABEL org.opencontainers.image.authors="desarrollo@lamansys.com.ar"

WORKDIR /app
COPY app/target/app*.jar /app/app.jar

ENV SERVER_PORT=80
EXPOSE 80

ENTRYPOINT ["java","-jar","/app/app.jar"]
