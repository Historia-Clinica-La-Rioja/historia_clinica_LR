#!/bin/bash
set -e

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
    CREATE USER user_svc WITH ENCRYPTED PASSWORD 'user_svcp0o9';
    CREATE DATABASE user_svc;
    \connect user_svc;
    CREATE SCHEMA user_svc;
    GRANT ALL PRIVILEGES ON DATABASE user_svc TO user_svc;
    \connect postgres;
    CREATE USER sge WITH ENCRYPTED PASSWORD 'sge';
    CREATE DATABASE hospitalDB;
    \connect hospitalDB;
    CREATE SCHEMA hospitalDB;
    GRANT ALL PRIVILEGES ON DATABASE hospitalDB TO hospitalDB;
EOSQL
