package com.gridweaver;

import com.gridweaver.engine.MicrogridStateEngine;
import com.gridweaver.model.Device;
import com.gridweaver.model.DeviceType;

/** Zero-dependency smoke tests for quick verification without Maven/JUnit. */
public final class GridWeaverSelfTest {
    public static void main(String[] args) throws Exception {
        testGridImport();
        testBatteryContribution();
        testAsyncUpdate();
        System.out.println("SELF-TEST: ALL 3 TESTS PASSED");
    }

    private static void testGridImport() {
        try (MicrogridStateEngine e = new MicrogridStateEngine()) {
            e.register(new Device("solar", DeviceType.SOLAR_PANEL, 10));
            e.register(new Device("load", DeviceType.LOAD, 16));
            var s = e.snapshot();
            check(Math.abs(s.gridImportKw() - 6) < 0.001, "grid import calculation");
            check("GRID_IMPORT".equals(s.status()), "grid import status");
        }
    }

    private static void testBatteryContribution() {
        try (MicrogridStateEngine e = new MicrogridStateEngine()) {
            e.register(new Device("solar", DeviceType.SOLAR_PANEL, 10));
            e.register(new Device("battery", DeviceType.BATTERY, 3));
            e.register(new Device("load", DeviceType.LOAD, 12));
            var s = e.snapshot();
            check(Math.abs(s.balanceKw() - 1) < 0.001, "battery contribution");
            check(s.gridImportKw() == 0, "no grid import with battery");
        }
    }

    private static void testAsyncUpdate() throws InterruptedException {
        try (MicrogridStateEngine e = new MicrogridStateEngine()) {
            e.register(new Device("solar", DeviceType.SOLAR_PANEL, 5));
            e.submitSensorUpdate("solar", 9);
            Thread.sleep(100);
            check(Math.abs(e.snapshot().generationKw() - 9) < 0.001, "async sensor update");
        }
    }

    private static void check(boolean condition, String name) {
        if (!condition) throw new AssertionError("FAILED: " + name);
        System.out.println("PASS: " + name);
    }
}
