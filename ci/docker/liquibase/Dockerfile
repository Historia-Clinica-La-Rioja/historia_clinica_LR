FROM maven:3-jdk-8

COPY liquibase/pom.xml /dba/liquibase/
COPY pom-parent.xml /dba/

# copy 
WORKDIR /dba/liquibase/
COPY liquibase/* ./
