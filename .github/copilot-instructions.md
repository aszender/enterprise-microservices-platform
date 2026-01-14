# Java + Spring + Frontend Learning Repo (AI Agent Guide)

This repo is a learning workspace for Java/Spring interview prep (Jan 2026). It’s a monorepo with multiple small projects; changes should stay simple and demo-focused.

## Repo Map (what lives where)
- Java fundamentals + OOP demos: [App.java](../App.java) + [basic/](../basic/)
- Spring Boot REST API (Products): [spring-backend/](../spring-backend/)
- Vue 3 UI that talks to Spring API via Vite proxy: [frontVUE/products-ui/](../frontVUE/products-ui/)
- React Vite starter (currently template): [frontReact/products-react/](../frontReact/products-react/)
- Vanilla JS/TS practice scripts: [frontVainilla/](../frontVainilla/)

## Key Data Flow (Products app)
- Backend endpoints: [ProductController](../spring-backend/src/main/java/com/aszender/spring_backend/controller/ProductController.java)
  - Base path: `/api/products` (CRUD + `/search?keyword=...`)
- Vue UI calls the API through a tiny fetch client: [productsApi.js](../frontVUE/products-ui/src/api/productsApi.js)
  - Dev proxy lives in: [vite.config.js](../frontVUE/products-ui/vite.config.js) (`/api` → `http://localhost:8080`)
- Error shape comes from: [GlobalExceptionHandler](../spring-backend/src/main/java/com/aszender/spring_backend/exception/GlobalExceptionHandler.java)

## Workflows (per subproject)
- Java demos (root): `javac App.java basic/*.java && java App`
- Spring backend: `cd spring-backend && ./mvnw spring-boot:run` (Java 17, Spring Boot 4)
- Spring tests: `cd spring-backend && ./mvnw test` (currently just `contextLoads`)
- Vue UI: `cd frontVUE/products-ui && npm install && npm run dev`
- React UI: `cd frontReact/products-react && npm install && npm run dev`

## Conventions to follow (project-specific)
- Java demo code in [basic/JavaCore.java](../basic/JavaCore.java): keep concepts isolated in small private methods and call them from `executeValues()`.
- OOP demos use nested inner classes in [basic/oop.java](../basic/oop.java) (instantiate like `new oop().new Dog(...)` from [App.java](../App.java)).
- Spring REST layer uses DTOs + mapping helpers inside the controller (see `toDto` / `toEntity` in ProductController) instead of returning JPA entities directly.
- Spring uses in-memory H2 by default (see [application.properties](../spring-backend/src/main/resources/application.properties)); keep changes compatible with that.
- Vue UI state is centralized in a small singleton store (no Pinia): [productsStore.js](../frontVUE/products-ui/src/stores/productsStore.js) + composable [useProducts.js](../frontVUE/products-ui/src/composables/useProducts.js).
