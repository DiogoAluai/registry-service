#!/bin/bash

# Compile and deploy to fly.io
# Relies on environment variable for app name: $COOL_REGISTRY_FLY_NAME

mvn -DskipTests clean install
fly deploy -a "$COOL_REGISTRY_FLY_NAME"
flyctl scale count 1 -y --app "$COOL_REGISTRY_FLY_NAME"
