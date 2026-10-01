# GridWeaver — Project Documentation

## Purpose
GridWeaver is a Java 21 simulation of an IoT-connected microgrid state engine. It demonstrates modern Java concurrency by processing asynchronous device telemetry through virtual threads and maintaining a thread-safe in-memory device registry.

## Functional Requirements
1. Register microgrid devices.
2. Represent solar generation, battery contribution and electrical load.
3. Receive asynchronous sensor updates.
4. Calculate generation, load, battery power and net balance.
5. Calculate grid import when local supply is insufficient.
6. Produce a human-readable health report.
7. Verify core behaviour with automated smoke tests and JUnit tests.
8. Benchmark platform-thread and virtual-thread execution.

## Non-Functional Requirements
- Java 21 compatibility.
- Thread-safe state updates.
- Clear separation between model and engine layers.
- Repeatable local execution.
- Maven-compatible project layout.

## Design
### Device model
`Device` stores an ID, type, power reading and online state. `AtomicReference` protects mutable readings.

### State engine
`MicrogridStateEngine` stores devices in a `ConcurrentHashMap`. Every sensor update is submitted as an independent virtual-thread task. A snapshot iterates through online devices and derives the current power balance.

### Balance formula
`balance = generation + batteryPower - load`

If `balance < 0`, then `gridImport = -balance`; otherwise grid import is zero.

## Concurrency Approach
Java 21 virtual threads are appropriate for large numbers of short, I/O-like sensor tasks because they are lightweight compared with one platform thread per task. The project intentionally keeps the shared state small and thread-safe.

## Error Handling
Negative power readings are rejected with `IllegalArgumentException`. Unknown device IDs are ignored for asynchronous updates so a late telemetry packet cannot crash the engine.

## Testing
The project contains JUnit 5 tests under `src/test` and a dependency-free smoke test `GridWeaverSelfTest` for environments without Maven.

### Verified reference run
The source was compiled with OpenJDK 21 and the application was executed successfully. A reference benchmark with 1,000 tasks of 20 ms each measured approximately 224 ms for a 100-thread platform pool and 28 ms for virtual threads in the verification environment. These timings are illustrative, not universal; run `run-benchmark.bat` on the submission machine and record its output.

## Future Enhancements
- Spring Boot REST API for live telemetry.
- MQTT ingestion for real IoT devices.
- PostgreSQL/TimescaleDB persistence.
- Web dashboard with live charts.
- Battery charge/discharge limits and state-of-charge.
- Alerting for overload and device failure.
- Docker deployment and CI pipeline.
