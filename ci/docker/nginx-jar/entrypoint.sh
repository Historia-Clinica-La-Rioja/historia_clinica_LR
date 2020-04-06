#!/bin/sh
set -e

nginx
if [ ! -z "$GLOWROOT_AGENT_ID" ]; then
    echo "agent.id=$GLOWROOT_PROJECT::$GLOWROOT_AGENT_ID" > /glowroot/glowroot.properties
    echo "collector.address=http://192.168.7.79:8181" >> /glowroot/glowroot.properties
fi
exec "$@"
