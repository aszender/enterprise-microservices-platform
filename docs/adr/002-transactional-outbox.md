# ADR-002: Transactional Outbox

## Context

Order creation and cancellation must persist domain state and publish Kafka events without message loss when Kafka is unavailable.

## Decision

Persist outbox events in the same transaction as order state changes, then publish with a scheduled relay that marks rows `PUBLISHED` only after successful Kafka send.

## Consequences

- Eliminates after-commit publish loss window.
- Enables retry with persisted attempts and error metadata.
- Requires polling and outbox table lifecycle management.

## Alternatives considered

- TransactionSynchronization afterCommit callbacks.
- Direct Kafka publish inside transactional service method.
- Two-phase commit across DB and Kafka (not practical for this scope).
