# Architecture

GridWeaver uses a layered architecture:

- Simulator layer: virtual-thread-based node generation and periodic telemetry updates
- Service layer: node, grid, and event management
- State machine layer: battery transitions based on renewable generation and demand
- Kafka layer: optional telemetry publish/consume path
- WebSocket layer: real-time push to the React frontend
- Frontend layer: dashboard, map, event log, and controls
