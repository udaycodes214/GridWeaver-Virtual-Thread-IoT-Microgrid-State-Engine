package com.gridweaver.controller;

import com.gridweaver.model.IoTNode;
import com.gridweaver.service.NodeService;
import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class NodeController {
    private final NodeService nodeService;

    public NodeController(NodeService nodeService) {
        this.nodeService = nodeService;
    }

    @GetMapping("/nodes")
    public List<IoTNode> getNodes() {
        return nodeService.getNodes();
    }

    @GetMapping("/nodes/{nodeId}")
    public ResponseEntity<IoTNode> getNode(@PathVariable String nodeId) {
        IoTNode node = nodeService.getNodeById(nodeId);
        if (node == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(node);
    }

    @GetMapping("/nodes/stats")
    public Map<String, Object> stats() {
        return nodeService.getStats();
    }

    @GetMapping("/events")
    public List<String> events() {
        return nodeService.getEvents();
    }
}
