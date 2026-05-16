# ADR-006: CI Verification Gate

## Status

Accepted

## Context

The repository is a multi-module Maven project with `contracts`, `products-service`, `orders-service`, and `inventory-service`. Pull requests need a repeatable baseline that compiles contracts, compiles all services, and runs tests.

The root Maven wrapper is the canonical command for this check.

## Decision

Use GitHub Actions to run:

```bash
./mvnw -B clean verify
```

The workflow runs on pushes and pull requests that affect Java modules, Maven files, the workflow itself, or relevant service resources.

Use Temurin Java 17 and cache Maven dependencies through `actions/setup-java`.

## Consequences

CI exercises the same command developers can run locally.

Contract generation and service compilation happen together, reducing drift between protobuf definitions and service code.

The first run can be slower while Maven dependencies are downloaded, but later runs benefit from dependency caching.

CI does not yet run Docker Compose smoke tests, frontend tests, security scanning, or broker-backed integration tests.

## Alternatives

Run module-specific jobs only. This could be faster, but the current repo benefits from a single integration gate while the module count is small.

Run `test` instead of `verify`. `verify` is the safer lifecycle target because it includes all earlier phases and leaves room for future verification plugins.

Skip CI until the architecture stabilizes. That would allow regressions in generated contracts, wiring, and tests to reach the main branch.
