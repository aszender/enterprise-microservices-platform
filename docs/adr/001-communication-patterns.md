# ADR-001: Communication Patterns

## Context

The platform has three bounded contexts (Products, Orders, Inventory) with both immediate and eventual consistency needs.

## Decision

Use **gRPC** for synchronous stock reservation decisions and **Kafka domain events** for asynchronous propagation across contexts.

## Consequences

- Clear boundary between request/response decisions and event-driven facts.
- Supports replay/idempotency patterns with Kafka inbox.
- Adds operational complexity (broker management, schema/event versioning).

## Alternatives considered

- REST-only: simpler but weaker for replayable integration facts.
- Kafka-only: no immediate stock decision path for order flow.
- Shared database: stronger coupling and ownership violations.
