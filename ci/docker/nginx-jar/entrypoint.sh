#!/bin/sh
set -e

nginx
if [ ! -z "$GLOWROOT_AGENT_ID" ]; then
    echo "agent.id=$GLOWROOT_PROJECT::$GLOWROOT_AGENT_ID" > /glowroot/glowroot.properties
    echo "collector.address=http://10.125.125.79:8181" >> /glowroot/glowroot.properties
fi
exec "$@"
