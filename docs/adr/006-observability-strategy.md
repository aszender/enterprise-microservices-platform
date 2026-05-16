# ADR-006: Observability Strategy

## Context

Distributed services need operational visibility across metrics, traces, logs, and request correlation for debugging and incident response.

## Decision

Use Spring Boot Actuator + Micrometer Prometheus metrics, OpenTelemetry tracing export (OTLP), correlation ID propagation (`X-Correlation-Id`), and local observability stack (Prometheus/Grafana/Jaeger).

## Consequences

- Consistent service health/readiness and runtime signal.
- Trace/log correlation improves root-cause analysis across services.
- Requires maintaining telemetry config and dashboard/alert evolution.

## Alternatives considered

- Metrics-only baseline without tracing.
- Vendor-specific APM-only approach.
- Minimal health endpoint without centralized observability tooling.
