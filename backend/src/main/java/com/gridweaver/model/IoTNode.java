package com.gridweaver.model;

import java.time.Instant;

public class IoTNode {
    private String nodeId;
    private double latitude;
    private double longitude;
    private double batteryPercentage;
    private double solarPower;
    private double powerDemand;
    private BatteryState state;
    private String zone;
    private Instant lastUpdated;

    public IoTNode() {
    }

    public IoTNode(String nodeId, double latitude, double longitude, double batteryPercentage,
            double solarPower, double powerDemand, BatteryState state, String zone) {
        this.nodeId = nodeId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.batteryPercentage = batteryPercentage;
        this.solarPower = solarPower;
        this.powerDemand = powerDemand;
        this.state = state;
        this.zone = zone;
        this.lastUpdated = Instant.now();
    }

    public String getNodeId() { return nodeId; }
    public void setNodeId(String nodeId) { this.nodeId = nodeId; }
    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }
    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }
    public double getBatteryPercentage() { return batteryPercentage; }
    public void setBatteryPercentage(double batteryPercentage) { this.batteryPercentage = batteryPercentage; }
    public double getSolarPower() { return solarPower; }
    public void setSolarPower(double solarPower) { this.solarPower = solarPower; }
    public double getPowerDemand() { return powerDemand; }
    public void setPowerDemand(double powerDemand) { this.powerDemand = powerDemand; }
    public BatteryState getState() { return state; }
    public void setState(BatteryState state) { this.state = state; }
    public String getZone() { return zone; }
    public void setZone(String zone) { this.zone = zone; }
    public Instant getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(Instant lastUpdated) { this.lastUpdated = lastUpdated; }
}
