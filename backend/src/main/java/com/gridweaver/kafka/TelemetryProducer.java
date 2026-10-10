package com.gridweaver.kafka;

import com.gridweaver.model.IoTNode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class TelemetryProducer {
    private static final Logger log = LoggerFactory.getLogger(TelemetryProducer.class);
    private static final String TOPIC = "iot-telemetry";
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public TelemetryProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publish(IoTNode node) {
        try {
            String json = objectMapper.writeValueAsString(Map.of(
                    "nodeId", node.getNodeId(),
                    "batteryPercentage", node.getBatteryPercentage(),
                    "solarPower", node.getSolarPower(),
                    "powerDemand", node.getPowerDemand(),
                    "state", node.getState(),
                    "zone", node.getZone()));
            kafkaTemplate.send(TOPIC, node.getNodeId(), json);
        } catch (JsonProcessingException e) {
            log.warn("Unable to encode telemetry for node {}", node.getNodeId(), e);
        } catch (Exception e) {
            log.warn("Kafka is unavailable. Continuing without Kafka publishing for node {}", node.getNodeId(), e);
        }
    }
}
