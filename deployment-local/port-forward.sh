#!/bin/bash
#----------------------------------------------------------- 
nohup kubectl port-forward svc/camunda-platform-zeebe-gateway 26500:26500 -n default 1>zeebe.log 2>zeebe.err &
nohup kubectl port-forward svc/camunda-platform-zeebe-gateway 8088:8080 -n default 1>zeebe-gw.log 2>zeebe-gw.err & 
nohup kubectl port-forward svc/camunda-platform-operate  8081:80 1>operate.log 2>operate.err &
nohup kubectl port-forward svc/camunda-platform-tasklist  8082:80 1>tasklist.log 2>tasklist.err &
nohup kubectl port-forward svc/postgres-service  5432:5432 1>postgres.log 2>postgres.err &
nohup kubectl port-forward svc/keycloak-service  8080:8080 1>keycloak.log 2>keycloak.err &

