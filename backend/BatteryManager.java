public class BatteryManager {

    public static BatteryState calculateState(double battery, double solarPower) {

        if (battery < 30) {
            return BatteryState.FAULT;
        }

        if (solarPower > 7 && battery < 95) {
            return BatteryState.CHARGING;
        }

        if (battery < 50) {
            return BatteryState.DISCHARGING;
        }

        return BatteryState.IDLE;
    }
}