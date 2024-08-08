#!/bin/bash
#----------------------------------------------------------- 
nohup kubectl port-forward svc/camunda-platform-zeebe-gateway 26500:26500 -n default 1>./logs/zeebe.log 2>./logs/zeebe.err &
nohup kubectl port-forward svc/camunda-platform-zeebe-gateway 8088:8080 -n default 1>./logs/zeebe-gw.log 2>./logs/zeebe-gw.err & 
nohup kubectl port-forward svc/camunda-platform-identity 8080:80 1>./logs/identity.log 2>./logs/identity.err &
nohup kubectl port-forward svc/camunda-platform-operate  8081:80 1>./logs/operate.log 2>./logs/operate.err &
nohup kubectl port-forward svc/camunda-platform-tasklist  8082:80 1>./logs/tasklist.log 2>./logs/tasklist.err &
nohup kubectl port-forward svc/camunda-platform-keycloak 18080:80  1>./logs/keycloak.log 2>./logs/keycloak.err &
nohup kubectl port-forward svc/postgres-service  5433:5432 1>./logs/app-postgres.log 2>./logs/app-postgres.err &
nohup kubectl port-forward --namespace default svc/zookeeper 2181:2181 1>./logs/zookeeper.log 2>./logs/zookeeper.err &
