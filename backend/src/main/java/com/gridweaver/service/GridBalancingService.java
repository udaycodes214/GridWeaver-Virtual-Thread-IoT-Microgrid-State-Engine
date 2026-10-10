package com.gridweaver.service;

import com.gridweaver.model.GridZone;
import com.gridweaver.model.IoTNode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class GridBalancingService {

    public List<GridZone> buildZones(List<IoTNode> nodes) {
        Map<String, GridZone> zoneMap = new HashMap<>();
        for (String zoneName : List.of("ZONE-A", "ZONE-B", "ZONE-C", "ZONE-D", "ZONE-E")) {
            zoneMap.put(zoneName, new GridZone(zoneName, 0, 0, 0, 0, "BALANCED"));
        }

        for (IoTNode node : nodes) {
            String zone = node.getZone() == null ? detectZone(node) : node.getZone();
            node.setZone(zone);
            GridZone zoneInfo = zoneMap.getOrDefault(zone, new GridZone(zone, 0, 0, 0, 0, "BALANCED"));
            zoneInfo.setNodeCount(zoneInfo.getNodeCount() + 1);
            zoneInfo.setGeneration(zoneInfo.getGeneration() + node.getSolarPower());
            zoneInfo.setDemand(zoneInfo.getDemand() + node.getPowerDemand());
        }

        List<GridZone> summary = new ArrayList<>();
        for (GridZone zone : zoneMap.values()) {
            double balance = zone.getGeneration() - zone.getDemand();
            zone.setBalance(balance);
            if (balance > 5) {
                zone.setStatus("SURPLUS");
            } else if (balance < -5) {
                zone.setStatus("DEFICIT");
            } else {
                zone.setStatus("BALANCED");
            }
            summary.add(zone);
        }
        return summary;
    }

    public String summarize(List<IoTNode> nodes) {
        List<GridZone> zones = buildZones(nodes);
        double generation = zones.stream().mapToDouble(GridZone::getGeneration).sum();
        double demand = zones.stream().mapToDouble(GridZone::getDemand).sum();
        return generation >= demand ? "SURPLUS" : "DEFICIT";
    }

    private String detectZone(IoTNode node) {
        if (node.getLatitude() >= 28.8) return "ZONE-A";
        if (node.getLongitude() >= 77.5) return "ZONE-B";
        if (node.getLatitude() < 28.4) return "ZONE-C";
        if (node.getLongitude() > 77.3) return "ZONE-D";
        return "ZONE-E";
    }
}
