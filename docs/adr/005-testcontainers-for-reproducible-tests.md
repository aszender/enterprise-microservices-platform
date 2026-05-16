# ADR-005: Testcontainers for Reproducible Tests

## Context

The repository must run `./mvnw clean verify` without manually installed PostgreSQL, Kafka, or Redis.

## Decision

Adopt Testcontainers integration tests for service contexts that require PostgreSQL/Kafka/Redis and keep the CI gate on `clean verify`.

## Consequences

- Infrastructure-dependent tests become reproducible across developer machines and CI.
- Better confidence in migration/config behavior than pure in-memory-only tests.
- Longer build times and Docker runtime dependency for integration test execution.

## Alternatives considered

- Manual local infra setup.
- Embedded/in-memory substitutes for all integrations.
- External shared test environments.
