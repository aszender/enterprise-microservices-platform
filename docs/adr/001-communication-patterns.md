# ADR-001: Communication Patterns

## Status
Accepted

## Context
Services need to coordinate stock reservation and state propagation across independent bounded contexts (Products, Orders, Inventory). Some operations require synchronous confirmation (reservation), while others can be propagated asynchronously for eventual consistency.

## Decision
- Use gRPC for synchronous stock reservation where immediate confirmation is required.
- Use Kafka for asynchronous domain events to propagate facts between services (product created, stock reserved, low stock events).

## Consequences
- Strong consistency for reservations (through gRPC) where user-facing flows require immediate feedback.
- Eventual consistency for catalog and projections; consumers must be idempotent and handle replay.
- Operational complexity added by Kafka (offset management, consumer groups, DLQs).
- Compensating actions preferred over distributed transactions to keep services decoupled.

## Notes
Event schemas are versioned via topic suffixes (e.g. `.v1`). Backward-compatible changes should add optional fields; breaking changes require a new topic version and coordinated consumer rollout.
