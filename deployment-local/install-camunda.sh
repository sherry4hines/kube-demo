#!/bin/bash
#------------------------------------------
# Install Camunda Kind Cluster locally
#------------------------------------------
kind create cluster --name camunda-platform-local
kubectl config use-context kind-camunda-platform-local
kubectl apply -f postgres
kubectl apply -f keycloak
helm install camunda-platform camunda/camunda-platform \
    -f ./camunda-values.yaml
