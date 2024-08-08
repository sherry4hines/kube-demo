#!/bin/bash
#------------------------------------------
# Install Camunda Kind Cluster locally
#------------------------------------------
# Remove cluster if it already exists
echo "Removing existing camunda-platform-local cluster..." 
kind delete cluster --name camunda-platform-local

# Create kind cluster 
echo "Creating camunda-platform-local cluster..." 
kind create cluster --name camunda-platform-local --config=cluster.yaml
if [[ $? -ne 0 ]]
then 
   echo "Error occured creating kind cluster ..."
   exit 1 
fi 

# Install Nginx Controller 
echo "Installing Nginx Ingress controller support..." 
kubectl config use-context kind-camunda-platform-local
kubectl apply -f https://raw.githubusercontent.com/kubernetes/ingress-nginx/main/deploy/static/provider/kind/deploy.yaml
#kubectl wait --namespace ingress-nginx \
#  --for=condition=Ready pod \
#  --selector=app.kubernetes.io/component=controller \
#  --timeout=90s
#if [[ $? -ne 0 ]]
#then 
#   echo "Error occured installing NginX Ingress controller ..."
#   exit 1 
#fi

# Deploy Postgresql Instance to support demo application
echo "Deploying postgresql instance for demo application..."
kubectl apply -f postgres
kubectl wait pods -n default \
  --for condition=Ready pod -l app=postgres \
  --timeout=90s
#if [[ $? -ne 0 ]]
#then 
#   echo "Error occured installing Demo postgresql instance ..."
#   exit 1 
#fi

# helm repo add bitnami https://charts.bitnami.com/bitnami
# Install Kafka with zookeeper
echo "Deploying Kafka with Zookeeper..."
helm install zookeeper bitnami/zookeeper \
   -f ./kafka/zookeeper-values.yaml  --wait \
   1>./logs/zookeeper.log 2>./logs/zookeeper.err

helm install kafka bitnami/kafka \
   -f ./kafka/kafka-values.yaml --wait \
   1>./logs/kafka.log 2>./logs/kafka.err

# Install Camunda 8.5 platform 
echo "Deploying Camunda 8.5 Platform...."
helm install camunda-platform camunda/camunda-platform \
    -f ./camunda-values.yaml --wait \
    1>./logs/camunda.log 2>./logs/camunda.err


#TODO
#services = ["camunda8-svc", "fda-demo-ui"]
#kubectl apply camunda8-svc
#kubectl apply demo-ui
