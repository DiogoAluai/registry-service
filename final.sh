#!/bin/bash

# Compile and deploy to fly.io
# Relies on environment variable for app name: $COOL_REGISTRY_FLY_NAME, which is sourced from app_name.src file

source app_name.src || exit 1

mvn -DskipTests clean package # you can download from releases instead, and stick it inside target/ directory
fly deploy -a "$COOL_REGISTRY_FLY_NAME"                  # deploy app, it also creates the needed volume
flyctl scale count 1 -y --app "$COOL_REGISTRY_FLY_NAME"  # make sure only one machine is used
