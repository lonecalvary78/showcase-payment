# Payment Showcase

[![Test and Build Image](https://github.com/lonecalvary78/showcase-payment/actions/workflows/test-and-build-image.yaml/badge.svg)](https://github.com/lonecalvary78/showcase-payment/actions/workflows/test-and-build-image.yaml)

## Table of Contents
- [Introduction](#introduction)
- [Features](#features)
- [Application Architecture](#application-architecture)
  - [Overview](#overview)
  - [Architecture Diagram](#architecture-diagram)
  - [Service Components](#service-components)
  - [Security Architecture](#security-architecture)
  - [Data Flow](#data-flow)
  - [Technology Stack](#technology-stack)
  - [Deployment Architecture](#deployment-architecture)
  - [Design Patterns](#design-patterns)
  - [Scalability Considerations](#scalability-considerations)
- [Frameworks and Tools](#frameworks-and-tools)
- [Getting Started](#getting-started)

## Introduction
This is a showcase to demonstrate a modern **Payment API** system with enterprise-grade microservices architecture. The project demonstrates advanced security patterns, comprehensive testing strategies, and production-ready deployment configurations suitable for enterprise environments.

## Features
- **🏗️ Microservices Architecture**: Three-tier service architecture with clear separation of concerns
- **🔒 Enterprise Security**: Hybrid authentication with Microsoft Entra ID and internal JWT tokens
- **🧪 Comprehensive Testing**: 101+ unit tests with modern testing frameworks (JUnit 5, Mockito, AssertJ)
- **📚 Complete Documentation**: OpenAPI 3.0 specifications and detailed architectural documentation
- **🚀 Production Ready**: Kubernetes deployment manifests with monitoring and observability
- **⚡ Modern Technology Stack**: Java 21, Helidon 4.2.6, DynamoDB, and reactive programming
- **🔍 Observability**: Prometheus metrics, Grafana dashboards, and structured logging
- **🌐 Cloud Native**: Docker containerization with Kubernetes orchestration
- **🛡️ Security Best Practices**: Defense in depth, zero trust architecture, and secret management
- **📊 Payment Processing**: Core payment functionality with balance verification and transaction management

## Frameworks and Tools

### Core Frameworks
| Framework | Version | Purpose |
|-----------|---------|---------|
| **Helidon SE** | 4.2.6 | Reactive microservices framework |
| **Java** | 21 (LTS) | Programming language with modern features |
| **Maven** | 3.x | Build automation and dependency management |

### Data & Storage
| Technology | Purpose |
|------------|---------|
| **DynamoDB** | Primary NoSQL database for payment data |
| **LocalStack** | Local AWS services emulation for development |

### Security & Authentication
| Technology | Purpose |
|------------|---------|
| **Microsoft Entra ID** | Enterprise identity provider |
| **MSAL** | Microsoft Authentication Library |
| **JWT** | Internal service-to-service authentication |

### DevOps & Operations
| Tool | Purpose |
|------|---------|
| **Docker** | Containerization platform |
| **Kubernetes** | Container orchestration |
| **Prometheus** | Metrics collection and monitoring |
| **Grafana** | Data visualization and alerting |

### Testing & Quality
| Framework | Version | Purpose |
|-----------|---------|---------|
| **JUnit** | 5.11.3 | Modern testing framework |
| **Mockito** | 5.14.2 | Mocking framework for unit tests |
| **AssertJ** | 3.26.3 | Fluent assertion library |

## Application Architecture

### Overview
The Payment Showcase application implements a modern **microservices architecture** with enterprise-grade security, demonstrating best practices for payment processing systems. The system follows a **Backend for Frontend (BFF)** pattern with secure service-to-service communication.

### Architecture Diagram
```
┌─────────────────────┐    ┌─────────────────────┐    ┌─────────────────────┐
│                     │    │                     │    │                     │
│   Frontend/Client   │    │   Payment BFF       │    │  Payment API        │
│   (External Users)  ├────┤   (Port 8080)       ├────┤  (Port 8282)        │
│                     │    │                     │    │                     │
└─────────────────────┘    └─────────┬───────────┘    └─────────┬───────────┘
                                     │                          │
                                     │                          │
                                     │                          │
                              ┌──────▼──────────┐               │
                              │                 │               │
                              │ Bank Account API│               │
                              │  (Port 8181)    │               │
                              │                 │               │
                              └─────────────────┘               │
                                                                │
                              ┌─────────────────────────────────▼───┐
                              │                                     │
                              │        DynamoDB                     │
                              │      (LocalStack)                   │
                              │                                     │
                              └─────────────────────────────────────┘
```

### Service Components

#### 1. Payment BFF (Backend for Frontend)
- **Port**: 8080
- **Purpose**: Orchestration layer and API gateway
- **Responsibilities**:
  - User authentication via Microsoft Entra ID
  - Request orchestration between services
  - Internal JWT token generation
  - Business logic coordination
  - Error handling and response formatting

#### 2. Payment API (Core Processing)
- **Port**: 8282
- **Purpose**: Core payment processing engine
- **Responsibilities**:
  - Payment transaction processing
  - Payment data persistence to DynamoDB
  - Payment status management
  - Internal API authentication validation
  - Business rule enforcement

#### 3. Bank Account API (Account Management)
- **Port**: 8181
- **Purpose**: Account balance and validation service
- **Responsibilities**:
  - Account balance retrieval
  - Account validation
  - Balance verification for payments
  - Account information management

#### 4. Supporting Infrastructure
- **DynamoDB**: Primary data store for payment records
- **LocalStack**: Local AWS services emulation for development
- **Prometheus**: Metrics collection and monitoring
- **Grafana**: Visualization and alerting dashboards

### Security Architecture

#### Hybrid Authentication Model
The application implements a sophisticated **two-tier security model**:

##### External Authentication (Client → BFF)
- **Microsoft Entra ID (Azure AD)**: Enterprise identity provider
- **MSAL Integration**: Microsoft Authentication Library
- **OIDC/OAuth 2.0**: Standard authentication protocols
- **Role-Based Access Control**: User permissions via Microsoft Graph

##### Internal Authentication (BFF → APIs)
- **Internal JWT Tokens**: Service-to-service authentication
- **Short-lived Tokens**: 15-minute expiration for security
- **Strong Cryptography**: HMAC-SHA512 signatures
- **Service Authorization**: Validated service identity

#### Security Configuration
```yaml
# Microsoft Entra ID Configuration
msal:
  tenant-id: "${ENTRA_TENANT_ID}"
  client-id: "${ENTRA_CLIENT_ID}"
  authority: "https://login.microsoftonline.com/${ENTRA_TENANT_ID}"

# Internal JWT Configuration
security:
  internal-jwt:
    secret: "${INTERNAL_JWT_SECRET}"
    issuer: "payment-showcase"
    audience: "internal-apis"
    expiration-minutes: 15
```

### Data Flow

#### Payment Processing Flow
1. **Authentication**: User authenticates via Microsoft Entra ID
2. **Request Initiation**: Frontend sends payment request to Payment BFF
3. **Token Generation**: BFF generates internal JWT for service calls
4. **Balance Verification**: BFF calls Bank Account API to verify sufficient funds
5. **Payment Processing**: BFF calls Payment API to process the transaction
6. **Data Persistence**: Payment API stores transaction in DynamoDB
7. **Response**: Success/failure response flows back through the chain

#### Service Communication Pattern
```
Client Request → Entra ID Auth → Payment BFF → Internal JWT → APIs → DynamoDB
                                      ↓
                              Balance Check (Bank Account API)
                                      ↓
                              Payment Processing (Payment API)
```

### Technology Stack

#### Core Framework
- **Helidon SE 4.2.6**: Reactive microservices framework
- **Java 21**: Latest LTS with modern language features
- **Maven**: Dependency management and build automation

#### Data & Persistence
- **DynamoDB**: NoSQL database for scalable data storage
- **LocalStack**: Local AWS service emulation

#### Security & Authentication
- **Microsoft Entra ID**: Enterprise identity provider
- **MSAL**: Microsoft Authentication Library
- **JWT**: JSON Web Tokens for service authentication

#### DevOps & Operations
- **Docker**: Containerization platform
- **Kubernetes**: Container orchestration
- **Prometheus**: Metrics and monitoring
- **Grafana**: Data visualization and alerting

#### Testing & Quality
- **JUnit 5**: Modern testing framework
- **Mockito**: Mocking framework
- **AssertJ**: Fluent assertion library
- **101 Unit Tests**: Comprehensive test coverage

### Deployment Architecture

#### Containerization
Each service is containerized with optimized Docker images:
```dockerfile
FROM bellsoft/liberica-openjdk-centos:21
COPY target/service-*.jar service.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/service.jar"]
```

#### Kubernetes Deployment
- **Namespace isolation** for environment separation
- **ConfigMaps** for configuration management
- **Secrets** for sensitive data handling
- **Health checks** and readiness probes
- **Horizontal Pod Autoscaling** support

#### Observability
- **Structured logging** with appropriate log levels
- **Metrics collection** via Prometheus
- **Health check endpoints** for monitoring
- **Distributed tracing** ready (can be extended)

### Design Patterns

#### Architectural Patterns
- **Microservices Architecture**: Loosely coupled, independently deployable services
- **Backend for Frontend (BFF)**: Specialized backend for frontend requirements
- **API Gateway Pattern**: Single entry point for client requests
- **Database per Service**: Each service manages its own data

#### Security Patterns
- **Defense in Depth**: Multiple layers of security controls
- **Zero Trust Architecture**: Every request is authenticated and authorized
- **Token-based Authentication**: Stateless authentication mechanism
- **Secret Management**: Externalized configuration for sensitive data

#### Resilience Patterns
- **Health Checks**: Service availability monitoring
- **Graceful Shutdown**: Proper resource cleanup
- **Configuration Externalization**: Environment-specific configurations
- **Circuit Breaker Ready**: Can be extended with Resilience4j

### Scalability Considerations

#### Horizontal Scaling
- **Stateless Services**: All services designed for horizontal scaling
- **Load Balancing**: Kubernetes native load balancing
- **Auto-scaling**: HPA (Horizontal Pod Autoscaler) ready

#### Performance Optimization
- **Reactive Programming**: Non-blocking I/O operations
- **Connection Pooling**: Efficient resource utilization
- **Caching Strategy**: Token caching for authentication
- **Database Optimization**: DynamoDB for high-performance NoSQL operations

## Getting Started

### Prerequisites
- **Java 21** or later
- **Maven 3.6+** for building
- **Docker** and **Docker Compose** for containerization
- **kubectl** for Kubernetes deployment (optional)

### Local Development

#### 1. Using Docker Compose (Recommended)
```bash
# Start all services with dependencies
docker-compose up

# Access the services:
# Payment BFF: http://localhost:8080
# Payment API: http://localhost:8282
# Bank Account API: http://localhost:8181
# Prometheus: http://localhost:9090
# Grafana: http://localhost:3000
```

#### 2. Manual Setup
```bash
# Build all modules
mvn clean compile -f payment-bff/pom.xml
mvn clean compile -f payment-api/pom.xml
mvn clean compile -f bankaccount-api/pom.xml

# Run tests
mvn test -f payment-bff/pom.xml
mvn test -f payment-api/pom.xml
mvn test -f bankaccount-api/pom.xml

# Start LocalStack (for DynamoDB)
docker run -p 4566:4566 localstack/localstack

# Start services (in separate terminals)
java -jar payment-bff/target/payment-bff-*.jar
java -jar payment-api/target/payment-api-*.jar
java -jar bankaccount-api/target/bankaccount-api-*.jar
```

### Production Deployment

#### Kubernetes Deployment
```bash
# Navigate to deployment directory
cd deployment

# Deploy to Kubernetes
./deploy.sh

# Or manually deploy
kubectl apply -f namespace.yaml
kubectl apply -f secrets.yaml
kubectl apply -k .
```

### API Documentation
- **Payment BFF API**: [docs/api/payment-bff-api.yaml](docs/api/payment-bff-api.yaml)
- **Payment API**: [docs/api/payment-api.yaml](docs/api/payment-api.yaml)
- **Bank Account API**: [docs/api/bankaccount-api.yaml](docs/api/bankaccount-api.yaml)

### Configuration
See the [Hybrid Security Architecture](HYBRID_SECURITY_ARCHITECTURE.md) document for detailed configuration instructions including:
- Microsoft Entra ID setup
- Environment variables
- Security configuration
- Deployment considerations