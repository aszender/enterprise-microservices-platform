#!/usr/bin/env bash

# If invoked as `sh dev.sh`, re-run with bash (script relies on bash features).
if [[ -z "${BASH_VERSION:-}" ]]; then
  exec bash "$0" "$@"
fi
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
RUN_DIR="$ROOT_DIR/.run"
LOG_DIR="$ROOT_DIR/.logs"
PID_DIR="$RUN_DIR/pids"

mkdir -p "$PID_DIR" "$LOG_DIR"

usage() {
  cat <<'EOF'
Usage:
  ./dev.sh                # same as: ./dev.sh up
  ./dev.sh up             # start Postgres + build contracts + start services + start frontends
  ./dev.sh down           # stop everything started by this script
  ./dev.sh status         # show status of tracked processes

Environment flags:
  WITH_REDIS=1            # also start a local Redis container (optional)
  WITH_KAFKA=1            # also start Kafka (docker compose) and run backends with kafka profile
  SKIP_NPM_INSTALL=1      # skip npm install checks

Kafka mode notes:
  - Kafka only affects NEW events (newly created products). Existing products won't backfill stock.
  - To debug inventory: tail inventory-service.log and look for Kafka connection errors.

Logs:
  ./.logs/<name>.log
PIDs:
  ./.run/pids/<name>.pid
EOF
}

docker_compose() {
  if docker compose version >/dev/null 2>&1; then
    docker compose "$@"
  else
    docker-compose "$@"
  fi
}

port_in_use() {
  local port="$1"
  lsof -nP -iTCP:"$port" -sTCP:LISTEN >/dev/null 2>&1
}

ensure_docker_running() {
  if ! docker info >/dev/null 2>&1; then
    echo "Docker is not running. Start Docker Desktop and retry." >&2
    exit 1
  fi
}

start_bg() {
  local name="$1"; shift
  local pid_file="$PID_DIR/$name.pid"
  local log_file="$LOG_DIR/$name.log"

  if [[ -f "$pid_file" ]]; then
    local existing_pid
    existing_pid="$(cat "$pid_file" 2>/dev/null || true)"
    if [[ -n "${existing_pid:-}" ]] && kill -0 "$existing_pid" >/dev/null 2>&1; then
      echo "Already running: $name (pid $existing_pid)"
      return 0
    fi
    rm -f "$pid_file"
  fi

  echo "Starting: $name"
  (
    cd "$ROOT_DIR"
    exec "$@"
  ) >>"$log_file" 2>&1 &

  echo $! >"$pid_file"
}

stop_bg() {
  local name="$1"
  local pid_file="$PID_DIR/$name.pid"

  if [[ ! -f "$pid_file" ]]; then
    return 0
  fi

  local pid
  pid="$(cat "$pid_file" 2>/dev/null || true)"
  if [[ -n "${pid:-}" ]] && kill -0 "$pid" >/dev/null 2>&1; then
    echo "Stopping: $name (pid $pid)"
    kill "$pid" >/dev/null 2>&1 || true

    # Give it a moment, then force kill if needed.
    for _ in {1..20}; do
      if ! kill -0 "$pid" >/dev/null 2>&1; then
        break
      fi
      sleep 0.2
    done
    if kill -0 "$pid" >/dev/null 2>&1; then
      kill -9 "$pid" >/dev/null 2>&1 || true
    fi
  fi

  rm -f "$pid_file"
}

status_bg() {
  local name="$1"
  local pid_file="$PID_DIR/$name.pid"

  if [[ ! -f "$pid_file" ]]; then
    echo "$name: not tracked"
    return 0
  fi

  local pid
  pid="$(cat "$pid_file" 2>/dev/null || true)"
  if [[ -n "${pid:-}" ]] && kill -0 "$pid" >/dev/null 2>&1; then
    echo "$name: running (pid $pid)"
  else
    echo "$name: not running (stale pid file)"
  fi
}

npm_install_if_needed() {
  local dir="$1"

  if [[ "${SKIP_NPM_INSTALL:-}" == "1" ]]; then
    return 0
  fi

  if [[ ! -d "$dir/node_modules" ]]; then
    echo "Installing npm deps in: $dir"
    (cd "$dir" && npm install)
  fi
}

