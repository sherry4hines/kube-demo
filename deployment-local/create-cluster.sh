#!/bin/bash
#-----------------------------------------------
# ./create-cluster.sh 
#-----------------------------------------------

#- Identify environ to be created
environ=local

#-- ensure that the required resources file was updated
if [[ -s ../current-config/required-resources.json ]]
then
  echo 'Required resources file found...'
else 
  echo 'Please copy required-resources.json to ../current-config/required-resources.json'
  echo 'and edit all required references with values for this project, then re-run this script'
  exit 1 
fi

#-- parse project options 
project=$(jq -r .project ../current-config/required-resources.json)

echo "Creating local kind cluster..."
kind create cluster --config=cluster.yaml --name ${project}

echo "Deploying ingress-nginx" 
kubectl apply -f https://raw.githubusercontent.com/kubernetes/ingress-nginx/main/deploy/static/provider/kind/deploy.yaml
kubectl wait --namespace ingress-nginx \
  --for=condition=ready pod \
  --selector=app.kubernetes.io/component=controller \
  --timeout=120s

kubectl apply -f ./ingress.yaml
