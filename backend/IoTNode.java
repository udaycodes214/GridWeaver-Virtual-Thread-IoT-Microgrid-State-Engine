public class IoTNode {

    String nodeId;
    double battery;
    double solarPower;
    BatteryState state;

    public IoTNode(String nodeId, double battery, double solarPower) {
        this.nodeId = nodeId;
        this.battery = battery;
        this.solarPower = solarPower;

        this.state = BatteryManager.calculateState(
                battery,
                solarPower
        );
    }

    public void update() {

        BatteryState oldState = state;

        state = BatteryManager.calculateState(
                battery,
                solarPower
        );

        if (oldState != state) {
            System.out.println(
                    nodeId + " : " +
                    oldState + " -> " +
                    state
            );
        }
    }

    public void displayNode() {

        System.out.println(
                "Node: " + nodeId +
                " | Battery: " + battery + "%" +
                " | Solar: " + solarPower + " kW" +
                " | State: " + state
        );
    }
}