FROM openjdk:11-jre-slim

LABEL org.opencontainers.image.authors="desarrollo@lamansys.com.ar"

WORKDIR /app
COPY app/target/app*.jar /app/app.jar

ENTRYPOINT ["java","-jar","/app/app.jar"]
