#!/bin/sh
cd ..
cd dba
mvn -Dliquibase.propertyFile=../sgh-ansible/liquibase.properties "$@" liquibase:update
