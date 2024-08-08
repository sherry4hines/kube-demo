#!/bin/bash
#------------------------------------------
# Install Camunda Kind Cluster locally
#------------------------------------------
kind create cluster --name camunda-platform-local --config=cluster.yaml
kubectl config use-context kind-camunda-platform-local
kubectl apply -f https://raw.githubusercontent.com/kubernetes/ingress-nginx/main/deploy/static/provider/kind/deploy.yaml
kubectl wait --namespace ingress-nginx \
  --for=condition=ready pod \
  --selector=app.kubernetes.io/component=controller \
  --timeout=90s

kubectl apply -f postgres

# helm repo add bitnami https://charts.bitnami.com/bitnami
helm install zookeeper bitnami/zookeeper -f ./kafka/zookeeper-values.yaml
helm install kafka bitnami/kafka -f ./kafka/kafka-values.yaml

helm install camunda-platform camunda/camunda-platform \
    -f ./camunda-values.yaml 1>./logs/camunda.log 2>./logs/camunda.err

#TODO
#kubectl apply camunda8-svc
#kubectl apply demo-ui
