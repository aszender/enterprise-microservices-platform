# Enterprise Microservices Platform

> **Full-Stack Demo**: Java 17 + Spring Boot 3 + gRPC + Kafka + Redis + PostgreSQL + Vue 3 + React 18

A production-ready microservices architecture demonstrating **enterprise patterns** for distributed systems, event-driven communication, and modern frontend development.

[![Java](https://img.shields.io/badge/Java-17-orange?logo=openjdk)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen?logo=springboot)](https://spring.io/projects/spring-boot)
[![Vue](https://img.shields.io/badge/Vue-3-42b883?logo=vuedotjs)](https://vuejs.org/)
[![React](https://img.shields.io/badge/React-18-61dafb?logo=react)](https://reactjs.org/)
[![Kafka](https://img.shields.io/badge/Kafka-Event%20Streaming-231F20?logo=apachekafka)](https://kafka.apache.org/)
[![Redis](https://img.shields.io/badge/Redis-Caching-DC382D?logo=redis)](https://redis.io/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-4169E1?logo=postgresql)](https://www.postgresql.org/)
[![gRPC](https://img.shields.io/badge/gRPC-Sync%20Communication-244c5a)](https://grpc.io/)

---

## Table of Contents

- [Architecture Overview](#-architecture-overview)
- [Key Design Decisions](#-key-design-decisions)
- [Tech Stack](#-tech-stack)
- [Service Breakdown](#-service-breakdown)
- [Communication Patterns](#-communication-patterns)
- [Quick Start](#-quick-start)
- [API Documentation](#-api-documentation)
- [Testing Strategy](#-testing-strategy)
- [Frontend Applications](#-frontend-applications)
- [Interview Talking Points](#-interview-talking-points)

---

## Architecture Overview

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                              FRONTEND LAYER                                  │
│  ┌─────────────────────┐              ┌─────────────────────┐               │
│  │     Vue 3 + Vite    │              │   React 18 + Vite   │               │
│  │   (Port 5173)       │              │   (Port 5174)       │               │
│  │  • Composition API  │              │  • Redux Toolkit    │               │
│  │  • Reactive Store   │              │  • Custom Hooks     │               │
│  └──────────┬──────────┘              └──────────┬──────────┘               │
│             │                                    │                          │
│             └──────────────┬─────────────────────┘                          │
│                            │ REST/HTTP                                      │
└────────────────────────────┼────────────────────────────────────────────────┘
                             │
┌────────────────────────────┼────────────────────────────────────────────────┐
│                            ▼          API GATEWAY / PROXY                   │
│                     Vite Dev Proxy (routes /api/* to services)              │
└────────────────────────────┬────────────────────────────────────────────────┘
                             │
┌────────────────────────────┼────────────────────────────────────────────────┐
│                      BACKEND SERVICES                                        │
│                            │                                                 │
│  ┌─────────────────────────┼─────────────────────────────────┐              │
│  │                         ▼                                  │              │
│  │  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐     │              │
│  │  │   PRODUCTS   │  │    ORDERS    │  │  INVENTORY   │     │              │
│  │  │   SERVICE    │  │   SERVICE    │  │   SERVICE    │     │              │
│  │  │  (Port 8080) │  │  (Port 8081) │  │  (Port 8082) │     │              │
│  │  │              │  │              │  │              │     │              │
│  │  │ • REST API   │  │ • REST API   │  │ • REST API   │     │              │
│  │  │ • JWT Auth   │  │ • JWT Auth   │  │ • gRPC Server│     │              │
│  │  │ • Redis Cache│  │ • gRPC Client│  │   (Port 9090)│     │              │
│  │  └──────┬───────┘  └──────┬───────┘  └──────┬───────┘     │              │
│  │         │                 │                  │             │              │
│  │         │                 │    gRPC (sync)   │             │              │
│  │         │                 └──────────────────┘             │              │
│  │         │                                                  │              │
│  └─────────┼──────────────────────────────────────────────────┘              │
│            │                                                                 │
│            │  Kafka (async events)                                           │
│            ▼                                                                 │
│  ┌─────────────────────────────────────────────────────────────┐            │
│  │                    APACHE KAFKA                              │            │
│  │  Topics:                                                     │            │
│  │  • products.product-created.v1  (Products → Inventory)       │            │
│  │  • inventory.stock-reserved.v1  (Inventory → Orders)         │            │
│  │  • inventory.low-stock.v1       (Inventory → Products)       │            │
│  └─────────────────────────────────────────────────────────────┘            │
│                                                                              │
└──────────────────────────────────────────────────────────────────────────────┘
                             │
┌────────────────────────────┼────────────────────────────────────────────────┐
│                      DATA LAYER                                              │
│                            │                                                 │
│  ┌─────────────────┐  ┌────┴────────┐  ┌─────────────────┐                  │
│  │   PostgreSQL    │  │    Redis    │  │   H2 In-Memory  │                  │
│  │   (Port 5432)   │  │ (Port 6379) │  │ (Orders/Inv)    │                  │
│  │                 │  │             │  │                 │                  │
│  │ • Products DB   │  │ • API Cache │  │ • Dev/Test DB   │                  │
│  │ • Persistent    │  │ • Sessions  │  │ • Fast Startup  │                  │
│  └─────────────────┘  └─────────────┘  └─────────────────┘                  │
│                                                                              │
└──────────────────────────────────────────────────────────────────────────────┘
```

---

## Key Design Decisions

### Why This Architecture?

| Decision | Rationale | Enterprise Benefit |
|----------|-----------|-------------------|
| **gRPC for sync operations** | Stock reservation requires immediate consistency | Sub-millisecond latency, type-safe contracts, bidirectional streaming ready |
| **Kafka for async events** | Decoupled services, event replay, audit trail | Horizontal scaling, fault tolerance, event sourcing ready |
| **Redis caching** | Reduce database load on high-traffic endpoints | 10-100x faster reads, distributed cache for multi-instance |
| **Shared Protobuf contracts** | Single source of truth for service interfaces | No API drift, generated clients, versioned schemas |
| **PostgreSQL + H2 hybrid** | Production-ready persistence + fast dev cycle | Real DB for catalog, in-memory for rapid iteration |

### Service Boundaries (Domain-Driven Design)

```
┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐
│    PRODUCTS     │     │     ORDERS      │     │   INVENTORY     │
│    BOUNDED      │     │    BOUNDED      │     │    BOUNDED      │
│    CONTEXT      │     │    CONTEXT      │     │    CONTEXT      │
├─────────────────┤     ├─────────────────┤     ├─────────────────┤
│ Owns:           │     │ Owns:           │     │ Owns:           │
│ • Product name  │     │ • Order status  │     │ • Stock levels  │
│ • Description   │     │ • Customer      │     │ • Reservations  │
│ • Price         │     │ • Line items    │     │ • Thresholds    │
│ • Catalog data  │     │ • Totals        │     │ • Availability  │
└─────────────────┘     └─────────────────┘     └─────────────────┘
```

---

## Tech Stack

### Backend

| Technology | Version | Purpose |
|------------|---------|---------|
| Java | 17 LTS | Core language with modern features (records, sealed classes, pattern matching) |
| Spring Boot | 3.x | Application framework, dependency injection, auto-configuration |
| Spring Data JPA | 3.x | Repository pattern, Hibernate ORM |
| Spring Security | 6.x | JWT authentication, role-based access control |
| Spring Kafka | 3.x | Event-driven messaging |
| gRPC Java | 1.62 | High-performance RPC framework |
| Protobuf | 3.25 | Schema-first API contracts |
| PostgreSQL | 16 | Production database (products catalog) |
| H2 | 2.x | In-memory database (orders, inventory for dev) |
| Redis | 7 | Distributed caching layer |

### Frontend

| Technology | Version | Purpose |
|------------|---------|---------|
| Vue 3 | 3.x | Reactive UI with Composition API |
| React | 18 | Component-based UI with hooks |
| Vite | 5.x | Lightning-fast build tool |
| Redux Toolkit | 2.x | State management (React) |
| TailwindCSS | 3.x | Utility-first styling |

### Infrastructure

| Technology | Purpose |
|------------|---------|
| Docker Compose | Local development orchestration |
| Apache Kafka | Event streaming platform |
| Maven | Build automation, dependency management |

---

## Service Breakdown

### 1. Products Service (Port 8080)

**Responsibilities**: Product catalog management, caching, authentication

**Key Features**:
- ✅ Full CRUD with pagination, sorting, search
- ✅ JWT authentication (HS256, 24h expiry)
- ✅ Redis caching with `@Cacheable`, `@CacheEvict`, `@CachePut`
- ✅ Publishes `ProductCreatedEvent` to Kafka
- ✅ Consumes `LowStockEvent` for dashboard projection

**API Endpoints**:
```
GET    /api/products                    # List all (+ pagination)
GET    /api/products/{id}               # Get single product
POST   /api/products                    # Create (requires JWT)
PUT    /api/products/{id}               # Full update
PATCH  /api/products/{id}               # Partial update
DELETE /api/products/{id}               # Delete
GET    /api/products/search?keyword=x   # Full-text search
POST   /api/auth/login                  # Get JWT token
POST   /api/auth/register               # Create user
```

---

### 2. Orders Service (Port 8081)

**Responsibilities**: Order lifecycle, stock reservation orchestration

**Key Features**:
- ✅ Order creation with line items
- ✅ **gRPC client** calls Inventory for stock reservation
- ✅ Status transitions: CREATED → RESERVED → CANCELLED
- ✅ Publishes order events to Kafka

**API Endpoints**:
```
GET    /api/orders                      # List all orders
GET    /api/orders/{id}                 # Get single order
POST   /api/orders                      # Create order
POST   /api/orders/{id}/reserve         # Reserve stock (gRPC → Inventory)
POST   /api/orders/{id}/cancel          # Cancel order
```

**gRPC Flow (Reserve Stock)**:
```
┌──────────────┐     gRPC (sync)      ┌──────────────┐
│    Orders    │ ─────────────────▶   │  Inventory   │
│   Service    │   ReserveStock()     │   Service    │
│              │ ◀─────────────────   │              │
│              │   Success/Fail       │              │
└──────────────┘                      └──────────────┘
```

---

### 3. Inventory Service (Port 8082 REST, Port 9090 gRPC)

**Responsibilities**: Stock management, reservation logic, low-stock alerts

**Key Features**:
- ✅ **gRPC server** for synchronous stock reservation
- ✅ Atomic stock updates with optimistic locking
- ✅ Low-stock threshold alerts (publishes to Kafka)
- ✅ Consumes `ProductCreatedEvent` to auto-create stock entries

**gRPC Contract** (`contracts/src/main/proto/inventory.proto`):
```protobuf
service InventoryService {
  rpc ReserveStock(ReserveStockRequest) returns (ReserveStockResponse);
}

message ReserveStockRequest {
  int64 order_id = 1;
  repeated ReserveItem items = 2;
}

message ReserveStockResponse {
  bool reserved = 1;
  string reason = 2;
}
```

---

## Communication Patterns

### Pattern 1: Synchronous (gRPC) - Stock Reservation

**Use case**: Stock reservation requires immediate consistency

```
User → Orders Service → gRPC → Inventory Service → Response
                    (blocking, ~50ms)
```

**Why gRPC?**
- Type-safe contracts (Protobuf)
- ~10x faster than REST
- Streaming-ready for future features

---

### Pattern 2: Asynchronous (Kafka) - Event Propagation

**Use case**: Notify services after state changes

```
Products Service  ──ProductCreatedEvent──▶  Kafka  ──▶  Inventory Service
Inventory Service ──LowStockEvent────────▶  Kafka  ──▶  Products Service
```

**Why Kafka?**
- Services remain decoupled
- Events can be replayed
- Multiple consumers possible

---

### Pattern 3: Caching (Redis)

**Use case**: High-traffic read endpoints

```
First request:  Cache MISS → PostgreSQL → Store in Redis → Return
Second request: Cache HIT  → Return from Redis (microseconds)
```

---

## Quick Start

### Prerequisites

- Docker Desktop
- Java 17+
- Node.js 20+

### One-Command Start

```bash
# Clone the repo
git clone https://github.com/aszender/java-spring-Guide.git
cd java-spring-Guide

# Start everything (Postgres + Kafka + Redis + services + UIs)
WITH_KAFKA=1 WITH_REDIS=1 ./dev.sh up

# Check status
./dev.sh status

# Stop everything
./dev.sh down
```

### Startup Options

| Command | What it starts |
|---------|----------------|
| `./dev.sh up` | Basic mode (Postgres + services + UIs) |
| `WITH_KAFKA=1 ./dev.sh up` | + Kafka event streaming |
| `WITH_REDIS=1 ./dev.sh up` | + Redis caching |
| `WITH_KAFKA=1 WITH_REDIS=1 ./dev.sh up` | Full production-like setup |

### Access Points

| Service | URL |
|---------|-----|
| Vue UI | http://localhost:5173 |
| React UI | http://localhost:5174 |
| Products API | http://localhost:8080/api/products |
| Orders API | http://localhost:8081/api/orders |
| Inventory API | http://localhost:8082/api/inventory/stock |

---

## API Documentation

### Authentication Flow

```bash
# 1. Login to get JWT token
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'

# 2. Use token for protected endpoints
curl -X POST http://localhost:8080/api/products \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{"name":"MacBook Pro","description":"16-inch","price":2499.99}'
```

### Complete Order Flow (E2E)

```bash
# Step 1: Create a product (triggers Kafka → Inventory creates stock)
curl -X POST http://localhost:8080/api/products \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{"name":"iPhone 15","price":999.99}'

# Step 2: Check inventory was auto-created
curl http://localhost:8082/api/inventory/stock

# Step 3: Create an order
curl -X POST http://localhost:8081/api/orders \
  -H "Content-Type: application/json" \
  -d '{"customerName":"John Doe","items":[{"productId":1,"quantity":2,"unitPrice":999.99}]}'

# Step 4: Reserve stock (gRPC call)
curl -X POST http://localhost:8081/api/orders/1/reserve

# Step 5: Check order status
curl http://localhost:8081/api/orders/1
```

---

## Testing Strategy

### Test Pyramid Implementation

```
                    ┌───────────┐
                    │   E2E     │  ← Manual testing via curl/UI
                    │  (few)    │
               ┌────┴───────────┴────┐
               │   Integration       │  ← @SpringBootTest + @Transactional
               │   (some)            │
          ┌────┴─────────────────────┴────┐
          │         Unit Tests            │  ← @ExtendWith(MockitoExtension)
          │         (many)                │     @WebMvcTest
          └───────────────────────────────┘
```

### Products Service Test Suite

Located in `products-service/src/test/java/`

| Test Class | Type | What it Tests |
|------------|------|---------------|
| `ProductServiceTest` | Unit | Service layer logic with mocked repository |
| `ProductControllerWebMvcTest` | Slice | REST endpoints with `@WebMvcTest` + MockMvc |
| `ProductRepositoryTest` | Integration | JPA queries with real database (`@SpringBootTest`) |

### Run Tests

```bash
# Run all tests (products-service)
cd products-service && ./mvnw test

# Run with verbose output
./mvnw test -Dtest=ProductServiceTest

# Run only unit tests (fast)
./mvnw test -Dgroups="unit"

# Run all services tests
./mvnw test -pl products-service,orders-service,inventory-service
```

### Testing Tools & Frameworks

| Tool | Purpose |
|------|---------|
| **JUnit 5** | Test framework with `@Test`, `@BeforeEach`, `@DisplayName` |
| **Mockito** | Mocking dependencies (`@Mock`, `@InjectMocks`, `when().thenReturn()`) |
| **AssertJ** | Fluent assertions (`assertThat(x).isEqualTo(y)`) |
| **MockMvc** | Test REST controllers without starting server |
| **@SpringBootTest** | Full integration tests with real beans |
| **@WebMvcTest** | Slice test for web layer only |
| **@DataJpaTest** | Slice test for repository layer |
| **H2** | In-memory database for fast integration tests |

### Test Coverage Focus

- ✅ **Happy path** - CRUD operations work correctly
- ✅ **Edge cases** - Not found, validation errors
- ✅ **Exception handling** - `ProductNotFoundException` thrown appropriately
- ✅ **Repository queries** - Custom JPQL/native queries return expected results

---

## Frontend Applications

### Vue 3 (`frontVUE/products-ui`)

- **Composition API** with `<script setup>`
- **Reactive Store** pattern (singleton module)
- **Composables** for reusable logic
- Pages: Products, Orders, Inventory

### React 18 (`frontReact/products-react`)

- **Redux Toolkit** for state management
- **Custom Hooks** wrapping Redux
- **Feature-based** folder structure
- Pages: Products, Orders, Inventory


## Project Structure

```
java-spring/
├── products-service/          # Product catalog + Auth + Cache
├── orders-service/            # Order management + gRPC client
├── inventory-service/         # Stock management + gRPC server
├── contracts/                 # Shared Protobuf definitions
├── frontVUE/products-ui/      # Vue 3 frontend
├── frontReact/products-react/ # React 18 frontend
├── basic/                     # Java fundamentals demos
├── docker-compose.yml         # Infrastructure
├── dev.sh                     # Startup script
└── pom.xml                    # Parent Maven POM
```

---

---

## Error Handling, Validation & Logging

### Global Error Handling
- Centralized with `@RestControllerAdvice` (`GlobalExceptionHandler`)
- Handles validation errors, not found, bad JSON, data integrity, and unexpected exceptions
- Returns structured JSON error responses (status, message, path, validation details)

### Validation
- Uses Jakarta Bean Validation (`@Valid`, `@NotNull`, etc.) on DTOs and controller inputs
- Automatic validation of incoming requests
- Validation errors are caught and returned as clear API error responses

### Logging
- Spring Boot logging enabled (Logback)
- Log levels set in `application.properties` (`INFO` for root, `DEBUG` for web)
- All requests, errors, and stack traces are logged for troubleshooting

---

## Future Enhancements

### Planned Production Deployment

This project currently runs locally with Docker Compose. For production deployment, the architecture would include:

#### Backend Services
- **ECS/Fargate**: Containerized microservices with auto-scaling
- **RDS PostgreSQL**: Multi-AZ deployment for high availability
- **ElastiCache Redis**: Distributed caching layer
- **MSK (Managed Kafka)**: Managed event streaming
- **Application Load Balancer**: Traffic distribution across service instances
- **ECR**: Private container registry

#### Frontend Applications
- **S3 + CloudFront**: Static site hosting with global CDN
  - Vue/React build artifacts deployed to S3 buckets
  - CloudFront for edge caching and HTTPS
  - Route 53 for custom domain management
  - Reduced latency with edge locations worldwide
- **API Gateway** (optional): Single entry point for backend APIs with rate limiting and authentication

#### CI/CD Pipeline (Jenkins)
```groovy
pipeline {
    stages {
        stage('Backend Build & Test') {
            steps {
                sh './mvnw clean test package'
            }
        }
        stage('Frontend Build') {
            steps {
                sh 'cd frontVUE && npm run build'
                sh 'cd frontReact && npm run build'
            }
        }
        stage('Deploy Frontend to S3') {
            steps {
                sh 'aws s3 sync frontVUE/dist/ s3://products-vue-app/'
                sh 'aws s3 sync frontReact/dist/ s3://products-react-app/'
                sh 'aws cloudfront create-invalidation --distribution-id XXX'
            }
        }
        stage('Deploy Backend to ECS') {
            steps {
                sh 'docker build -t products-service .'
                sh 'docker push ${ECR_REPO}/products-service:${BUILD_NUMBER}'
                sh 'aws ecs update-service --cluster prod --service products --force-new-deployment'
            }
        }
    }
}
```

#### System Design Considerations

**Scalability**:
- Stateless services enable horizontal scaling via ECS auto-scaling groups
- CloudFront CDN reduces backend load for static assets
- Database read replicas for Products service (read-heavy workload)
- Kafka topic partitioning for parallel event processing
- Redis cluster mode for distributed caching

**Security**:
- VPC isolation for backend services (private subnets)
- Security groups restricting inter-service communication
- S3 bucket policies with CloudFront Origin Access Identity (OAI)
- Secrets Manager for database credentials and API keys
- WAF on CloudFront for DDoS protection

**Reliability**:
- Multi-AZ RDS deployment (automatic failover)
- ECS service auto-recovery on health check failures
- Circuit breakers (Resilience4j) for fault isolation
- Kafka consumer groups with retry logic and dead letter queues
- CloudWatch alarms for proactive monitoring

**Cost Optimization**:
- ECS Fargate Spot instances for non-critical workloads
- S3 lifecycle policies for log retention
- CloudFront caching reduces origin requests
- RDS reserved instances for predictable workloads

### Monitoring & Observability
- **CloudWatch Logs**: Centralized logging for all services
- **CloudWatch Metrics**: CPU, memory, request latency dashboards
- **X-Ray**: Distributed tracing across microservices
- **Alarms**: Automated alerts for error rates, latency spikes, low disk space



## 📝 License

MIT License

---

## 👤 Author

**Andres Gonzalez**  
