# ADR-003: Inbox Idempotency and DLQ

## Context

Kafka delivery is at-least-once. Consumers can receive duplicates, process failures, and broker re-deliveries.

## Decision

Use persisted inbox rows keyed by `(topic, partition, offset)` with status transitions (`RECEIVED`, `PROCESSING`, `PROCESSED`, `FAILED`) and Spring Kafka retry + DLQ for exhausted failures.

## Consequences

- Duplicate records are skipped safely after successful processing.
- Failure diagnostics are persisted (`attempts`, `lastError`, timestamps).
- Requires inbox schema evolution and retention strategy.

## Alternatives considered

- In-memory dedupe cache.
- Exactly-once Kafka transactions end-to-end.
- No inbox and rely on best-effort consumer logic.
