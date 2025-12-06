package org.dreeam.leaf.util.entity;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.projectile.Projectile;

/**
 * Entity processing optimizer that reduces tick frequency based on distance and
 * importance.
 * 
 * @author TRLCore Team
 */
public final class EntityDistanceCuller {

    // Distance thresholds (squared for fast comparison)
    private static final double FULL_TICK_DISTANCE_SQ = 32.0 * 32.0; // 32 blocks = full tick
    private static final double HALF_TICK_DISTANCE_SQ = 64.0 * 64.0; // 64 blocks = half tick
    private static final double QUARTER_TICK_DISTANCE_SQ = 96.0 * 96.0; // 96 blocks = quarter tick

    /**
     * Gets the tick frequency for an entity based on nearest player distance.
     * Returns 1 = every tick, 2 = every 2 ticks, 4 = every 4 ticks, etc.
     */
    public static int getTickFrequency(Entity entity, ServerLevel level) {
        // Always tick players
        if (entity instanceof ServerPlayer)
            return 1;

        // Fast path: if no players, use slowest frequency
        if (level.players().isEmpty())
            return 4;

        double minDistSq = Double.MAX_VALUE;
        for (ServerPlayer player : level.players()) {
            double distSq = entity.distanceToSqr(player);
            if (distSq < minDistSq) {
                minDistSq = distSq;
            }
        }

        // Determine base frequency from distance
        int baseFreq;
        if (minDistSq <= FULL_TICK_DISTANCE_SQ) {
            baseFreq = 1;
        } else if (minDistSq <= HALF_TICK_DISTANCE_SQ) {
            baseFreq = 2;
        } else if (minDistSq <= QUARTER_TICK_DISTANCE_SQ) {
            baseFreq = 4;
        } else {
            baseFreq = 8;
        }

        // Adjust based on entity type
        return adjustForEntityType(entity, baseFreq);
    }

    private static int adjustForEntityType(Entity entity, int baseFreq) {
        // Items and XP orbs can always be ticked slower
        if (entity instanceof ItemEntity || entity instanceof net.minecraft.world.entity.ExperienceOrb) {
            return Math.max(baseFreq, 2);
        }

        // Projectiles need more frequent ticks for accuracy
        if (entity instanceof Projectile) {
            return Math.min(baseFreq, 2);
        }

        // Mobs with targets need faster ticks
        if (entity instanceof LivingEntity living) {
            if (living.getLastAttacker() != null || living.getTarget() != null) {
                return 1;
            }
        }

        return baseFreq;
    }

    /**
     * Checks if an entity should be ticked this cycle.
     */
    public static boolean shouldTick(Entity entity, int tickCount, int frequency) {
        if (frequency <= 1)
            return true;
        return (entity.getId() + tickCount) % frequency == 0;
    }

    /**
     * Gets squared distance to nearest player.
     */
    public static double getDistanceToNearestPlayerSq(Entity entity, ServerLevel level) {
        double minDistSq = Double.MAX_VALUE;
        for (ServerPlayer player : level.players()) {
            double distSq = entity.distanceToSqr(player);
            if (distSq < minDistSq) {
                minDistSq = distSq;
            }
        }
        return minDistSq;
    }

    /**
     * Checks if entity is beyond all player render distances.
     */
    public static boolean isBeyondAllPlayers(Entity entity, ServerLevel level, double maxDistanceSq) {
        for (ServerPlayer player : level.players()) {
            if (entity.distanceToSqr(player) <= maxDistanceSq) {
                return false;
            }
        }
        return true;
    }
}
