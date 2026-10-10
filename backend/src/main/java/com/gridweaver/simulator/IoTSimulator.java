package com.gridweaver.simulator;

import com.gridweaver.model.BatteryState;
import com.gridweaver.model.IoTNode;
import com.gridweaver.service.NodeService;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.springframework.stereotype.Component;

@Component
public class IoTSimulator {
    private final NodeService nodeService;
    private final ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();

    public IoTSimulator(NodeService nodeService) {
        this.nodeService = nodeService;
    }

    public void loadSimulation(int count) {
        nodeService.startSimulation(count);
        List<IoTNode> nodes = nodeService.getNodes();
        for (IoTNode node : nodes) {
            executor.submit(() -> {
                while (nodeService.isSimulationRunning()) {
                    try {
                        Thread.sleep(1000L + (long) (Math.random() * 800L));
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                    nodeService.updateNodeFromTick(node);
                }
            });
        }
    }

    public void stop() {
        nodeService.stopSimulation();
    }

    public static BatteryState calculateState(double battery, double solarPower, double demand, boolean stormMode) {
        if (battery <= 8) {
            return BatteryState.FAULT;
        }
        if (stormMode && solarPower < 1.0) {
            return BatteryState.DISCHARGING;
        }
        if (solarPower > demand + 1.5 && battery < 95) {
            return BatteryState.CHARGING;
        }
        if (demand > solarPower + 1.0) {
            return BatteryState.DISCHARGING;
        }
        return BatteryState.IDLE;
    }
}
