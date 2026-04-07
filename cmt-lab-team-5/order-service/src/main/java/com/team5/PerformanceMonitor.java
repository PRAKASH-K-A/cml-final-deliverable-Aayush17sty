package com.team5;

import java.util.concurrent.atomic.AtomicLong;

/**
 * PerformanceMonitor - Non-blocking performance metrics collector
 * 
 * This class tracks Tick-to-Trade latency using atomic operations to avoid
 * blocking the order matching engine. It reports average latency every 1000 orders
 * using nanosecond-precision timing.
 * 
 * Lab 9: Performance Engineering and Telemetry
 * - Captures ingress timestamp when FIX order arrives
 * - Captures egress timestamp when ExecutionReport is sent
 * - Records latency = (egressTime - ingressTime) in nanoseconds
 * - Prints aggregated stats to avoid logging overhead
 */
public class PerformanceMonitor {

    private static AtomicLong totalLatency = new AtomicLong(0);
    private static AtomicLong count = new AtomicLong(0);
    private static AtomicLong minLatency = new AtomicLong(Long.MAX_VALUE);
    private static AtomicLong maxLatency = new AtomicLong(0);

    /**
     * Record a single order's Tick-to-Trade latency
     * 
     * Called immediately after sendToTarget() to capture egress latency.
     * Uses atomic operations to avoid mutex locks that would impact throughput.
     * 
     * @param nanos Latency in nanoseconds (egressTime - ingressTime)
     */
    public static void recordLatency(long nanos) {
        totalLatency.addAndGet(nanos);
        
        // Track min and max for distribution analysis
        minLatency.updateAndGet(current -> Math.min(current, nanos));
        maxLatency.updateAndGet(current -> Math.max(current, nanos));
        
        long currentCount = count.incrementAndGet();

        // Print every 1000 orders to reduce console I/O overhead
        if (currentCount % 1000 == 0) {
            double avgMicros = (totalLatency.get() / (double) currentCount) / 1000.0;
            double minMicros = minLatency.get() / 1000.0;
            double maxMicros = maxLatency.get() / 1000.0;
            
            System.out.printf(
                "═══ PERFORMANCE REPORT ═══ Processed %,d orders | Avg: %.2f µs | Min: %.2f µs | Max: %.2f µs%n",
                currentCount, avgMicros, minMicros, maxMicros
            );
        }
    }

    /**
     * Get total orders processed
     * @return count of orders
     */
    public static long getOrderCount() {
        return count.get();
    }

    /**
     * Get average latency in microseconds
     * @return average latency or 0 if no orders
     */
    public static double getAverageLatencyMicros() {
        long totalCount = count.get();
        if (totalCount == 0) return 0;
        return (totalLatency.get() / (double) totalCount) / 1000.0;
    }

    /**
     * Reset all counters (useful for multi-test scenarios)
     */
    public static void reset() {
        totalLatency.set(0);
        count.set(0);
        minLatency.set(Long.MAX_VALUE);
        maxLatency.set(0);
    }

    /**
     * Print summary report
     */
    public static void printSummary() {
        long totalCount = count.get();
        if (totalCount == 0) {
            System.out.println("No orders processed yet.");
            return;
        }

        double avgMicros = (totalLatency.get() / (double) totalCount) / 1000.0;
        double minMicros = minLatency.get() / 1000.0;
        double maxMicros = maxLatency.get() / 1000.0;

        System.out.println("\n╔════════════════════════════════════════════════╗");
        System.out.println("║   TICK-TO-TRADE LATENCY SUMMARY (Lab 9)        ║");
        System.out.println("╠════════════════════════════════════════════════╣");
        System.out.printf("║ Total Orders:      %,15d             ║%n", totalCount);
        System.out.printf("║ Average Latency:   %15.2f µs       ║%n", avgMicros);
        System.out.printf("║ Min Latency:       %15.2f µs       ║%n", minMicros);
        System.out.printf("║ Max Latency:       %15.2f µs       ║%n", maxMicros);
        System.out.println("╚════════════════════════════════════════════════╝\n");
    }
}
