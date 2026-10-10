public class Main {

    public static void main(String[] args) {

        System.out.println("================================");
        System.out.println("       GRIDWEAVER STARTED");
        System.out.println("================================");

        int totalNodes = 1000;

        for (int i = 1; i <= totalNodes; i++) {

            int nodeNumber = i;

            Thread.startVirtualThread(() -> {

                String nodeId =
                        String.format("SOLAR-%04d", nodeNumber);

                double battery =
                        20 + (nodeNumber % 81);

                double solarPower =
                        2 + (nodeNumber % 9);

                IoTNode node =
                        new IoTNode(
                                nodeId,
                                battery,
                                solarPower
                        );

                node.displayNode();

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

                node.battery =
                        Math.max(0, node.battery - 5);

                node.update();
            });
        }

        System.out.println(
                totalNodes + " Virtual Threads Started"
        );

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("================================");
        System.out.println("       GRIDWEAVER FINISHED");
        System.out.println("================================");
    }
}