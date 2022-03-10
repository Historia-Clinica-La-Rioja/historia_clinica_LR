FROM adoptopenjdk/openjdk11:debian
RUN mkdir /opt/app
COPY app/target/app*.jar /opt/app/app.jar
CMD ["java", "-jar", "/opt/app/app.jar"]
