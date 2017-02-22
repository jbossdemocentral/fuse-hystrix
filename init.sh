#!/bin/bash

SCRIPT_BASE_DIR=$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )
PROJECT_NAME="fis-hystrix"

oc whoami >/dev/null 2>&1 || { echo "Cannot validate connectivity to OpenShift. Ensure oc client tool installed and logged in" ; exit 1; }

echo
echo "Creating new project ${PROJECT_NAME}..."
echo
oc new-project "${PROJECT_NAME}" --description="Fuse Integration Services Hystrix Demo" --display-name="FIS Hystrix" >/dev/null 2>&1

echo
echo "Deploying Kubeflix..."
echo
oc process -f "${SCRIPT_BASE_DIR}/support/templates/kubeflix.json" | oc create -f-

echo
echo "Deploying Gateway Application..."
echo
oc process -f "${SCRIPT_BASE_DIR}/support/templates/gateway.json" | oc create -f-

echo
echo "Deploying Definition Application..."
echo
oc process -f "${SCRIPT_BASE_DIR}/support/templates/definition.json" | oc create -f-
