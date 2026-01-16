
# Java + Spring + Frontend Monorepo (AI Agent Guide)

This monorepo is a hands-on learning platform for Java/Spring, modern frontend, and microservices. It is intentionally split into small, focused projects for interview prep and demo clarity.

## Architecture Overview
- **Java Demos:** Core Java/OOP concepts in [App.java](../App.java) and [basic/](../basic/). Each concept is isolated in its own method/class for clarity.
- **Spring Boot Services:**
  - [products-service/](../products-service/): REST API for products (CRUD, search, stock status)
  - [orders-service/](../orders-service/), [inventory-service/](../inventory-service/): Additional microservices (see docker-compose)
- **Frontend UIs:**
  - [frontVUE/products-ui/](../frontVUE/products-ui/): Vue 3 app, fetches from Spring API via Vite proxy
  - [frontReact/products-react/](../frontReact/products-react/): React Vite starter (minimal, for practice)
  - [frontVainilla/](../frontVainilla/): Vanilla JS/TS scripts for fundamentals
- **Integration:**
  - Services communicate via REST (see controller endpoints)
  - Kafka and Redis integration is demoed via profiles and docker-compose
  - Dev workflow is containerized (see [dev.sh](../dev.sh), [docker-compose.yml](../docker-compose.yml))

## Key Data Flows & Patterns
- **Products API:**
  - Endpoints: [ProductController.java](../products-service/src/main/java/com/aszender/spring_backend/controller/ProductController.java) (`/api/products`)
  - Error responses: [GlobalExceptionHandler.java](../products-service/src/main/java/com/aszender/spring_backend/exception/GlobalExceptionHandler.java)
  - DTO mapping: Use `toDto`/`toEntity` helpers inside controllers (do not expose JPA entities)
- **Vue UI:**
  - API client: [productsApi.js](../frontVUE/products-ui/src/api/productsApi.js)
  - State: Singleton store ([productsStore.js](../frontVUE/products-ui/src/stores/productsStore.js)), not Pinia
  - Dev proxy: [vite.config.js](../frontVUE/products-ui/vite.config.js) (proxies `/api` to backend)
- **Spring Profiles:**
  - Use `SPRING_PROFILES_ACTIVE=kafka` for Kafka integration
  - Use `SPRING_PROFILES_ACTIVE=redis` for Redis cache
  - Default profile uses in-memory H2 (see [application.properties](../products-service/src/main/resources/application.properties))

## Developer Workflows
- **Start all services:** `./dev.sh up` (add `WITH_KAFKA=1` or `WITH_REDIS=1` for extra services)
- **Stop all services:** `./dev.sh down`
- **Check status:** `./dev.sh status`
- **Build Java demos:** `javac App.java basic/*.java && java App`
- **Run Spring backend:** `cd products-service && ./mvnw spring-boot:run`
- **Run Vue UI:** `cd frontVUE/products-ui && npm install && npm run dev`
- **Run React UI:** `cd frontReact/products-react && npm install && npm run dev`
- **Run Spring tests:** `cd products-service && ./mvnw test`
- **Build contracts (protobuf):** `cd contracts && ./mvnw clean install -DskipTests`

## Project-Specific Conventions
- **Java demos:**
  - Each concept in its own method/class ([basic/JavaCore.java](../basic/JavaCore.java))
  - OOP demos use nested classes ([basic/oop.java](../basic/oop.java)), instantiate via `new oop().new Dog(...)`
- **Spring REST:**
  - Always use DTOs for API responses/requests
  - Map entities <-> DTOs with static helpers in controller
- **Frontend:**
  - Vue state is a singleton store, not Pinia
  - API calls are centralized in `/src/api/`
- **Integration:**
  - Use Vite proxy for local frontend-backend communication
  - Use profiles and docker-compose for optional Kafka/Redis

## References
- [README.md](../README.md): High-level goals and study plan
- [start.txt](../start.txt): Common dev commands and profiles

Keep all changes simple, demo-focused, and compatible with in-memory H2 unless otherwise required.
