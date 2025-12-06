package org.dreeam.leaf.util;

/**
 * Centralized constants for TRLCore to avoid magic numbers.
 * All constants should be defined here for maintainability.
 * 
 * @author TRLCore Team
 */
public final class LeafConstants {

    private LeafConstants() {
    } // Prevent instantiation

    // ===== Time Constants =====
    public static final int TICKS_PER_SECOND = 20;
    public static final int MILLIS_PER_TICK = 50;
    public static final long NANOS_PER_TICK = 50_000_000L;
    public static final int TICKS_PER_MINUTE = 1200;
    public static final int TICKS_PER_HOUR = 72000;
    public static final int TICKS_PER_DAY = 24000; // Minecraft day

    // ===== Chunk Constants =====
    public static final int CHUNK_SIZE = 16;
    public static final int CHUNK_SECTION_SIZE = 16;
    public static final int BLOCKS_PER_SECTION = CHUNK_SIZE * CHUNK_SIZE * CHUNK_SECTION_SIZE;
    public static final int MIN_BUILD_HEIGHT = -64;
    public static final int MAX_BUILD_HEIGHT = 320;
    public static final int WORLD_HEIGHT = MAX_BUILD_HEIGHT - MIN_BUILD_HEIGHT;

    // ===== Entity Constants =====
    public static final double ENTITY_COLLISION_EPSILON = 1.0E-7;
    public static final double MOVEMENT_EPSILON = 1.0E-4;
    public static final int MAX_ENTITY_TRACKING_DISTANCE = 256;
    public static final int DEFAULT_DESPAWN_RANGE = 128;

    // ===== Network Constants =====
    public static final int DEFAULT_COMPRESSION_THRESHOLD = 256;
    public static final int MAX_PACKET_SIZE = 2097152; // 2MB
    public static final int DEFAULT_VIEW_DISTANCE = 10;
    public static final int MAX_CHAT_LENGTH = 256;

    // ===== Memory Constants =====
    public static final int KB = 1024;
    public static final int MB = 1024 * 1024;
    public static final long GB = 1024L * 1024 * 1024;

    // ===== Pool Sizes =====
    public static final int DEFAULT_POOL_SIZE = 64;
    public static final int LARGE_POOL_SIZE = 256;
    public static final int MAX_POOLED_OBJECT_SIZE = 1024;

    // ===== Performance Thresholds =====
    public static final long TARGET_MSPT = 50L;
    public static final long WARNING_MSPT = 45L;
    public static final long CRITICAL_MSPT = 55L;
    public static final double MEMORY_WARNING_THRESHOLD = 0.70;
    public static final double MEMORY_CRITICAL_THRESHOLD = 0.85;
}
