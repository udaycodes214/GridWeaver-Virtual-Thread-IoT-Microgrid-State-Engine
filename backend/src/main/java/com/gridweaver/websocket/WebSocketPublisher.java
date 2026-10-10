package com.gridweaver.websocket;

import com.gridweaver.model.GridZone;
import com.gridweaver.model.IoTNode;
import com.gridweaver.service.GridBalancingService;
import java.util.List;
import java.util.Map;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class WebSocketPublisher {
    private final SimpMessagingTemplate messagingTemplate;
    private final GridBalancingService gridBalancingService;

    public WebSocketPublisher(SimpMessagingTemplate messagingTemplate, GridBalancingService gridBalancingService) {
        this.messagingTemplate = messagingTemplate;
        this.gridBalancingService = gridBalancingService;
    }

    public void publishNodes(List<IoTNode> nodes) {
        messagingTemplate.convertAndSend("/topic/nodes", nodes);
    }

    public void publishStats(Map<String, Object> stats) {
        messagingTemplate.convertAndSend("/topic/stats", stats);
    }

    public void publishEvents(List<String> events) {
        messagingTemplate.convertAndSend("/topic/events", events);
    }

    public void publishGrid(List<IoTNode> nodes) {
        List<GridZone> zones = gridBalancingService.buildZones(nodes);
        messagingTemplate.convertAndSend("/topic/grid", zones);
    }

    public void publishStormStatus(boolean enabled) {
        messagingTemplate.convertAndSend("/topic/stats", Map.of("stormMode", enabled));
    }
}
