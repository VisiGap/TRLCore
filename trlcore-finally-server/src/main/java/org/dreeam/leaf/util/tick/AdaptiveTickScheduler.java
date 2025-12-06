package org.dreeam.leaf.util.tick;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Adaptive tick scheduler that dynamically adjusts entity processing based on
 * TPS.
 * When TPS drops, less important entities are processed less frequently.
 * 
 * @author TRLCore Team
 */
public final class AdaptiveTickScheduler {

    private static final int TARGET_MSPT = 50; // 20 TPS = 50ms per tick
    private static final int WARNING_MSPT = 45; // Start throttling at 45ms
    private static final int CRITICAL_MSPT = 55; // Heavy throttling at 55ms

    private static final AtomicLong lastTickDuration = new AtomicLong(0);
    private static final AtomicInteger tickSkipFactor = new AtomicInteger(1);
    private static volatile int currentTickCount = 0;

    /**
     * Updates the scheduler with the latest tick duration.
     */
    public static void recordTickDuration(long durationMs) {
        lastTickDuration.set(durationMs);

        // Adjust skip factor based on performance
        if (durationMs > CRITICAL_MSPT) {
            tickSkipFactor.updateAndGet(v -> Math.min(v + 1, 4)); // Max skip 1/4 entities
        } else if (durationMs > WARNING_MSPT) {
            tickSkipFactor.updateAndGet(v -> Math.min(v + 1, 2)); // Max skip 1/2 entities
        } else if (durationMs < TARGET_MSPT - 10) {
            tickSkipFactor.updateAndGet(v -> Math.max(v - 1, 1)); // Reduce skipping
        }

        currentTickCount++;
    }

    /**
     * Checks if an entity should be ticked this cycle.
     * Low-priority entities may be skipped during lag.
     */
    public static boolean shouldTickEntity(Entity entity, int priority) {
        int skipFactor = tickSkipFactor.get();
        if (skipFactor <= 1)
            return true;

        // High priority (players, bosses) always tick
        if (priority <= 0)
            return true;

        // Use entity ID for deterministic but distributed skipping
        return (entity.getId() + currentTickCount) % skipFactor == 0;
    }

    /**
     * Gets entity priority based on type and distance to nearest player.
     * 0 = highest priority (always tick)
     * Higher = lower priority (may skip during lag)
     */
    public static int getEntityPriority(Entity entity) {
        if (entity.isRemoved())
            return 10;

        // Players and passenger vehicles are high priority
        if (entity instanceof net.minecraft.world.entity.player.Player)
            return 0;
        if (entity.hasControllingPassenger())
            return 0;

        // Hostile mobs near players are medium-high
        if (entity instanceof net.minecraft.world.entity.monster.Monster)
            return 1;

        // Animals and NPCs are medium
        if (entity instanceof net.minecraft.world.entity.animal.Animal)
            return 2;
        if (entity instanceof net.minecraft.world.entity.npc.Npc)
            return 2;

        // Items and XP orbs are lower priority
        if (entity instanceof net.minecraft.world.entity.item.ItemEntity)
            return 3;
        if (entity instanceof net.minecraft.world.entity.ExperienceOrb)
            return 3;

        // Projectiles depend on shooter
        if (entity instanceof net.minecraft.world.entity.projectile.Projectile)
            return 1;

        return 2; // Default medium priority
    }

    public static long getLastTickDuration() {
        return lastTickDuration.get();
    }

    public static int getCurrentSkipFactor() {
        return tickSkipFactor.get();
    }

    public static boolean isLagging() {
        return lastTickDuration.get() > WARNING_MSPT;
    }

    public static boolean isCriticallyLagging() {
        return lastTickDuration.get() > CRITICAL_MSPT;
    }
}
