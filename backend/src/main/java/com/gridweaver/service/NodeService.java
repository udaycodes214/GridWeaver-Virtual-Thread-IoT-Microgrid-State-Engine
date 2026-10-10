package com.gridweaver.service;

import com.gridweaver.model.BatteryState;
import com.gridweaver.model.IoTNode;
import com.gridweaver.model.TelemetryEvent;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Service;

@Service
public class NodeService {
    private final Map<String, IoTNode> nodes = new ConcurrentHashMap<>();
    private final EventLogService eventLogService;
    private final AtomicBoolean stormMode = new AtomicBoolean(false);
    private final AtomicBoolean simulationRunning = new AtomicBoolean(false);
    private final AtomicLong eventsGenerated = new AtomicLong();
    private final AtomicLong eventsProcessed = new AtomicLong();

    public NodeService(EventLogService eventLogService) {
        this.eventLogService = eventLogService;
    }

    public synchronized void startSimulation(int count) {
        if (count < 1 || count > 50000) {
            throw new IllegalArgumentException("Node count must be between 1 and 50000");
        }
        nodes.clear();
        eventLogService.clear();
        for (int i = 1; i <= count; i++) {
            String nodeId = String.format("SOLAR-%04d", i);
            double latitude = 28.40 + ((i % 12) * 0.10);
            double longitude = 77.30 + ((i % 10) * 0.08);
            double battery = 35 + (i % 65);
            double solar = 2 + (i % 8) + ((i % 5) * 0.2);
            double demand = 1.5 + (i % 6) * 0.8;
            BatteryState state = BatteryState.IDLE;
            String zone = zoneFor(latitude, longitude, i);
            IoTNode node = new IoTNode(nodeId, latitude, longitude, battery, solar, demand, state, zone);
            nodes.put(nodeId, node);
        }
        simulationRunning.set(true);
    }

    public void stopSimulation() {
        simulationRunning.set(false);
    }

    public boolean isSimulationRunning() {
        return simulationRunning.get();
    }

    public void setStormMode(boolean enabled) {
        stormMode.set(enabled);
    }

    public boolean isStormModeEnabled() {
        return stormMode.get();
    }

    public List<IoTNode> getNodes() {
        return new ArrayList<>(nodes.values());
    }

    public IoTNode getNodeById(String nodeId) {
        return nodes.get(nodeId);
    }

    public List<String> getEvents() {
        return eventLogService.getRecentEvents();
    }

    public Map<String, Object> getStats() {
        List<IoTNode> allNodes = getNodes();
        long charging = allNodes.stream().filter(n -> n.getState() == BatteryState.CHARGING).count();
        long discharging = allNodes.stream().filter(n -> n.getState() == BatteryState.DISCHARGING).count();
        long faults = allNodes.stream().filter(n -> n.getState() == BatteryState.FAULT).count();
        double solar = allNodes.stream().mapToDouble(IoTNode::getSolarPower).sum();
        double demand = allNodes.stream().mapToDouble(IoTNode::getPowerDemand).sum();
        Map<String, Object> stats = new java.util.HashMap<>();
        stats.put("totalNodes", allNodes.size());
        stats.put("onlineNodes", allNodes.size());
        stats.put("charging", charging);
        stats.put("discharging", discharging);
        stats.put("faults", faults);
        stats.put("totalSolarGeneration", solar);
        stats.put("totalDemand", demand);
        stats.put("stormMode", stormMode.get());
        stats.put("eventsGenerated", eventsGenerated.get());
        stats.put("eventsProcessed", eventsProcessed.get());
        return stats;
    }

    public void recordTelemetry(IoTNode node) {
        TelemetryEvent event = new TelemetryEvent(
                node.getNodeId(),
                Instant.now(),
                node.getSolarPower(),
                node.getPowerDemand(),
                node.getBatteryPercentage(),
                node.getState());
        eventsGenerated.incrementAndGet();
        eventsProcessed.incrementAndGet();
        node.setLastUpdated(event.getTimestamp());
        String message = String.format("%s | %s -> %s | %.1f kW | %.1f kW",
                node.getNodeId(),
                previousState(node),
                node.getState(),
                node.getSolarPower(),
                node.getPowerDemand());
        eventLogService.addEvent(message);
    }

    public void updateNodeFromTick(IoTNode node) {
        if (Objects.isNull(node)) {
            return;
        }
        double solar = node.getSolarPower();
        double demand = node.getPowerDemand();
        if (stormMode.get()) {
            solar *= 0.45;
            demand *= 1.3;
        }
        double battery = node.getBatteryPercentage();
        double energyDelta = solar - demand;
        if (energyDelta > 0.8) {
            battery = Math.min(100, battery + 4.5);
        } else if (energyDelta < -0.5) {
            battery = Math.max(0, battery - 5.5);
        }

        BatteryState previous = node.getState();
        if (battery <= 8 || (Math.random() < 0.01)) {
            node.setState(BatteryState.FAULT);
        } else if (solar > demand + 1.5) {
            node.setState(BatteryState.CHARGING);
        } else if (demand > solar + 1.25) {
            node.setState(BatteryState.DISCHARGING);
        } else {
            node.setState(BatteryState.IDLE);
        }

        node.setBatteryPercentage(battery);
        node.setSolarPower(solar);
        node.setPowerDemand(demand);
        node.setLastUpdated(Instant.now());

        if (previous != node.getState()) {
            eventLogService.addEvent(node.getNodeId() + " " + previous + " -> " + node.getState());
        }
        recordTelemetry(node);
    }

    private String previousState(IoTNode node) {
        return node.getState() == null ? "IDLE" : node.getState().name();
    }

    private String zoneFor(double latitude, double longitude, int index) {
        if (latitude > 28.6) return "ZONE-A";
        if (longitude > 77.5) return "ZONE-B";
        if (latitude < 28.45) return "ZONE-C";
        if (longitude > 77.4) return "ZONE-D";
        return index % 2 == 0 ? "ZONE-E" : "ZONE-A";
    }
}
