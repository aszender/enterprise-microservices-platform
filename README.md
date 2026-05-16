# Enterprise Microservices Platform

Production-oriented Java/Spring microservices reference implementation focused on distributed transactions, event-driven consistency, concurrency-safe inventory, transactional outbox, Kafka retry/DLQ, reproducible integration tests, and observability.

## Architecture Overview

- `products-service`: catalog ownership (products, categories, stock-status projection).
- `orders-service`: order lifecycle ownership (orders, order_items, outbox_events).
- `inventory-service`: stock ownership (stock_items, reservations, Kafka inbox).
- `contracts`: gRPC protobuf contracts for synchronous inventory reservation.

## Bounded Contexts and Service Ownership

- **Products** owns product metadata and publish-on-create events.
- **Orders** owns order state transitions and outbound order domain events.
- **Inventory** owns stock mutation, reservation idempotency, and low-stock signaling.
- Services do not share database tables; integration uses gRPC + Kafka events.

## Event Flow

1. `products.product-created.v1` emitted by Products.
2. `orders.order-created.v1` and `orders.order-cancelled.v1` emitted by Orders (through outbox relay).
3. `inventory.stock-reserved.v1` and `inventory.low-stock.v1` emitted by Inventory.
4. Kafka consumers persist inbox metadata and process idempotently.

## Transactional Outbox

- Order create/cancel writes domain row(s) and outbox row in one transaction.
- Scheduled outbox publisher polls `PENDING` rows, publishes to Kafka, then marks `PUBLISHED` only on successful send.
- Retries are persisted with attempts and `lastError`; Kafka downtime does not drop domain events.

## Kafka Inbox, Retry, and DLQ

- Inventory inbox tracks `topic`, `partition`, `offset`, `eventId`, `eventType`, `eventVersion`, `attempts`, `lastError`, and timestamps.
- Processing states: `RECEIVED -> PROCESSING -> PROCESSED | FAILED`.
- Duplicate processed records are skipped safely.
- Spring Kafka `DefaultErrorHandler` routes exhausted retries to `<topic>.DLQ`.

## Inventory Concurrency Control

- Reservation uses atomic SQL update (`available >= requested`) and version increments.
- One reservation per order (`UNIQUE(order_id)`), duplicate reserve is idempotent.
- Reservation and release are transactional; partial line failure rolls back.
- Concurrency tests prove no oversell, idempotent reserve/release, and no partial mutation under insufficient stock.

## Security

- No hardcoded production secrets; JWT secret sourced from environment.
- JWT configuration validated at startup (Base64 + minimum key length).
- Write endpoints are protected; validation and consistent API error envelopes are enforced.

## Testing Strategy

- Unit and slice tests for controllers, services, and outbox/inbox behavior.
- Integration tests for outbox publisher and inventory concurrency.
- Testcontainers-based reproducibility for PostgreSQL/Kafka/Redis-backed contexts.
- Single command gate:

```bash
./mvnw -B clean verify
```

## Local Run

1. Copy `.env.example` to `.env` and adjust local values.
2. Start dependencies:

```bash
docker compose up -d postgres kafka redis prometheus grafana jaeger
```

3. Run services (in separate terminals):

```bash
cd products-service && ../mvnw spring-boot:run
cd orders-service && ../mvnw spring-boot:run
cd inventory-service && ../mvnw spring-boot:run
```

## Observability

- Metrics: `/actuator/prometheus` on each service.
- Health/readiness: `/actuator/health` and `/actuator/health/readiness`.
- Tracing: Micrometer + OpenTelemetry OTLP export to Jaeger collector endpoint.
- Correlation ID: `X-Correlation-Id` propagated and logged with trace IDs.

Key dashboards/endpoints:

- Prometheus: `http://localhost:9091`
- Grafana: `http://localhost:3000`
- Jaeger: `http://localhost:16686`

## Failure Scenarios Handled

- Kafka unavailable during order write: outbox remains pending for retry.
- Duplicate Kafka delivery: inbox dedupe prevents duplicate business mutation.
- Reservation race/overload: atomic stock update prevents oversell.
- Insufficient stock in multi-line order: transaction rollback prevents partial mutation.
- Duplicate release: idempotent release prevents double stock increment.

## Trade-offs

- Focused on consistency patterns over breadth of business features.
- Single-repo topology for interview readability, not multi-team governance simulation.
- Uses practical local defaults in `application-local.yml` to keep onboarding friction low.

## Interview Talking Points

- Why outbox + inbox is needed for at-least-once messaging.
- How inventory avoids oversell under concurrency.
- How retry/DLQ and persisted error metadata aid operability.
- Why test reproducibility via Testcontainers strengthens CI signal.
- How trace/correlation instrumentation improves distributed debugging.

## ADR Index

- [001 Communication Patterns](docs/adr/001-communication-patterns.md)
- [002 Transactional Outbox](docs/adr/002-transactional-outbox.md)
- [003 Inbox Idempotency and DLQ](docs/adr/003-inbox-idempotency-and-dlq.md)
- [004 Inventory Concurrency Control](docs/adr/004-inventory-concurrency-control.md)
- [005 Testcontainers for Reproducible Tests](docs/adr/005-testcontainers-for-reproducible-tests.md)
- [006 Observability Strategy](docs/adr/006-observability-strategy.md)
