# Kubernetes Deployment Guide

This directory contains Kubernetes deployment manifests for the Payment Showcase application.

## 🏗️ Architecture

The application consists of the following components:

- **payment-bff** (Port 8080) - Backend for Frontend service
- **payment-api** (Port 8282) - Core payment processing service  
- **bankaccount-api** (Port 8181) - Bank account management service
- **localstack** (Port 4566) - Local DynamoDB for development

## 📁 Directory Structure

```
deployment/
├── deploy.sh                    # Automated deployment script
├── namespace.yaml              # Namespace definition
├── secrets.template           # Template for creating secrets (safe to commit)
├── .gitignore                 # Prevents accidental commit of secrets
├── localstack.yaml            # LocalStack DynamoDB
├── kustomization.yaml         # Kustomize configuration
├── bankaccount/
│   ├── deployment.yaml        # Bank Account API deployment
│   └── service.yaml          # Bank Account API service
├── payment-api/
│   ├── deployment.yaml        # Payment API deployment
│   ├── service.yaml          # Payment API service
│   └── configmap.yaml         # Payment API configuration
└── payment-bff/
    ├── deployment.yaml        # Payment BFF deployment
    ├── service.yaml          # Payment BFF service
    ├── ingress.yaml          # Payment BFF ingress
    └── configmap.yaml         # Payment BFF configuration
```

## 🚀 Quick Start

### Prerequisites

1. **Kubernetes cluster** (minikube, kind, or cloud provider)
2. **kubectl** configured and connected to your cluster
3. **Docker images** built and pushed to GitHub Container Registry

### 1. Build and Push Docker Images

```bash
# Build images for each service
cd bankaccount-api && docker build -t ghcr.io/lonecalvary78/bankaccount-api:latest .
cd ../payment-api && docker build -t ghcr.io/lonecalvary78/payment-api:latest .
cd ../payment-bff && docker build -t ghcr.io/lonecalvary78/payment-bff:latest .

# Push to registry
docker push ghcr.io/lonecalvary78/bankaccount-api:latest
docker push ghcr.io/lonecalvary78/payment-api:latest
docker push ghcr.io/lonecalvary78/payment-bff:latest
```

### 2. Create GitHub Container Registry Secret

**⚠️ Important: Never commit secrets to version control!**

Use the `secrets.template` file as a reference and create the secret using one of these methods:

**Method 1: Direct kubectl command (Recommended)**
```bash
kubectl create secret docker-registry ghcr-secret \
  --docker-server=ghcr.io \
  --docker-username=your-github-username \
  --docker-password=your-github-token \
  --namespace=payment-showcase
```

**Method 2: From existing Docker credentials**
```bash
# If you're already logged in to ghcr.io
kubectl create secret generic ghcr-secret \
  --from-file=.dockerconfigjson=$HOME/.docker/config.json \
  --type=kubernetes.io/dockerconfigjson \
  --namespace=payment-showcase
```

### 3. Deploy Using Script

```bash
cd deployment
./deploy.sh
```

### 4. Manual Deployment (Alternative)

```bash
# Create namespace
kubectl apply -f namespace.yaml

# Deploy LocalStack
kubectl apply -f localstack.yaml

# Deploy services with separate files
kubectl apply -f bankaccount/deployment.yaml -n payment-showcase
kubectl apply -f bankaccount/service.yaml -n payment-showcase
kubectl apply -f payment-api/configmap.yaml -n payment-showcase
kubectl apply -f payment-api/deployment.yaml -n payment-showcase
kubectl apply -f payment-api/service.yaml -n payment-showcase
kubectl apply -f payment-bff/configmap.yaml -n payment-showcase
kubectl apply -f payment-bff/deployment.yaml -n payment-showcase
kubectl apply -f payment-bff/service.yaml -n payment-showcase
kubectl apply -f payment-bff/ingress.yaml -n payment-showcase
```

## 🔍 Verification

### Check Pod Status
```bash
kubectl get pods -n payment-showcase
```

### Check Services
```bash
kubectl get services -n payment-showcase
```

### View Logs
```bash
kubectl logs -f deployment/payment-bff -n payment-showcase
kubectl logs -f deployment/payment-api -n payment-showcase
kubectl logs -f deployment/bankaccount-api -n payment-showcase
```

## 🌐 Accessing the Application