cmd_up() {
  ensure_docker_running

  echo "Bringing up Postgres..."
  docker_compose up -d postgres

  if [[ "${WITH_KAFKA:-}" == "1" ]]; then
    echo "Bringing up Kafka (optional)..."
    docker_compose up -d kafka
  fi

  if [[ "${WITH_REDIS:-}" == "1" ]]; then
    if ! docker ps --format '{{.Names}}' | grep -qx redis-local; then
      echo "Starting Redis (optional)..."
      docker run -d --name redis-local -p 6379:6379 redis:7-alpine >/dev/null
    else
      echo "Redis already running: redis-local"
    fi
  fi

  echo "Building and installing contracts (local Maven repo)..."
  "$ROOT_DIR/mvnw" -pl contracts -am install -DskipTests

  if port_in_use 8080; then echo "Port 8080 is already in use." >&2; exit 1; fi
  if port_in_use 8081; then echo "Port 8081 is already in use." >&2; exit 1; fi
  if port_in_use 8082; then echo "Port 8082 is already in use." >&2; exit 1; fi

  if [[ "${WITH_KAFKA:-}" == "1" ]]; then
    start_bg products-service env SPRING_PROFILES_ACTIVE=kafka "$ROOT_DIR/mvnw" -pl products-service spring-boot:run
    start_bg inventory-service env SPRING_PROFILES_ACTIVE=kafka "$ROOT_DIR/mvnw" -pl inventory-service spring-boot:run
    start_bg orders-service env SPRING_PROFILES_ACTIVE=kafka "$ROOT_DIR/mvnw" -pl orders-service spring-boot:run
  else
    start_bg products-service "$ROOT_DIR/mvnw" -pl products-service spring-boot:run
    start_bg inventory-service "$ROOT_DIR/mvnw" -pl inventory-service spring-boot:run
    start_bg orders-service "$ROOT_DIR/mvnw" -pl orders-service spring-boot:run
  fi

  npm_install_if_needed "$ROOT_DIR/frontVUE/products-ui"
  npm_install_if_needed "$ROOT_DIR/frontReact/products-react"

  # Source nvm if available so we get the correct Node version
  local nvm_init=""
  if [[ -s "$HOME/.nvm/nvm.sh" ]]; then
    nvm_init="source $HOME/.nvm/nvm.sh && "
  fi

  start_bg vue-ui bash -c "${nvm_init}cd '$ROOT_DIR/frontVUE/products-ui' && npm run dev"
  start_bg react-ui bash -c "${nvm_init}cd '$ROOT_DIR/frontReact/products-react' && npm run dev"

  echo
  echo "All processes started in the background (no need to run docker ps)."
  echo "Check status with: ./dev.sh status"
  if [[ "${WITH_KAFKA:-}" == "1" ]]; then
    echo "Kafka mode is ON. Create a NEW product, then refresh Inventory."
  fi
  echo "Tail logs with:" 
  echo "  tail -f ./.logs/products-service.log"
  echo "  tail -f ./.logs/orders-service.log"
  echo "  tail -f ./.logs/inventory-service.log"
  echo "  tail -f ./.logs/vue-ui.log"
  echo "  tail -f ./.logs/react-ui.log"
}

cmd_down() {
  stop_bg react-ui
  stop_bg vue-ui
  stop_bg orders-service
  stop_bg inventory-service
  stop_bg products-service

  if [[ "${WITH_REDIS:-}" == "1" ]]; then
    docker stop redis-local >/dev/null 2>&1 || true
    docker rm redis-local >/dev/null 2>&1 || true
  fi

  echo "Stopping Postgres..."
  docker_compose stop postgres >/dev/null 2>&1 || true

  if [[ "${WITH_KAFKA:-}" == "1" ]]; then
    echo "Stopping Kafka (optional)..."
    docker_compose stop kafka >/dev/null 2>&1 || true
  fi
}

cmd_status() {
  status_bg products-service
  status_bg inventory-service
  status_bg orders-service
  status_bg vue-ui
  status_bg react-ui
}

main() {
  local cmd="${1:-up}"
  case "$cmd" in
    up) cmd_up ;;
    down) cmd_down ;;
    status) cmd_status ;;
    -h|--help|help)
      usage
      exit 0
      ;;
    *)
      echo "Unknown command: $cmd" >&2
      usage
      exit 2
      ;;
  esac
}

main "$@"
