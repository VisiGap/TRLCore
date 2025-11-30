package org.dreeam.leaf.util;

/**
 * Central repository for常量 values used throughout Leaf.
 * <p>
 * This class consolidates magic numbers and hardcoded values into named
 * constants
 * for better maintainability and readability. Constants are organized by
 * category
 * for easy navigation.
 * </p>
 *
 * @author Leaf Development Team
 * @since 1.21.8
 */
public final class LeafConstants {

    private LeafConstants() {
        throw new AssertionError("Utility class should not be instantiated");
    }

    /**
     * Performance-related constants
     */
    public static class Performance {

        /** Default cache size for various caching mechanisms */
        public static final int DEFAULT_CACHE_SIZE = 1024;

        /** Maximum number of entities to process per tick in certain operations */
        public static final int MAX_ENTITIES_PER_TICK = 200;

        /** Threshold for considering an operation expensive (in nanoseconds) */
        public static final long EXPENSIVE_OPERATION_THRESHOLD_NS = 1_000_000; // 1ms

        /** Default chunk processing batch size */
        public static final int CHUNK_BATCH_SIZE = 16;

        /** Default thread pool size multiplier (cores * multiplier) */
        public static final int THREAD_POOL_MULTIPLIER = 2;

        private Performance() {
        }
    }

    /**
     * Timeout and delay constants
     */
    public static class Timeouts {

        /** Default player login timeout in milliseconds */
        public static final long LOGIN_TIMEOUT_MS = 30_000; // 30 seconds

        /** Default chunk load timeout in milliseconds */
        public static final long CHUNK_LOAD_TIMEOUT_MS = 5_000; // 5 seconds

        /** Default async operation timeout in milliseconds */
        public static final long ASYNC_OPERATION_TIMEOUT_MS = 10_000; // 10 seconds

        /** Tick length in milliseconds */
        public static final long TICK_LENGTH_MS = 50; // 20 TPS

        /** Keep-alive packet interval in ticks */
        public static final int KEEP_ALIVE_INTERVAL_TICKS = 20; // 1 second

        private Timeouts() {
        }
    }

    /**
     * Network-related constants
     */
    public static class Network {

        /** Maximum packet size in bytes before compression */
        public static final int MAX_PACKET_SIZE = 2_097_152; // 2MB

        /** Network compression threshold in bytes */
        public static final int COMPRESSION_THRESHOLD = 256;

        /** Maximum number of packets to batch */
        public static final int MAX_BATCH_PACKETS = 100;

        /** Default netty IO thread count */
        public static final int DEFAULT_IO_THREADS = 4;

        private Network() {
        }
    }

    /**
     * Chunk and world-related constants
     */
    public static class World {

        /** Chunk size (blocks per chunk side) */
        public static final int CHUNK_SIZE = 16;

        /** Chunk section height */
        public static final int SECTION_HEIGHT = 16;

        /** View distance minimum */
        public static final int MIN_VIEW_DISTANCE = 2;

        /** View distance maximum */
        public static final int MAX_VIEW_DISTANCE = 32;

        /** Simulation distance minimum */
        public static final int MIN_SIMULATION_DISTANCE = 2;

        /** Simulation distance maximum */
        public static final int MAX_SIMULATION_DISTANCE = 32;

        private World() {
        }
    }

    /**
     * Entity and mob-related constants
     */
    public static class Entity {

        /** Maximum entity tracking range in blocks */
        public static final int MAX_TRACKING_RANGE = 128;

        /** Default entity update interval in ticks */
        public static final int DEFAULT_UPDATE_INTERVAL = 3;

        /** Maximum entities per chunk before spawn throttling */
        public static final int MAX_ENTITIES_PER_CHUNK = 50;

        /** Despawn range in blocks */
        public static final int DESPAWN_RANGE = 128;

        private Entity() {
        }
    }

    /**
     * Memory and GC-related constants
     */
    public static class Memory {

        /** Small object threshold in bytes */
        public static final int SMALL_OBJECT_THRESHOLD = 256;

        /** Initial capacity for frequently created collections */
        public static final int DEFAULT_COLLECTION_CAPACITY = 16;

        /** Load factor for hash maps */
        public static final float DEFAULT_LOAD_FACTOR = 0.75f;

        /** String pool size for commonly used strings */
        public static final int STRING_POOL_SIZE = 512;

        private Memory() {
        }
    }

    /**
     * Validation and limit constants
     */
    public static class Limits {

        /** Maximum player name length */
        public static final int MAX_PLAYER_NAME_LENGTH = 16;

        /** Maximum chat message length */
        public static final int MAX_CHAT_MESSAGE_LENGTH = 256;

        /** Maximum number of concurrent async operations */
        public static final int MAX_CONCURRENT_ASYNC_OPS = 100;

        /** Maximum book page count */
        public static final int MAX_BOOK_PAGES = 100;

        /** Maximum items in a recipe */
        public static final int MAX_RECIPE_ITEMS = 9;

        private Limits() {
        }
    }

    /**
     * File and I/O constants
     */
    public static class IO {

        /** Buffer size for I/O operations */
        public static final int BUFFER_SIZE = 8192; // 8KB

        /** Maximum file size for certain operations in bytes */
        public static final long MAX_FILE_SIZE = 10_485_760; // 10MB

        /** File backup timestamp format */
        public static final String BACKUP_DATE_FORMAT = "yyMMddHHmmss";

        private IO() {
        }
    }

    /**
     * Debug and logging constants
     */
    public static class Debug {

        /** Stack trace depth for abbreviated logging */
        public static final int STACK_TRACE_DEPTH = 10;

        /** Maximum log message length before truncation */
        public static final int MAX_LOG_MESSAGE_LENGTH = 1024;

        /** Verbose logging threshold (log operations slower than this in ms) */
        public static final long VERBOSE_THRESHOLD_MS = 100;

        private Debug() {
        }
    }
}
