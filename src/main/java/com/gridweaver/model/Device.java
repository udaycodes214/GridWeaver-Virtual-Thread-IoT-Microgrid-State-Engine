package com.gridweaver.model;

import java.util.concurrent.atomic.AtomicReference;

public final class Device {
    private final String id;
    private final DeviceType type;
    private final AtomicReference<Double> powerKw;
    private final AtomicReference<Boolean> online = new AtomicReference<>(true);

    public Device(String id, DeviceType type, double powerKw) {
        if (powerKw < 0) throw new IllegalArgumentException("Power cannot be negative");
        this.id = id;
        this.type = type;
        this.powerKw = new AtomicReference<>(powerKw);
    }

    public String id() { return id; }
    public DeviceType type() { return type; }
    public double powerKw() { return powerKw.get(); }
    public boolean online() { return online.get(); }
    public void updatePower(double value) {
        if (value < 0) throw new IllegalArgumentException("Power cannot be negative");
        powerKw.set(value);
    }
    public void setOnline(boolean value) { online.set(value); }
}
