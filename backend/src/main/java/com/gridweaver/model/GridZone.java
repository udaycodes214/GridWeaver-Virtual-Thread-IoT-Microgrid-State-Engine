package com.gridweaver.model;

public class GridZone {
    private String zoneName;
    private int nodeCount;
    private double generation;
    private double demand;
    private double balance;
    private String status;

    public GridZone() {
    }

    public GridZone(String zoneName, int nodeCount, double generation, double demand, double balance, String status) {
        this.zoneName = zoneName;
        this.nodeCount = nodeCount;
        this.generation = generation;
        this.demand = demand;
        this.balance = balance;
        this.status = status;
    }

    public String getZoneName() { return zoneName; }
    public void setZoneName(String zoneName) { this.zoneName = zoneName; }
    public int getNodeCount() { return nodeCount; }
    public void setNodeCount(int nodeCount) { this.nodeCount = nodeCount; }
    public double getGeneration() { return generation; }
    public void setGeneration(double generation) { this.generation = generation; }
    public double getDemand() { return demand; }
    public void setDemand(double demand) { this.demand = demand; }
    public double getBalance() { return balance; }
    public void setBalance(double balance) { this.balance = balance; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
