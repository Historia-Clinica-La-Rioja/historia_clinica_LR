#!/bin/sh
set -e

nginx
if [ ! -z "$GLOWROOT_AGENT_ID" ]; then
    echo "agent.id=$GLOWROOT_PROJECT::$GLOWROOT_AGENT_ID" > /glowroot/glowroot.properties
    echo "collector.address=$GLOWROOT_COLLECTOR_ADDRESS" >> /glowroot/glowroot.properties
fi
exec "$@"
