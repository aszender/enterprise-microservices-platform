# ADR-004: Inventory Concurrency Control

## Context

Concurrent reservations must not oversell stock, and repeated requests for the same order must be idempotent.

## Decision

Use conditional atomic stock updates (`available >= quantity`), reservation uniqueness on `order_id`, and transactional reservation/release flows with rollback on partial failure.

## Consequences

- Oversell prevention under concurrent requests.
- Deterministic idempotency for duplicate reserve/release requests.
- Requires careful SQL update semantics and concurrency testing.

## Alternatives considered

- Pessimistic row locks for all reservation paths.
- Optimistic locking with retry loops only.
- Single-threaded command queue per product key.
