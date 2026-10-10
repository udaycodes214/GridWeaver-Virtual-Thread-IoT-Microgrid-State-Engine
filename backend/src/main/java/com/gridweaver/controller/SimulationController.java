package com.gridweaver.controller;

import com.gridweaver.model.GridZone;
import com.gridweaver.model.IoTNode;
import com.gridweaver.service.GridBalancingService;
import com.gridweaver.service.NodeService;
import com.gridweaver.service.SimulationService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/", "/api"})
public class SimulationController {
    private final SimulationService simulationService;
    private final NodeService nodeService;
    private final GridBalancingService gridBalancingService;

    public SimulationController(SimulationService simulationService, NodeService nodeService,
            GridBalancingService gridBalancingService) {
        this.simulationService = simulationService;
        this.nodeService = nodeService;
        this.gridBalancingService = gridBalancingService;
    }

    @GetMapping({"/", ""})
    public Map<String, String> home() {
        return Map.of("service", "GridWeaver", "status", "running");
    }

    @GetMapping("/health")
    public Map<String, Object> health() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("simulationRunning", nodeService.isSimulationRunning());
        response.put("stormMode", nodeService.isStormModeEnabled());
        response.put("totalNodes", nodeService.getNodes().size());
        response.put("timestamp", System.currentTimeMillis());
        return response;
    }

    @PostMapping("/simulator/start")
    public ResponseEntity<Map<String, Object>> startSimulation(@RequestParam(defaultValue = "100") int count) {
        if (count < 1 || count > 50000) {
            return ResponseEntity.badRequest().body(Map.of("status", "error", "message", "Node count must be between 1 and 50000"));
        }
        try {
            simulationService.startSimulation(count);
            return ResponseEntity.ok(Map.of("status", "started", "count", count));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(Map.of("status", "error", "message", ex.getMessage()));
        }
    }

    @PostMapping("/simulator/stop")
    public Map<String, String> stopSimulation() {
        simulationService.stopSimulation();
        return Map.of("status", "stopped");
    }

    @PostMapping("/simulator/storm")
    public Map<String, Object> setStorm(@RequestParam boolean enabled) {
        simulationService.setStormMode(enabled);
        return Map.of("status", enabled ? "storm enabled" : "storm disabled", "stormMode", enabled);
    }

    @GetMapping("/grid/zones")
    public List<GridZone> getZones() {
        return gridBalancingService.buildZones(nodeService.getNodes());
    }

    @GetMapping("/grid/summary")
    public Map<String, Object> getGridSummary() {
        List<IoTNode> nodes = nodeService.getNodes();
        List<GridZone> zones = gridBalancingService.buildZones(nodes);
        Map<String, Object> summary = new HashMap<>();
        summary.put("overallStatus", gridBalancingService.summarize(nodes));
        summary.put("zones", zones);
        summary.put("totalGeneration", zones.stream().mapToDouble(GridZone::getGeneration).sum());
        summary.put("totalDemand", zones.stream().mapToDouble(GridZone::getDemand).sum());
        return summary;
    }
}
