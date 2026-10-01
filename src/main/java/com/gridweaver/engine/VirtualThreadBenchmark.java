package com.gridweaver.engine;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public final class VirtualThreadBenchmark {
    private VirtualThreadBenchmark() {}

    public static String run(int tasks, int sleepMs) throws InterruptedException {
        long platform = runWith(Executors.newFixedThreadPool(Math.min(100, tasks)), tasks, sleepMs);
        long virtual = runWith(Executors.newVirtualThreadPerTaskExecutor(), tasks, sleepMs);
        return "Benchmark: " + tasks + " I/O-like tasks x " + sleepMs + "ms\n" +
                "Platform-thread pool: " + platform + " ms\n" +
                "Virtual threads:      " + virtual + " ms\n" +
                "Lower wall-clock time is better for this concurrency-oriented workload.";
    }

    private static long runWith(ExecutorService executor, int tasks, int sleepMs) throws InterruptedException {
        long start = System.nanoTime();
        for (int i = 0; i < tasks; i++) {
            executor.submit(() -> {
                try { Thread.sleep(sleepMs); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            });
        }
        executor.shutdown();
        executor.awaitTermination(60, TimeUnit.SECONDS);
        return TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);
    }
}
