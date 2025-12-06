package org.dreeam.leaf.util;

import org.dreeam.leaf.config.LeafConfig;
import org.slf4j.Logger;

import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Supplier;

/**
 * Centralized error handler for consistent error handling and logging.
 * Provides retry logic, error counting, and graceful degradation.
 * 
 * @author TRLCore Team
 */
public final class ErrorHandler {

    private static final Logger LOGGER = LeafConfig.LOGGER;
    private static final AtomicLong totalErrors = new AtomicLong(0);
    private static final AtomicLong suppressedErrors = new AtomicLong(0);

    // Error suppression to prevent log spam
    private static final long ERROR_SUPPRESS_INTERVAL_MS = 5000;
    private static volatile long lastErrorLogTime = 0;
    private static volatile String lastErrorMessage = "";
    private static int repeatCount = 0;

    private ErrorHandler() {
    }

    /**
     * Handles an exception with standard logging.
     */
    public static void handle(String context, Throwable t) {
        totalErrors.incrementAndGet();

        String message = context + ": " + t.getMessage();
        long now = System.currentTimeMillis();

        // Suppress repeated errors
        if (message.equals(lastErrorMessage) && now - lastErrorLogTime < ERROR_SUPPRESS_INTERVAL_MS) {
            repeatCount++;
            suppressedErrors.incrementAndGet();
            return;
        }

        // Log accumulated repeats
        if (repeatCount > 0) {
            LOGGER.warn("Previous error repeated {} times", repeatCount);
            repeatCount = 0;
        }

        lastErrorMessage = message;
        lastErrorLogTime = now;
        LOGGER.error(context, t);
    }

    /**
     * Handles an exception silently (no logging).
     */
    public static void handleSilent(Throwable t) {
        totalErrors.incrementAndGet();
    }

    /**
     * Handles an exception with a warning level log.
     */
    public static void handleWarning(String context, Throwable t) {
        totalErrors.incrementAndGet();
        LOGGER.warn("{}: {}", context, t.getMessage());
    }

    /**
     * Executes with retry logic.
     */
    public static <T> T executeWithRetry(Supplier<T> action, int maxRetries, String context) {
        Throwable lastError = null;
        for (int i = 0; i <= maxRetries; i++) {
            try {
                return action.get();
            } catch (Throwable t) {
                lastError = t;
                if (i < maxRetries) {
                    LOGGER.debug("Retry {} of {} for {}", i + 1, maxRetries, context);
                }
            }
        }
        handle(context + " (after " + maxRetries + " retries)", lastError);
        return null;
    }

    /**
     * Executes with a fallback value on error.
     */
    public static <T> T executeWithFallback(Supplier<T> action, T fallback, String context) {
        try {
            return action.get();
        } catch (Throwable t) {
            handleWarning(context, t);
            return fallback;
        }
    }

    /**
     * Executes an action, catching any exceptions.
     */
    public static void executeSafe(Runnable action, String context) {
        try {
            action.run();
        } catch (Throwable t) {
            handle(context, t);
        }
    }

    /**
     * Executes an action silently, ignoring any exceptions.
     */
    public static void executeSilent(Runnable action) {
        try {
            action.run();
        } catch (Throwable t) {
            handleSilent(t);
        }
    }

    // ===== Statistics =====

    public static long getTotalErrors() {
        return totalErrors.get();
    }

    public static long getSuppressedErrors() {
        return suppressedErrors.get();
    }

    public static void resetStats() {
        totalErrors.set(0);
        suppressedErrors.set(0);
    }
}
