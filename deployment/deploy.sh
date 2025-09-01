#!/bin/bash

# Payment Showcase Kubernetes Deployment Script
# This script deploys the entire payment showcase application to Kubernetes

set -e

echo "🚀 Starting Payment Showcase Kubernetes Deployment..."

# Function to check if kubectl is available
check_kubectl() {
    if ! command -v kubectl &> /dev/null; then
        echo "❌ kubectl is not installed or not in PATH"
        exit 1
    fi
    echo "✅ kubectl is available"
}

# Function to check if cluster is accessible
check_cluster() {
    if ! kubectl cluster-info &> /dev/null; then
        echo "❌ Cannot connect to Kubernetes cluster"
        exit 1
    fi
    echo "✅ Kubernetes cluster is accessible"
}

# Function to create namespace
create_namespace() {
    echo "📁 Creating namespace..."
    kubectl apply -f namespace.yaml
    echo "✅ Namespace created"
}

# Function to create secrets (manual step required)
create_secrets() {
    echo "🔐 Creating secrets..."
    echo "⚠️  IMPORTANT: Secrets are not stored in version control for security!"
    echo ""
    echo "📋 Please create the GitHub Container Registry secret using one of these methods:"
    echo ""
    echo "Method 1 (Recommended):"
    echo "kubectl create secret docker-registry ghcr-secret \\"
    echo "  --docker-server=ghcr.io \\"
    echo "  --docker-username=your-github-username \\"
    echo "  --docker-password=your-github-token \\"
    echo "  --namespace=payment-showcase"
    echo ""
    echo "Method 2 (If already logged in to ghcr.io):"
    echo "kubectl create secret generic ghcr-secret \\"
    echo "  --from-file=.dockerconfigjson=\$HOME/.docker/config.json \\"
    echo "  --type=kubernetes.io/dockerconfigjson \\"
    echo "  --namespace=payment-showcase"
    echo ""
    echo "📖 See secrets.template for more details"
    echo ""
    read -p "Press Enter after creating the secret..."
}

# Function to deploy LocalStack
deploy_localstack() {
    echo "🗄️  Deploying LocalStack (DynamoDB)..."
    kubectl apply -f localstack.yaml
    echo "✅ LocalStack deployed"
}

# Function to deploy services
deploy_services() {
    echo "🏦 Deploying Bank Account API..."
    kubectl apply -f bankaccount/deployment.yaml -n payment-showcase
    kubectl apply -f bankaccount/service.yaml -n payment-showcase
    
    echo "💳 Deploying Payment API..."
    kubectl apply -f payment-api/configmap.yaml -n payment-showcase
    kubectl apply -f payment-api/deployment.yaml -n payment-showcase
    kubectl apply -f payment-api/service.yaml -n payment-showcase
    
    echo "🌐 Deploying Payment BFF..."
    kubectl apply -f payment-bff/configmap.yaml -n payment-showcase
    kubectl apply -f payment-bff/deployment.yaml -n payment-showcase
    kubectl apply -f payment-bff/service.yaml -n payment-showcase
    kubectl apply -f payment-bff/ingress.yaml -n payment-showcase
    
    echo "✅ All services deployed"
}

# Function to wait for deployments
wait_for_deployments() {
    echo "⏳ Waiting for deployments to be ready..."
    
    kubectl wait --for=condition=available --timeout=300s deployment/localstack -n payment-showcase
    kubectl wait --for=condition=available --timeout=300s deployment/bankaccount-api -n payment-showcase
    kubectl wait --for=condition=available --timeout=300s deployment/payment-api -n payment-showcase
    kubectl wait --for=condition=available --timeout=300s deployment/payment-bff -n payment-showcase
    
    echo "✅ All deployments are ready"
}

# Function to show status
show_status() {
    echo "📊 Deployment Status:"
    kubectl get pods -n payment-showcase
    echo ""
    kubectl get services -n payment-showcase
    echo ""
    echo "🌐 Access the application:"
    echo "   - Payment BFF: http://payment.local (if using ingress)"
    echo "   - Or port-forward: kubectl port-forward service/payment-bff-service 8080:8080 -n payment-showcase"
}

# Main execution
main() {
    check_kubectl
    check_cluster
    create_namespace
    create_secrets
    deploy_localstack
    deploy_services
    wait_for_deployments
    show_status
    
    echo "🎉 Payment Showcase deployment completed successfully!"
}

# Run main function
main "$@"
