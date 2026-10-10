package com.gridweaver.service;

import com.gridweaver.model.IoTNode;
import com.gridweaver.websocket.WebSocketPublisher;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import org.springframework.stereotype.Service;

@Service
public class SimulationService {

    private final NodeService nodeService;
    private final WebSocketPublisher webSocketPublisher;
    private final ExecutorService virtualExecutor;
    private final AtomicBoolean running = new AtomicBoolean(false);

    public SimulationService(
            NodeService nodeService,
            WebSocketPublisher webSocketPublisher) {

        this.nodeService = nodeService;
        this.webSocketPublisher = webSocketPublisher;
        this.virtualExecutor = Executors.newVirtualThreadPerTaskExecutor();
    }

    public synchronized void startSimulation(int count) {

        // Stop the previous simulation before starting a new one.
        running.set(false);

        // Give existing virtual-thread tasks a moment to exit their loop.
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Create the requested number of nodes.
        nodeService.startSimulation(count);

        // Start the new simulation.
        running.set(true);

        List<IoTNode> nodes = nodeService.getNodes();

        // Publish the initial state immediately.
        webSocketPublisher.publishNodes(nodes);
        webSocketPublisher.publishStats(nodeService.getStats());
        webSocketPublisher.publishEvents(nodeService.getEvents());
        webSocketPublisher.publishGrid(nodes);

        // One virtual thread per IoT node.
        for (IoTNode node : nodes) {

            virtualExecutor.submit(() -> {

                while (running.get()) {

                    try {
                        Thread.sleep(
                                900 + (long) (Math.random() * 1200)
                        );
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }

                    if (!running.get()) {
                        break;
                    }

                    nodeService.updateNodeFromTick(node);

                    webSocketPublisher.publishNodes(
                            nodeService.getNodes()
                    );

                    webSocketPublisher.publishStats(
                            nodeService.getStats()
                    );

                    webSocketPublisher.publishEvents(
                            nodeService.getEvents()
                    );

                    webSocketPublisher.publishGrid(
                            nodeService.getNodes()
                    );
                }
            });
        }
    }

    public synchronized void stopSimulation() {

        running.set(false);
        nodeService.stopSimulation();

        webSocketPublisher.publishNodes(nodeService.getNodes());
        webSocketPublisher.publishStats(nodeService.getStats());
        webSocketPublisher.publishEvents(nodeService.getEvents());
        webSocketPublisher.publishGrid(nodeService.getNodes());
    }

    public void setStormMode(boolean enabled) {

        nodeService.setStormMode(enabled);
        webSocketPublisher.publishStormStatus(enabled);
    }

    public void healthCheck() {

        webSocketPublisher.publishStats(nodeService.getStats());
    }

    public void shutdown() {

        stopSimulation();

        virtualExecutor.shutdown();

        try {

            if (!virtualExecutor.awaitTermination(
                    5,
                    TimeUnit.SECONDS)) {

                virtualExecutor.shutdownNow();
            }

        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();
            virtualExecutor.shutdownNow();
        }
    }
}