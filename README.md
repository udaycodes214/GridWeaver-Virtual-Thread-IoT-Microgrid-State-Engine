# GridWeaver — Virtual Thread IoT Microgrid State Engine

GridWeaver is a full-stack educational microgrid simulation platform that combines Java 25 virtual threads, Spring Boot, Spring StateMachine, Kafka telemetry, WebSockets, and a React dashboard.

## Architecture

```mermaid
flowchart LR
  Simulator --> Service --> StateMachine
  Service --> Kafka
  Kafka --> Consumer
  Consumer --> WebSocket --> React
  React --> REST API --> Service
```

## Features

- Java 25 virtual-thread based node simulation
- Battery state transitions with Spring StateMachine
- REST API for health, nodes, stats, and simulation controls
- Kafka telemetry streaming when local Kafka is available
- WebSocket updates for the live dashboard
- Leaflet map showing node positions and statuses
- Event log and grid-zone balancing
- Storm simulation control

## Prerequisites

- Java 25
- Maven 3.9+
- Node.js 18+
- Docker (optional, for Kafka)

## Backend setup

```bash
cd backend
mvn spring-boot:run
```

The backend listens on http://localhost:8080.

## Kafka setup

```bash
cd kafka
docker compose up -d
```

If Kafka is not running, the backend continues in degraded mode and the app still works with local in-memory simulation.

## Frontend setup

```bash
cd frontend
npm install
npm run dev -- --host 0.0.0.0
```

Open http://localhost:5173.

## API examples

```bash
curl http://localhost:8080/api/health
curl http://localhost:8080/api/nodes
curl -X POST "http://localhost:8080/api/simulator/start?count=1000"
curl -X POST "http://localhost:8080/api/simulator/storm?enabled=true"
```

## Testing

```bash
cd backend
mvn test
cd ../frontend
npm run build
```

## Demo flow

1. Start Kafka if available.
2. Start the backend.
3. Start the frontend.
4. Click Start 1000 Nodes.
5. Watch map, event log, and stats update.
6. Toggle storm mode to see charging/discharging changes.
7. Observe real-time WebSocket updates.

## Limitations

- Kafka is optional and gracefully degrades when unavailable.
- This is an educational simulation designed for local development.
- For heavy stress tests, use realistic hardware limits and run in smaller batches.
