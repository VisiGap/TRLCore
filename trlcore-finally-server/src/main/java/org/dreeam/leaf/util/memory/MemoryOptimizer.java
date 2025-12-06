package org.dreeam.leaf.util.memory;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Memory usage tracker and optimizer.
 * Monitors heap usage and triggers cleanup when needed.
 * 
 * @author TRLCore Team
 */
public final class MemoryOptimizer {

    private static final Runtime RUNTIME = Runtime.getRuntime();
    private static final AtomicLong lastCleanupTime = new AtomicLong(0);
    private static final long CLEANUP_COOLDOWN_MS = 30_000; // 30 seconds

    // Thresholds (as fraction of max heap)
    private static final double WARNING_THRESHOLD = 0.70; // 70% heap = warning
    private static final double CRITICAL_THRESHOLD = 0.85; // 85% heap = critical
    private static final double GC_INVOKE_THRESHOLD = 0.90; // 90% heap = suggest GC

    /**
     * Gets current heap usage as a fraction (0.0 - 1.0).
     */
    public static double getHeapUsage() {
        long used = RUNTIME.totalMemory() - RUNTIME.freeMemory();
        long max = RUNTIME.maxMemory();
        return (double) used / max;
    }

    /**
     * Gets used heap in MB.
     */
    public static long getUsedHeapMB() {
        return (RUNTIME.totalMemory() - RUNTIME.freeMemory()) / (1024 * 1024);
    }

    /**
     * Gets max heap in MB.
     */
    public static long getMaxHeapMB() {
        return RUNTIME.maxMemory() / (1024 * 1024);
    }

    /**
     * Gets free heap in MB.
     */
    public static long getFreeHeapMB() {
        return RUNTIME.freeMemory() / (1024 * 1024);
    }

    /**
     * Checks if memory usage is at warning level.
     */
    public static boolean isWarningLevel() {
        return getHeapUsage() >= WARNING_THRESHOLD;
    }

    /**
     * Checks if memory usage is at critical level.
     */
    public static boolean isCriticalLevel() {
        return getHeapUsage() >= CRITICAL_THRESHOLD;
    }

    /**
     * Suggests whether GC should be invoked.
     */
    public static boolean shouldSuggestGC() {
        if (getHeapUsage() < GC_INVOKE_THRESHOLD) {
            return false;
        }

        long now = System.currentTimeMillis();
        long lastCleanup = lastCleanupTime.get();
        return now - lastCleanup >= CLEANUP_COOLDOWN_MS;
    }

    /**
     * Performs memory cleanup by clearing caches and suggesting GC.
     * Should only be called during low activity periods.
     */
    public static void performCleanup() {
        long now = System.currentTimeMillis();
        if (!lastCleanupTime.compareAndSet(lastCleanupTime.get(), now)) {
            return; // Another thread is cleaning
        }

        // Clear soft/weak reference caches
        System.gc();
    }

    /**
     * Gets a memory status string for logging/display.
     */
    public static String getMemoryStatus() {
        long used = getUsedHeapMB();
        long max = getMaxHeapMB();
        double percent = getHeapUsage() * 100;
        return String.format("Memory: %dMB / %dMB (%.1f%%)", used, max, percent);
    }

    /**
     * Checks if there's enough memory for an operation.
     */
    public static boolean hasMemoryFor(long requiredMB) {
        return getFreeHeapMB() >= requiredMB;
    }
}
