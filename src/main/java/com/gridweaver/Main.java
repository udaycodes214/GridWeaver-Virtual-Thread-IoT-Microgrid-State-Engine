package com.gridweaver;

import com.gridweaver.engine.MicrogridSnapshot;
import com.gridweaver.engine.MicrogridStateEngine;
import com.gridweaver.engine.VirtualThreadBenchmark;
import com.gridweaver.model.Device;
import com.gridweaver.model.DeviceType;

public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("=== GridWeaver: Virtual Thread IoT Microgrid State Engine ===");
        try (MicrogridStateEngine engine = new MicrogridStateEngine()) {
            engine.register(new Device("solar-01", DeviceType.SOLAR_PANEL, 18.5));
            engine.register(new Device("solar-02", DeviceType.SOLAR_PANEL, 12.0));
            engine.register(new Device("battery-01", DeviceType.BATTERY, 5.0));
            engine.register(new Device("load-01", DeviceType.LOAD, 20.0));
            engine.register(new Device("load-02", DeviceType.LOAD, 11.0));

            System.out.println(engine.healthReport());

            engine.submitSensorUpdate("solar-02", 14.0);
            engine.submitSensorUpdate("load-02", 8.0);
            Thread.sleep(100);
            System.out.println("After asynchronous IoT updates:");
            System.out.println(engine.healthReport());

            MicrogridSnapshot s = engine.snapshot();
            System.out.printf("Snapshot -> generation %.2fkW | load %.2fkW | balance %.2fkW | grid import %.2fkW | status %s%n",
                    s.generationKw(), s.loadKw(), s.balanceKw(), s.gridImportKw(), s.status());

            if (args.length > 0 && args[0].equalsIgnoreCase("benchmark")) {
                System.out.println();
                System.out.println(VirtualThreadBenchmark.run(1000, 20));
            }
                        if (args.length > 0 && args[0].equalsIgnoreCase("server")) {
                WebServer.start(engine);

                System.out.println();
                System.out.println("======================================");
                System.out.println(" GridWeaver Backend is LIVE");
                System.out.println(" API: http://localhost:8080/api/grid");
                System.out.println(" Health: http://localhost:8080/api/health");
                System.out.println("======================================");
                System.out.println();
                System.out.println("Keep this terminal running.");
                System.out.println("Press Ctrl+C to stop the backend.");

                Thread.currentThread().join();
            }
        }
    }
}
