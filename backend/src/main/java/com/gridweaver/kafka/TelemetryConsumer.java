package com.gridweaver.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gridweaver.model.BatteryState;
import com.gridweaver.model.IoTNode;
import com.gridweaver.service.NodeService;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class TelemetryConsumer {
    private final NodeService nodeService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public TelemetryConsumer(NodeService nodeService) {
        this.nodeService = nodeService;
    }

    public void consume(String payload) {
        try {
            Map<String, Object> msg = objectMapper.readValue(payload, Map.class);
            String nodeId = (String) msg.get("nodeId");
            IoTNode node = nodeService.getNodeById(nodeId);
            if (node == null) {
                return;
            }
            node.setBatteryPercentage(((Number) msg.getOrDefault("batteryPercentage", node.getBatteryPercentage())).doubleValue());
            node.setSolarPower(((Number) msg.getOrDefault("solarPower", node.getSolarPower())).doubleValue());
            node.setPowerDemand(((Number) msg.getOrDefault("powerDemand", node.getPowerDemand())).doubleValue());
            node.setState(BatteryState.valueOf((String) msg.getOrDefault("state", node.getState().name())));
        } catch (Exception ignored) {
            // Kafka may be unavailable or message parsing may fail. The app should keep running.
        }
    }
}
