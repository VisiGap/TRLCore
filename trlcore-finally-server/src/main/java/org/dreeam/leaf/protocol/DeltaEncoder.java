package org.dreeam.leaf.protocol;

import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Delta encoder for sending only changed data to reduce bandwidth.
 * 
 * @author TRLCore Team
 */
public final class DeltaEncoder {

    // ===== Block State Delta =====

    /**
     * Per-player block state tracking for delta updates.
     */
    public static final class BlockStateDelta {
        private final Long2ObjectOpenHashMap<BlockState> lastSent = new Long2ObjectOpenHashMap<>(256);
        private int changeCount = 0;

        /**
         * Checks if a block state has changed and should be sent.
         */
        public boolean hasChanged(BlockPos pos, BlockState newState) {
            long key = pos.asLong();
            BlockState old = lastSent.get(key);
            if (old == newState) {
                return false;
            }
            lastSent.put(key, newState);
            changeCount++;
            return true;
        }

        /**
         * Clears tracking for a specific chunk.
         */
        public void clearChunk(int chunkX, int chunkZ) {
            lastSent.keySet().removeIf(key -> {
                int x = BlockPos.getX(key) >> 4;
                int z = BlockPos.getZ(key) >> 4;
                return x == chunkX && z == chunkZ;
            });
        }

        /**
         * Clears all tracking data.
         */
        public void clear() {
            lastSent.clear();
            changeCount = 0;
        }

        public int getChangeCount() {
            return changeCount;
        }

        public int getTrackedCount() {
            return lastSent.size();
        }
    }

    // ===== Position Delta =====

    /**
     * Encodes position delta as relative coordinates (fewer bytes).
     */
    public static short[] encodePositionDelta(double oldX, double oldY, double oldZ,
            double newX, double newY, double newZ) {
        // Delta in 1/4096ths of a block (12-bit fixed point)
        int dx = (int) ((newX - oldX) * 4096);
        int dy = (int) ((newY - oldY) * 4096);
        int dz = (int) ((newZ - oldZ) * 4096);

        // Check if delta fits in shorts
        if (dx >= Short.MIN_VALUE && dx <= Short.MAX_VALUE &&
                dy >= Short.MIN_VALUE && dy <= Short.MAX_VALUE &&
                dz >= Short.MIN_VALUE && dz <= Short.MAX_VALUE) {
            return new short[] { (short) dx, (short) dy, (short) dz };
        }
        return null; // Delta too large, send absolute
    }

    /**
     * Checks if position has moved enough to send an update.
     */
    public static boolean significantMovement(double dx, double dy, double dz, double threshold) {
        return dx * dx + dy * dy + dz * dz >= threshold * threshold;
    }

    // ===== Rotation Delta =====

    /**
     * Encodes rotation delta.
     * Returns null if rotation hasn't changed significantly.
     */
    public static byte[] encodeRotationDelta(float oldYaw, float oldPitch,
            float newYaw, float newPitch,
            float threshold) {
        float dYaw = Math.abs(newYaw - oldYaw);
        float dPitch = Math.abs(newPitch - oldPitch);

        // Normalize yaw difference
        if (dYaw > 180)
            dYaw = 360 - dYaw;

        if (dYaw < threshold && dPitch < threshold) {
            return null; // No significant change
        }

        // Encode as 256ths of a rotation (Minecraft protocol format)
        return new byte[] {
                (byte) (newYaw * 256.0F / 360.0F),
                (byte) (newPitch * 256.0F / 360.0F)
        };
    }

    // ===== VarInt Utilities =====

    /**
     * Calculates bytes needed for a VarInt.
     */
    public static int varIntSize(int value) {
        if ((value & 0xFFFFFF80) == 0)
            return 1;
        if ((value & 0xFFFFC000) == 0)
            return 2;
        if ((value & 0xFFE00000) == 0)
            return 3;
        if ((value & 0xF0000000) == 0)
            return 4;
        return 5;
    }

    /**
     * Calculates bytes saved by using a smaller encoding.
     */
    public static int bytesSaved(int fullSize, int encodedSize) {
        return Math.max(0, fullSize - encodedSize);
    }
}
