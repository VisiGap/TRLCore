package org.dreeam.leaf.util.chunk;

import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Prioritized chunk loading system that loads chunks in order of player
 * proximity.
 * Improves perceived loading speed by prioritizing visible chunks.
 * 
 * @author TRLCore Team
 */
public final class ChunkLoadPrioritizer {

    // Priority levels: 0 = immediate (player chunk), 1 = view distance, 2 =
    // extended, 3 = background
    public static final int PRIORITY_IMMEDIATE = 0;
    public static final int PRIORITY_VIEW = 1;
    public static final int PRIORITY_EXTENDED = 2;
    public static final int PRIORITY_BACKGROUND = 3;

    private static final int IMMEDIATE_RADIUS = 2; // Within 2 chunks = immediate
    private static final int VIEW_RADIUS = 8; // Within view distance = high priority

    /**
     * Calculates priority for a chunk based on nearest player distance.
     */
    public static int calculatePriority(ServerLevel level, ChunkPos pos) {
        int minDistSq = Integer.MAX_VALUE;

        for (ServerPlayer player : level.players()) {
            int dx = pos.x - player.chunkPosition().x;
            int dz = pos.z - player.chunkPosition().z;
            int distSq = dx * dx + dz * dz;
            minDistSq = Math.min(minDistSq, distSq);
        }

        if (minDistSq <= IMMEDIATE_RADIUS * IMMEDIATE_RADIUS) {
            return PRIORITY_IMMEDIATE;
        } else if (minDistSq <= VIEW_RADIUS * VIEW_RADIUS) {
            return PRIORITY_VIEW;
        } else if (minDistSq <= VIEW_RADIUS * VIEW_RADIUS * 2) {
            return PRIORITY_EXTENDED;
        }
        return PRIORITY_BACKGROUND;
    }

    /**
     * Calculates priority for a chunk at given coordinates.
     */
    public static int calculatePriority(ServerLevel level, int chunkX, int chunkZ) {
        return calculatePriority(level, new ChunkPos(chunkX, chunkZ));
    }

    /**
     * Checks if a chunk should be loaded immediately (high priority).
     */
    public static boolean isHighPriority(int priority) {
        return priority <= PRIORITY_VIEW;
    }

    /**
     * Gets suggested delay in ticks for loading a chunk based on priority.
     */
    public static int getLoadDelay(int priority) {
        return switch (priority) {
            case PRIORITY_IMMEDIATE -> 0;
            case PRIORITY_VIEW -> 0;
            case PRIORITY_EXTENDED -> 1;
            case PRIORITY_BACKGROUND -> 2;
            default -> 3;
        };
    }

    /**
     * Gets max chunks to process per tick for a given priority.
     */
    public static int getMaxChunksPerTick(int priority) {
        return switch (priority) {
            case PRIORITY_IMMEDIATE -> 16;
            case PRIORITY_VIEW -> 8;
            case PRIORITY_EXTENDED -> 4;
            case PRIORITY_BACKGROUND -> 2;
            default -> 1;
        };
    }
}
