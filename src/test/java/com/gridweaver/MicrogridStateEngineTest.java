package com.gridweaver;

import com.gridweaver.engine.MicrogridStateEngine;
import com.gridweaver.model.Device;
import com.gridweaver.model.DeviceType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MicrogridStateEngineTest {
    @Test
    void computesGridImportWhenLoadExceedsSupply() {
        try (MicrogridStateEngine engine = new MicrogridStateEngine()) {
            engine.register(new Device("solar", DeviceType.SOLAR_PANEL, 10));
            engine.register(new Device("load", DeviceType.LOAD, 16));
            var s = engine.snapshot();
            assertEquals(-6, s.balanceKw(), 0.001);
            assertEquals(6, s.gridImportKw(), 0.001);
            assertEquals("GRID_IMPORT", s.status());
        }
    }

    @Test
    void handlesBatteryContribution() {
        try (MicrogridStateEngine engine = new MicrogridStateEngine()) {
            engine.register(new Device("solar", DeviceType.SOLAR_PANEL, 10));
            engine.register(new Device("battery", DeviceType.BATTERY, 3));
            engine.register(new Device("load", DeviceType.LOAD, 12));
            var s = engine.snapshot();
            assertEquals(1, s.balanceKw(), 0.001);
            assertEquals(0, s.gridImportKw(), 0.001);
            assertEquals("SURPLUS", s.status());
        }
    }
}
