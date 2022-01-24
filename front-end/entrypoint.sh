#!/bin/sh

envsubst '${API_IP_PORT}' < /nginx-template.conf > /etc/nginx/conf.d/default.conf

cat /etc/nginx/conf.d/default.conf

exec "$@"
