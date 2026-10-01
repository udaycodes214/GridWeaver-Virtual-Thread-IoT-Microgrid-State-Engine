package com.gridweaver.engine;

public record MicrogridSnapshot(
        long timestamp,
        double generationKw,
        double loadKw,
        double batteryPowerKw,
        double gridImportKw,
        double balanceKw,
        int onlineDevices
) {
    public String status() {
        if (gridImportKw > 0.01) return "GRID_IMPORT";
        if (balanceKw > 0.01) return "SURPLUS";
        return "BALANCED";
    }
}
