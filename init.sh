#!/bin/bash

SCRIPT_BASE_DIR=$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )
PROJECT_NAME="fis-hystrix"

echo
echo "Creating new project ${PROJECT_NAME}..."
echo
oc new-project "${PROJECT_NAME}"

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