### Option 1: Port Forwarding
```bash
# Access Payment BFF
kubectl port-forward service/payment-bff-service 8080:8080 -n payment-showcase

# Access Payment API directly
kubectl port-forward service/payment-api-service 8282:8282 -n payment-showcase

# Access Bank Account API directly  
kubectl port-forward service/bankaccount-api-service 8181:8181 -n payment-showcase
```

### Option 2: Ingress (if enabled)
If you have an ingress controller installed:
```bash
# Add to /etc/hosts
echo "127.0.0.1 payment.local" >> /etc/hosts

# Access at http://payment.local
```

### Option 3: LoadBalancer (cloud environments)
```bash
# Get external IP
kubectl get service payment-bff-service -n payment-showcase
```

## 🔧 Configuration

### Environment Variables

**Payment API:**
- `AWS_REGION`: AWS region for DynamoDB
- `DB_MODE`: Database mode (localstack for development)
- `DB_LOCALSTACK_ENDPOINT`: LocalStack endpoint
- `DB_LOCALSTACK_REGION`: LocalStack region

**Payment BFF:**
- `INTEGRATION_BANKACCOUNT_ENDPOINT`: Bank Account API endpoint
- `INTEGRATION_PAYMENT_ENDPOINT`: Payment API endpoint

### Health Checks

All services expose health check endpoints:
- **Liveness**: `/observe/health/live`
- **Readiness**: `/observe/health/ready`

## 📊 Resource Requirements

| Service | CPU Request | Memory Request | CPU Limit | Memory Limit |
|---------|-------------|----------------|-----------|--------------|
| payment-bff | 250m | 256Mi | 500m | 512Mi |
| payment-api | 500m | 512Mi | 1000m | 1Gi |
| bankaccount-api | 250m | 256Mi | 500m | 512Mi |
| localstack | 250m | 512Mi | 500m | 1Gi |

## 🗑️ Cleanup

```bash
# Delete all resources
kubectl delete namespace payment-showcase

# Or delete individual components
kubectl delete -f payment-bff/deployment.yaml -n payment-showcase
kubectl delete -f payment-bff/service.yaml -n payment-showcase
kubectl delete -f payment-bff/ingress.yaml -n payment-showcase
kubectl delete -f payment-api/deployment.yaml -n payment-showcase
kubectl delete -f payment-api/service.yaml -n payment-showcase
kubectl delete -f bankaccount/deployment.yaml -n payment-showcase
kubectl delete -f bankaccount/service.yaml -n payment-showcase
kubectl delete -f localstack.yaml
```

## 🔧 Troubleshooting

### Common Issues

1. **ImagePullBackOff errors**
   - Ensure Docker images are built and pushed
   - Verify GitHub Container Registry credentials

2. **Service discovery issues**
   - Check service names match configuration
   - Verify all services are in the same namespace

3. **Health check failures**
   - Check application logs
   - Verify health endpoints are accessible

### Debug Commands

```bash
# Check pod events
kubectl describe pod <pod-name> -n payment-showcase

# Check service endpoints
kubectl get endpoints -n payment-showcase

# Port forward for debugging
kubectl port-forward pod/<pod-name> 8080:8080 -n payment-showcase
```

## 🚀 Production Considerations

For production deployments, consider:

1. **Resource limits and requests** - Adjust based on load testing
2. **Horizontal Pod Autoscaler** - Scale based on CPU/memory usage  
3. **Persistent volumes** - For LocalStack data persistence
4. **TLS/SSL termination** - Use cert-manager for HTTPS
5. **Network policies** - Restrict pod-to-pod communication
6. **Monitoring** - Deploy Prometheus and Grafana
7. **Logging** - Centralized logging with ELK or similar stack

## 🔒 Security Best Practices

### Secrets Management
- **Never commit secrets to version control!**
- Use tools like:
  - **Kubernetes Secrets** (basic, included in this setup)
  - **HashiCorp Vault** (recommended for production)
  - **Azure Key Vault** / **AWS Secrets Manager** (cloud-specific)
  - **Sealed Secrets** (GitOps-friendly)

### Image Security
- Scan container images for vulnerabilities
- Use specific image tags instead of `latest` in production
- Implement image signing and verification
- Use private registries for sensitive applications

### Network Security
- Implement NetworkPolicies to restrict pod-to-pod communication
- Use service mesh (Istio, Linkerd) for advanced traffic management
- Enable TLS for all inter-service communication
