# GridWeaver — Virtual Thread IoT Microgrid State Engine

## 1. Project Overview
GridWeaver is a Java 21 concurrency-focused microgrid simulator. It models IoT devices such as solar panels, batteries and electrical loads, receives asynchronous sensor updates using **virtual threads**, and continuously derives the current microgrid state.

## 2. Objectives
- Apply modern Java development practices and OOP.
- Demonstrate Java 21 virtual threads for high-concurrency sensor updates.
- Maintain thread-safe device state with `ConcurrentHashMap` and `AtomicReference`.
- Calculate generation, load, battery contribution, balance and grid import.
- Provide automated tests for the core state engine.
- Provide a repeatable benchmark for platform threads vs virtual threads.

## 3. Architecture
`IoT Device -> Virtual Thread Sensor Update -> Thread-safe Device State -> Microgrid State Engine -> Snapshot / Health Report`

## 4. Technology Stack
- Java 21
- Maven
- JUnit 5
- Java Virtual Threads (`Executors.newVirtualThreadPerTaskExecutor()`)
- Java Concurrency (`ConcurrentHashMap`, `AtomicReference`)

## 5. Project Structure
```text
src/main/java/com/gridweaver/
  Main.java
  model/
    Device.java
    DeviceType.java
  engine/
    MicrogridSnapshot.java
    MicrogridStateEngine.java
    VirtualThreadBenchmark.java
src/test/java/com/gridweaver/
  MicrogridStateEngineTest.java
```

## 6. Run
Prerequisite: JDK 21+ and Maven.

```bash
mvn clean test
mvn exec:java
mvn exec:java -Dexec.args=benchmark
```

On Windows PowerShell, if Maven is installed as `mvn` these commands are identical. If using a Maven wrapper, run `./mvnw.cmd clean test` and `./mvnw.cmd exec:java`.

## 7. Sample Behaviour
The demo registers solar, battery and load devices, submits asynchronous sensor updates, then prints a calculated microgrid snapshot. The engine automatically reports grid import when demand exceeds available supply.

## 8. Evaluation
The benchmark compares a fixed platform-thread pool with Java 21 virtual threads for many short I/O-like tasks. Results vary by machine, OS and JDK. Run the benchmark locally and record the observed timings in the final presentation/report.

## 9. Scope Note
This submission is intentionally focused on the assigned Java development and concurrency problem. A persistent database and external API are not required for the simulator's core objective; they can be added as future extensions through Spring Boot, PostgreSQL and MQTT/REST integration.
