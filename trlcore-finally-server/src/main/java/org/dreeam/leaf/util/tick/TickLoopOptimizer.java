package org.dreeam.leaf.util.tick;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Tick loop optimizer that monitors and adjusts server tick behavior.
 * Provides metrics and helpers for maintaining stable TPS.
 * 
 * @author TRLCore Team
 */
public final class TickLoopOptimizer {

    private static final int SAMPLE_SIZE = 100;
    private static final long[] tickSamples = new long[SAMPLE_SIZE];
    private static int sampleIndex = 0;
    private static long tickCount = 0;

    private static final AtomicLong totalTickTime = new AtomicLong(0);
    private static final AtomicLong lastTickStartNanos = new AtomicLong(0);
    private static volatile double averageMspt = 50.0;
    private static volatile double currentTps = 20.0;

    /**
     * Records the start of a tick.
     */
    public static void tickStart() {
        lastTickStartNanos.set(System.nanoTime());
    }

    /**
     * Records the end of a tick and updates metrics.
     */
    public static void tickEnd() {
        long endNanos = System.nanoTime();
        long durationNanos = endNanos - lastTickStartNanos.get();
        long durationMs = durationNanos / 1_000_000;

        // Store sample
        tickSamples[sampleIndex] = durationMs;
        sampleIndex = (sampleIndex + 1) % SAMPLE_SIZE;
        tickCount++;

        // Update totals
        totalTickTime.addAndGet(durationMs);

        // Update averages periodically
        if (tickCount % 20 == 0) {
            updateAverages();
        }

        // Notify adaptive scheduler
        AdaptiveTickScheduler.recordTickDuration(durationMs);
    }

    private static void updateAverages() {
        long sum = 0;
        int count = (int) Math.min(tickCount, SAMPLE_SIZE);
        for (int i = 0; i < count; i++) {
            sum += tickSamples[i];
        }
        averageMspt = count > 0 ? (double) sum / count : 50.0;
        currentTps = Math.min(20.0, 1000.0 / Math.max(averageMspt, 1.0));
    }

    /**
     * Gets current average milliseconds per tick.
     */
    public static double getAverageMspt() {
        return averageMspt;
    }

    /**
     * Gets current TPS (capped at 20).
     */
    public static double getCurrentTps() {
        return currentTps;
    }

    /**
     * Gets the last tick duration in milliseconds.
     */
    public static long getLastTickMs() {
        int lastIndex = (sampleIndex + SAMPLE_SIZE - 1) % SAMPLE_SIZE;
        return tickSamples[lastIndex];
    }

    /**
     * Checks if server should use catch-up ticks.
     */
    public static boolean shouldCatchUp() {
        return averageMspt < 45.0; // Only catch up if we have headroom
    }

    /**
     * Gets recommended sleep time for a tick based on performance.
     */
    public static long getRecommendedSleepNanos(long tickDurationNanos) {
        long targetNanos = 50_000_000L; // 50ms per tick
        long sleepTime = targetNanos - tickDurationNanos;

        if (sleepTime < 0) {
            return 0;
        }

        // If we consistently have spare time, slightly extend sleep to reduce CPU
        if (averageMspt < 40.0 && sleepTime > 1_000_000) {
            sleepTime += 1_000_000; // Add 1ms
        }

        return sleepTime;
    }

    /**
     * Gets total tick count since server start.
     */
    public static long getTotalTickCount() {
        return tickCount;
    }

    /**
     * Resets all metrics.
     */
    public static void reset() {
        sampleIndex = 0;
        tickCount = 0;
        totalTickTime.set(0);
        averageMspt = 50.0;
        currentTps = 20.0;
        for (int i = 0; i < SAMPLE_SIZE; i++) {
            tickSamples[i] = 0;
        }
    }
}
