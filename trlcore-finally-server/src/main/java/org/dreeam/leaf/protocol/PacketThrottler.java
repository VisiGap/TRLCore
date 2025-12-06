package org.dreeam.leaf.protocol;

import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.ServerPlayer;

/**
 * Packet rate limiter to prevent packet spam and reduce unnecessary network
 * traffic.
 * 
 * @author TRLCore Team
 */
public final class PacketThrottler {

    // Cooldowns per packet type (in milliseconds)
    private static final Object2LongOpenHashMap<Class<?>> packetCooldowns = new Object2LongOpenHashMap<>();

    // Default cooldowns
    private static final long DEFAULT_COOLDOWN_MS = 50; // 20 packets/second max per type
    private static final long ENTITY_UPDATE_COOLDOWN_MS = 50;
    private static final long BLOCK_UPDATE_COOLDOWN_MS = 25;
    private static final long CHAT_COOLDOWN_MS = 100;

    /**
     * Sets cooldown for a packet type.
     */
    public static void setCooldown(Class<?> packetClass, long cooldownMs) {
        packetCooldowns.put(packetClass, cooldownMs);
    }

    /**
     * Gets cooldown for a packet type.
     */
    public static long getCooldown(Class<?> packetClass) {
        return packetCooldowns.getOrDefault(packetClass, DEFAULT_COOLDOWN_MS);
    }

    /**
     * Per-player throttle tracker.
     */
    public static final class PlayerThrottleState {
        private final Object2LongOpenHashMap<Class<?>> lastPacketTime = new Object2LongOpenHashMap<>();
        private long totalThrottled = 0;
        private long totalSent = 0;

        /**
         * Checks if a packet should be throttled.
         * Returns true if packet should be sent, false if throttled.
         */
        public boolean shouldSend(Packet<?> packet) {
            Class<?> type = packet.getClass();
            long cooldown = getCooldown(type);
            if (cooldown <= 0) {
                totalSent++;
                return true;
            }

            long now = System.currentTimeMillis();
            long lastTime = lastPacketTime.getOrDefault(type, 0L);

            if (now - lastTime < cooldown) {
                totalThrottled++;
                return false;
            }

            lastPacketTime.put(type, now);
            totalSent++;
            return true;
        }

        /**
         * Checks if a specific packet type should be throttled.
         */
        public boolean shouldSend(Class<?> packetType) {
            long cooldown = getCooldown(packetType);
            if (cooldown <= 0) {
                totalSent++;
                return true;
            }

            long now = System.currentTimeMillis();
            long lastTime = lastPacketTime.getOrDefault(packetType, 0L);

            if (now - lastTime < cooldown) {
                totalThrottled++;
                return false;
            }

            lastPacketTime.put(packetType, now);
            totalSent++;
            return true;
        }

        /**
         * Gets percentage of throttled packets.
         */
        public double getThrottleRate() {
            long total = totalSent + totalThrottled;
            return total > 0 ? (double) totalThrottled / total : 0;
        }

        public long getTotalThrottled() {
            return totalThrottled;
        }

        public long getTotalSent() {
            return totalSent;
        }

        public void reset() {
            lastPacketTime.clear();
            totalThrottled = 0;
            totalSent = 0;
        }
    }

    /**
     * Creates a throttle state for a player.
     */
    public static PlayerThrottleState createForPlayer(ServerPlayer player) {
        return new PlayerThrottleState();
    }
}
