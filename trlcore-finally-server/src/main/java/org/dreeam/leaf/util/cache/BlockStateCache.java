package org.dreeam.leaf.util.cache;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Cache for frequently accessed BlockState properties to reduce computation
 * overhead.
 * 
 * @author TRLCore Team
 */
public final class BlockStateCache {

    private static final ConcurrentHashMap<BlockState, CachedBlockStateProperties> CACHE = new ConcurrentHashMap<>(
            4096);

    // Pre-computed common block states for fast lookup
    private static BlockState AIR_STATE;
    private static BlockState STONE_STATE;
    private static BlockState WATER_STATE;
    private static BlockState LAVA_STATE;

    public static void init() {
        AIR_STATE = net.minecraft.world.level.block.Blocks.AIR.defaultBlockState();
        STONE_STATE = net.minecraft.world.level.block.Blocks.STONE.defaultBlockState();
        WATER_STATE = net.minecraft.world.level.block.Blocks.WATER.defaultBlockState();
        LAVA_STATE = net.minecraft.world.level.block.Blocks.LAVA.defaultBlockState();
    }

    public static CachedBlockStateProperties getCached(BlockState state) {
        return CACHE.computeIfAbsent(state, CachedBlockStateProperties::new);
    }

    public static boolean isAir(BlockState state) {
        return state == AIR_STATE || state.isAir();
    }

    public static boolean isCommonFluid(BlockState state) {
        return state == WATER_STATE || state == LAVA_STATE;
    }

    public static void clearCache() {
        CACHE.clear();
    }

    /**
     * Cached properties for a BlockState to avoid repeated computation.
     */
    public static final class CachedBlockStateProperties {
        public final BlockState state;
        public final Block block;
        public final boolean isAir;
        public final boolean isSolid;
        public final boolean isLiquid;
        public final boolean blocksMotion;
        public final boolean isCollisionShapeFullBlock;
        public final float destroySpeed;
        public final int lightEmission;

        public CachedBlockStateProperties(BlockState state) {
            this.state = state;
            this.block = state.getBlock();
            this.isAir = state.isAir();
            this.isSolid = state.isSolid();
            this.isLiquid = !state.getFluidState().isEmpty();
            this.blocksMotion = state.blocksMotion();
            this.isCollisionShapeFullBlock = state.isCollisionShapeFullBlock(null, null);
            this.destroySpeed = state.getDestroySpeed(null, null);
            this.lightEmission = state.getLightEmission();
        }
    }
}
