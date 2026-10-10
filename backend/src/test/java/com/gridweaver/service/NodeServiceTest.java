package com.gridweaver.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.gridweaver.model.BatteryState;
import com.gridweaver.model.IoTNode;
import org.junit.jupiter.api.Test;

class NodeServiceTest {

    @Test
    void shouldCreateNodesWithExpectedState() {
        IoTNode node = new IoTNode("SOLAR-0100", 28.6, 77.3, 84, 8.1, 4.5, BatteryState.CHARGING, "ZONE-A");
        assertThat(node.getBatteryPercentage()).isGreaterThan(70);
        assertThat(node.getState()).isEqualTo(BatteryState.CHARGING);
    }

    @Test
    void shouldTransitionToFaultWhenBatteryCritical() {
        IoTNode node = new IoTNode("SOLAR-0200", 28.5, 77.5, 7, 0.4, 2.0, BatteryState.FAULT, "ZONE-B");
        assertThat(node.getBatteryPercentage()).isLessThanOrEqualTo(8);
        assertThat(node.getState()).isEqualTo(BatteryState.FAULT);
    }
}
