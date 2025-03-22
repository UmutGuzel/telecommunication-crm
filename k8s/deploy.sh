#!/bin/bash

# Exit on error
set -e

echo "Waiting for namespace to be fully created..."
sleep 2

echo "Deploying infrastructure components..."
kubectl apply -f infrastructure/kafka.yaml
kubectl apply -f infrastructure/papercut-smtp.yaml
kubectl apply -f infrastructure/prometheus.yaml
kubectl apply -f infrastructure/grafana.yaml
kubectl apply -f infrastructure/zipkin.yaml
kubectl apply -f infrastructure/alertmanager.yaml

echo "Waiting for infrastructure to be available..."
sleep 5

echo "Deploying databases..."
kubectl apply -f databases/postgres-configmap.yaml
kubectl apply -f databases/user-service-db.yaml
kubectl apply -f databases/plan-service-db.yaml
kubectl apply -f databases/payment-service-db.yaml
kubectl apply -f databases/customer-support-service-db.yaml
kubectl apply -f databases/contract-service-db.yaml

echo "Waiting for databases to be available..."
sleep 10

echo "Deploying configuration servers..."
kubectl apply -f config/config-server.yaml
echo "Waiting for config server to be available..."
sleep 30

kubectl apply -f config/discovery-server.yaml
echo "Waiting for discovery server to be available..."
sleep 30

echo "Deploying microservices..."
kubectl apply -f services/user-service.yaml
kubectl apply -f services/customer-service.yaml
kubectl apply -f services/plan-service.yaml
kubectl apply -f services/payment-service.yaml
kubectl apply -f services/notification-service.yaml
kubectl apply -f services/customer-support-service.yaml
kubectl apply -f services/contract-service.yaml
kubectl apply -f services/analytics-service.yaml
kubectl apply -f services/gateway-service.yaml

echo "Deployment completed successfully!"
echo "To check the status of the pods, run: kubectl get pods -n telecom-crm" 