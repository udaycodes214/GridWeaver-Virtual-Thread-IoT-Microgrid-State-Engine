package com.gridweaver.model;

import java.time.Instant;

public class TelemetryEvent {
    private String nodeId;
    private Instant timestamp;
    private double solarPower;
    private double powerDemand;
    private double batteryPercentage;
    private BatteryState state;

    public TelemetryEvent() {
    }

    public TelemetryEvent(String nodeId, Instant timestamp, double solarPower, double powerDemand,
            double batteryPercentage, BatteryState state) {
        this.nodeId = nodeId;
        this.timestamp = timestamp;
        this.solarPower = solarPower;
        this.powerDemand = powerDemand;
        this.batteryPercentage = batteryPercentage;
        this.state = state;
    }

    public String getNodeId() { return nodeId; }
    public void setNodeId(String nodeId) { this.nodeId = nodeId; }
    public Instant getTimestamp() { return timestamp; }
    public void setTimestamp(Instant timestamp) { this.timestamp = timestamp; }
    public double getSolarPower() { return solarPower; }
    public void setSolarPower(double solarPower) { this.solarPower = solarPower; }
    public double getPowerDemand() { return powerDemand; }
    public void setPowerDemand(double powerDemand) { this.powerDemand = powerDemand; }
    public double getBatteryPercentage() { return batteryPercentage; }
    public void setBatteryPercentage(double batteryPercentage) { this.batteryPercentage = batteryPercentage; }
    public BatteryState getState() { return state; }
    public void setState(BatteryState state) { this.state = state; }
}
