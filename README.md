# Payment Showcase

[![Test and Build Image](https://github.com/lonecalvary78/showcase-payment/actions/workflows/test-and-build-image.yaml/badge.svg)](https://github.com/lonecalvary78/showcase-payment/actions/workflows/test-and-build-image.yaml)
[![sonar-analysis](https://github.com/lonecalvary78/showcase-payment/actions/workflows/sonar-analysis.yaml/badge.svg)](https://github.com/lonecalvary78/showcase-payment/actions/workflows/sonar-analysis.yaml)

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
- **🔒 Enterprise Security**: Secure service-to-service communication patterns
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
| **HTTP** | Direct service-to-service communication |

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
  - Request orchestration between services
  - Business logic coordination
  - Error handling and response formatting

#### 2. Payment API (Core Processing)
- **Port**: 8282
- **Purpose**: Core payment processing engine
- **Responsibilities**:
  - Payment transaction processing
  - Payment data persistence to DynamoDB
  - Payment status management
  - Business rule enforcement

#### 3. Bank Account API (Account Management)
- **Port**: 8181
- **Purpose**: Account balance and validation service
- **Responsibilities**:
  - Account balance retrieval

#### 4. Supporting Infrastructure
- **DynamoDB**: Primary data store for payment records
- **LocalStack**: Local AWS services emulation for development
- **Prometheus**: Metrics collection and monitoring
- **Grafana**: Visualization and alerting dashboards

### Data Flow

#### Payment Processing Flow
1. **Request Initiation**: Client sends payment request to Payment BFF
2. **Balance Verification**: BFF calls Bank Account API to verify sufficient funds
3. **Payment Processing**: BFF calls Payment API to process the transaction
4. **Data Persistence**: Payment API stores transaction in DynamoDB
5. **Response**: Success/failure response flows back through the chain

#### Service Communication Pattern

##### High-Level Flow
```
┌──────────────┐    ┌──────────────┐    ┌──────────────┐    ┌──────────────┐
│              │    │              │    │              │    │              │
│    Client    │───▶│ Payment BFF  │───▶│   Service    │───▶│     APIs     │
│              │    │ (Port 8080)  │    │     Calls    │    │              │
│              │    │              │    │              │    │              │
└──────────────┘    └──────────────┘    └──────────────┘    └──────────────┘
```

##### Detailed Communication Flow
```
┌─────────────────┐
│   Client App    │
│  (Frontend/API) │
└─────────┬───────┘
          │ 1. POST /payments
          │ (PaymentRequestDTO)
          ▼
┌─────────────────┐
│   Payment BFF   │ ◄─── Entry Point & Orchestrator
│   (Port 8080)   │
└─────────┬───────┘
          │ 2. Orchestrate Service Calls
          │ (Direct HTTP requests)
          ▼
     ┌─────────────────────────────────┐
     │     Service-to-Service Calls    │
     │       (Direct HTTP Calls)       │
     └─────────────────────────────────┘
          │                    │
          │ 3a. Check Balance  │ 3b. Process Payment
          ▼                    ▼
┌─────────────────┐    ┌─────────────────┐
│ Bank Account    │    │   Payment API   │
│ API (8181)      │    │   (Port 8282)   │
│                 │    │                 │
│ GET /balances   │    │ POST /payments  │
│ ?accountNo=...  │    │                 │
└─────────────────┘    └─────────┬───────┘
          │                      │ 4. Persist Data
          │                      ▼
          │            ┌─────────────────┐
          │            │    DynamoDB     │
          │            │   (LocalStack)  │
          │            │                 │
          │            │ Store Payment   │
          │            │   Transaction   │
          │            └─────────────────┘
          │
          ▼
    ┌─────────────────────────────────┐
    │         Response Chain          │
    │    (Success/Error Messages)     │
    └─────────────────────────────────┘
          │
          ▼
┌─────────────────┐
│   Client App    │ ◄─── 5. Final Response
│   (Response)    │      (201 Created or Error)
└─────────────────┘
```

##### Service Call Pattern
```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│  Payment BFF    │    │   HTTP Request  │    │   Target API    │
│                 │    │                 │    │                 │
│ 1. User Request │───▶│ 2. Direct HTTP  │───▶│ 3. Process      │
│ 2. Build HTTP   │    │    - Headers    │    │    - Validate   │
│    Request      │    │    - Body       │    │    - Execute    │
│                 │    │    - Endpoint   │    │    - Response   │
└─────────────────┘    └─────────────────┘    └─────────────────┘
```

##### Error Handling Pattern
```
┌─────────────────┐
│   Any Service   │
│   Encounters    │ ──┐
│     Error       │   │
└─────────────────┘   │
                      │ Error Bubbles Up
┌─────────────────┐   │
│  Payment BFF    │ ◄─┘
│  Error Handler  │ ──┐
│                 │   │ Transform & Log
└─────────────────┘   │
                      │
┌─────────────────┐   │
│     Client      │ ◄─┘
│ Receives HTTP   │    Structured Error Response
│ Error Response  │    (400/500 with details)
└─────────────────┘
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
- **HTTP-based Communication**: Direct service-to-service HTTP calls

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

### API Documentation
- **Payment BFF API**: [docs/api/payment-bff-api.yaml](docs/api/payment-bff-api.yaml)
- **Payment API**: [docs/api/payment-api.yaml](docs/api/payment-api.yaml)
- **Bank Account API**: [docs/api/bankaccount-api.yaml](docs/api/bankaccount-api.yaml)

### Configuration
For detailed configuration instructions including:
- Environment variables
- Security configuration
- Deployment considerations

Refer to the configuration files in each service module.