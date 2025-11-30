package org.dreeam.leaf.util;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletionException;

/**
 * Centralized error handling utility for Leaf.
 * <p>
 * Provides consistent error handling, logging, and exception wrapping
 * throughout
 * the codebase. Includes specialized handlers for common error scenarios like
 * chunk operations, async operations, and world ticking.
 * </p>
 *
 * @author Leaf Development Team
 * @since 1.21.8
 */
public final class ErrorHandler {

    private static final Logger LOGGER = LogManager.getLogger(ErrorHandler.class);

    private ErrorHandler() {
        throw new AssertionError("Utility class should not be instantiated");
    }

    /**
     * Handles a general exception with proper logging
     *
     * @param context descriptive context of where the error occurred
     * @param e       the exception to handle
     */
    public static void handleException(@NotNull String context, @NotNull Throwable e) {
        LOGGER.error("Error in {}: {}", context, e.getMessage(), e);
    }

    /**
     * Handles an exception with a custom log level
     *
     * @param context descriptive context
     * @param e       the exception
     * @param level   log level to use
     */
    public static void handleException(@NotNull String context, @NotNull Throwable e, @NotNull Level level) {
        LOGGER.log(level, "Error in {}: {}", context, e.getMessage(), e);
    }

    /**
     * Handles chunk-related errors
     *
     * @param operation the chunk operation that failed
     * @param chunkX    chunk X coordinate
     * @param chunkZ    chunk Z coordinate
     * @param worldName world name
     * @param e         the exception
     */
    public static void handleChunkError(@NotNull String operation, int chunkX, int chunkZ,
            @NotNull String worldName, @NotNull Throwable e) {
        LOGGER.error("Chunk {} failed at ({}, {}) in world '{}': {}",
                operation, chunkX, chunkZ, worldName, e.getMessage(), e);
    }

    /**
     * Handles async operation failures
     *
     * @param operationName name of the async operation
     * @param e             the exception (may be wrapped in CompletionException)
     */
    public static void handleAsyncError(@NotNull String operationName, @NotNull Throwable e) {
        Throwable cause = e instanceof CompletionException && e.getCause() != null ? e.getCause() : e;
        LOGGER.error("Async operation '{}' failed: {}", operationName, cause.getMessage(), cause);
    }

    /**
     * Handles world ticking errors
     *
     * @param worldName the world that had an error during tick
     * @param tickPhase the phase of ticking (e.g., "entity", "block", "fluid")
     * @param e         the exception
     */
    public static void handleWorldTickError(@NotNull String worldName, @NotNull String tickPhase,
            @NotNull Throwable e) {
        LOGGER.error("World '{}' encountered error during {} tick: {}",
                worldName, tickPhase, e.getMessage(), e);
    }

    /**
     * Handles entity-related errors
     *
     * @param entityInfo description or ID of the entity
     * @param operation  the operation that failed
     * @param e          the exception
     */
    public static void handleEntityError(@NotNull String entityInfo, @NotNull String operation,
            @NotNull Throwable e) {
        LOGGER.error("Entity {} failed during {}: {}", entityInfo, operation, e.getMessage(), e);
    }

    /**
     * Handles network/packet errors
     *
     * @param packetType the type of packet
     * @param player     player name or connection info (nullable)
     * @param e          the exception
     */
    public static void handleNetworkError(@NotNull String packetType, @Nullable String player,
            @NotNull Throwable e) {
        if (player != null) {
            LOGGER.error("Packet {} error for player '{}': {}", packetType, player, e.getMessage(), e);
        } else {
            LOGGER.error("Packet {} error: {}", packetType, e.getMessage(), e);
        }
    }

    /**
     * Handles configuration errors
     *
     * @param configPath path to the config that had an error
     * @param e          the exception
     */
    public static void handleConfigError(@NotNull String configPath, @NotNull Throwable e) {
        LOGGER.error("Configuration error in '{}': {}", configPath, e.getMessage(), e);
    }

    /**
     * Handles I/O errors
     *
     * @param operation description of the I/O operation
     * @param filePath  path to the file (nullable)
     * @param e         the exception
     */
    public static void handleIOError(@NotNull String operation, @Nullable String filePath,
            @NotNull Throwable e) {
        if (filePath != null) {
            LOGGER.error("I/O error during {} on file '{}': {}", operation, filePath, e.getMessage(), e);
        } else {
            LOGGER.error("I/O error during {}: {}", operation, e.getMessage(), e);
        }
    }

    /**
     * Silently handles an exception (only logs at debug level)
     * Use sparingly, only for truly expected/non-critical errors
     *
     * @param context context description
     * @param e       the exception
     */
    public static void handleSilently(@NotNull String context, @NotNull Throwable e) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Suppressed exception in {}: {}", context, e.getMessage(), e);
        }
    }

    /**
     * Handles an error and returns a default value
     *
     * @param context      context description
     * @param e            the exception
     * @param defaultValue default value to return
     * @param <T>          type of the default value
     * @return the default value
     */
    public static <T> T handleAndReturn(@NotNull String context, @NotNull Throwable e, T defaultValue) {
        handleException(context, e);
        return defaultValue;
    }

    /**
     * Checks if an exception is critical and should trigger special handling
     *
     * @param e the exception to check
     * @return true if critical
     */
    public static boolean isCritical(@NotNull Throwable e) {
        return e instanceof OutOfMemoryError ||
                e instanceof StackOverflowError ||
                e instanceof VirtualMachineError ||
                (e.getCause() != null && isCritical(e.getCause()));
    }

    /**
     * Handles a critical error that may require server shutdown
     *
     * @param context context description
     * @param e       the critical exception
     */
    public static void handleCritical(@NotNull String context, @NotNull Throwable e) {
        LOGGER.fatal("CRITICAL ERROR in {}: {}", context, e.getMessage(), e);
        LOGGER.fatal("This is a critical error that may cause server instability!");

        // Optionally trigger additional handling, like sending alerts
        // or preparing for graceful shutdown
    }
}
