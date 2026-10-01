package com.gridweaver.engine;

import com.gridweaver.model.Device;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class MicrogridStateEngine implements AutoCloseable {
    private final Map<String, Device> devices = new ConcurrentHashMap<>();
    private final ExecutorService virtualThreads = Executors.newVirtualThreadPerTaskExecutor();

    public void register(Device device) { devices.put(device.id(), device); }
    public List<Device> devices() { return new ArrayList<>(devices.values()); }

    public void submitSensorUpdate(String deviceId, double powerKw) {
        virtualThreads.submit(() -> {
            Device device = devices.get(deviceId);
            if (device != null && device.online()) device.updatePower(powerKw);
        });
    }

    public MicrogridSnapshot snapshot() {
        double generation = 0, load = 0, battery = 0, gridImport = 0;
        int online = 0;
        for (Device d : devices.values()) {
            if (!d.online()) continue;
            online++;
            switch (d.type()) {
                case SOLAR_PANEL -> generation += d.powerKw();
                case LOAD -> load += d.powerKw();
                case BATTERY -> battery += d.powerKw();
                case GRID_IMPORT -> gridImport += d.powerKw();
            }
        }
        double balance = generation + battery - load;
        if (balance < 0) gridImport += -balance;
        return new MicrogridSnapshot(System.currentTimeMillis(), generation, load, battery, gridImport, balance, online);
    }

    public String healthReport() {
        MicrogridSnapshot s = snapshot();
        return "GridWeaver | status=" + s.status() +
                " | generation=" + fmt(s.generationKw()) + "kW" +
                " | load=" + fmt(s.loadKw()) + "kW" +
                " | battery=" + fmt(s.batteryPowerKw()) + "kW" +
                " | gridImport=" + fmt(s.gridImportKw()) + "kW" +
                " | devices=" + s.onlineDevices();
    }

    private static String fmt(double n) { return String.format("%.2f", n); }

    @Override
    public void close() { virtualThreads.close(); }
}
