# Telecommunication CRM Kubernetes Configuration

This directory contains Kubernetes configuration files for deploying the Telecommunication CRM microservices architecture.

## Directory Structure

- **config/** - Configuration servers (Config Server, Discovery Server)
- **databases/** - Database services for each microservice
- **infrastructure/** - Infrastructure components (Kafka, Prometheus, Grafana, etc.)
- **services/** - Application microservices

## Deployment Order

For proper deployment, services should be deployed in the following order:

1. Create namespace: `kubectl apply -f namespace.yaml`
2. Deploy infrastructure components: `kubectl apply -f infrastructure/`
3. Deploy databases: `kubectl apply -f databases/`
4. Deploy configuration servers: `kubectl apply -f config/`
5. Deploy application services: `kubectl apply -f services/`

## Port Mappings

The following services are exposed:

| Service | Internal Port | External Port |
|---------|--------------|--------------|
| user-service-db | 5432 | 5434 |
| plan-service-db | 5432 | 5433 |
| payment-service-db | 5432 | 5432 |
| customer-support-service-db | 5432 | 5435 |
| contract-service-db | 5432 | 5436 |
| kafka | 9092, 9093, 9094 | 9092 |
| papercut-smtp | 25, 37408 | 25, 37408 |
| config-server | 8090 | 8090 |
| discovery-server | 8888 | 8888 |
| user-service | 9010 | 9010 |
| customer-service | 9020 | 9020 |
| contract-service | 9030 | 9030 |
| plan-service | 9040 | 9040 |
| payment-service | 9050 | 9050 |
| customer-support-service | 9060 | 9060 |
| analytics-service | 9070 | 9070 |
| notification-service | 9080 | 9080 |
| gateway-service | 8080 | 8080 |
| prometheus | 9090 | 9090 |
| alertmanager | 9093 | 9093 |
| grafana | 3000 | 3000 |
| zipkin | 9411 | 9411 | 