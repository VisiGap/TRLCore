package org.dreeam.leaf.util;

import java.util.function.Supplier;

/**
 * Timing utilities for performance measurement.
 * 
 * @author TRLCore Team
 */
public final class Timing {

    private Timing() {
    }

    /**
     * Measures execution time of a runnable.
     * 
     * @return Execution time in nanoseconds
     */
    public static long measureNanos(Runnable action) {
        long start = System.nanoTime();
        action.run();
        return System.nanoTime() - start;
    }

    /**
     * Measures execution time of a runnable.
     * 
     * @return Execution time in milliseconds
     */
    public static long measureMillis(Runnable action) {
        long start = System.currentTimeMillis();
        action.run();
        return System.currentTimeMillis() - start;
    }

    /**
     * Measures execution time and returns result with timing.
     */
    public static <T> TimedResult<T> timed(Supplier<T> action) {
        long start = System.nanoTime();
        T result = action.get();
        long duration = System.nanoTime() - start;
        return new TimedResult<>(result, duration);
    }

    /**
     * Measures average execution time over multiple runs.
     */
    public static double averageNanos(Runnable action, int iterations) {
        // Warmup
        for (int i = 0; i < Math.min(3, iterations); i++) {
            action.run();
        }

        long total = 0;
        for (int i = 0; i < iterations; i++) {
            total += measureNanos(action);
        }
        return (double) total / iterations;
    }

    /**
     * Creates a simple stopwatch.
     */
    public static Stopwatch startNew() {
        return new Stopwatch();
    }

    // ===== Result Container =====

    public record TimedResult<T>(T value, long nanos) {
        public double millis() {
            return nanos / 1_000_000.0;
        }

        public double seconds() {
            return nanos / 1_000_000_000.0;
        }
    }

    // ===== Stopwatch =====

    public static final class Stopwatch {
        private long startNanos;
        private long elapsed = 0;
        private boolean running = true;

        private Stopwatch() {
            this.startNanos = System.nanoTime();
        }

        public void stop() {
            if (running) {
                elapsed += System.nanoTime() - startNanos;
                running = false;
            }
        }

        public void resume() {
            if (!running) {
                startNanos = System.nanoTime();
                running = true;
            }
        }

        public void reset() {
            startNanos = System.nanoTime();
            elapsed = 0;
            running = true;
        }

        public long elapsedNanos() {
            if (running) {
                return elapsed + (System.nanoTime() - startNanos);
            }
            return elapsed;
        }

        public double elapsedMillis() {
            return elapsedNanos() / 1_000_000.0;
        }

        public double elapsedSeconds() {
            return elapsedNanos() / 1_000_000_000.0;
        }

        @Override
        public String toString() {
            return StringUtils.formatDuration((long) elapsedMillis());
        }
    }
}
