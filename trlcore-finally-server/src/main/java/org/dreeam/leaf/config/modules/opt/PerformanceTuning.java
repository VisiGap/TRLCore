package org.dreeam.leaf.config.modules.opt;

import org.dreeam.leaf.config.ConfigModules;
import org.dreeam.leaf.config.EnumConfigCategory;

/**
 * Advanced performance tuning parameters.
 */
public class PerformanceTuning extends ConfigModules {

    public String getBasePath() {
        return EnumConfigCategory.PERF.getBaseKeyName() + ".tuning";
    }

    // Tick settings
    public static int targetTps = 20;
    public static int maxCatchUpTicks = 10;
    public static boolean enableTickSkipping = false;
    public static int tickSkipThresholdMs = 100;

    // Entity settings
    public static int maxEntitiesPerTick = 0; // 0 = unlimited
    public static int entityActivationRange = 32;
    public static int monsterActivationRange = 32;
    public static int animalActivationRange = 16;
    public static int miscActivationRange = 8;

    // Chunk settings
    public static int maxChunkLoadsPerTick = 10;
    public static int maxChunkUnloadsPerTick = 50;
    public static int chunkLoadThreads = 2;

    // Spawn settings
    public static int mobSpawnRange = 8;
    public static int maxMobsPerChunk = 0; // 0 = use vanilla
    public static double spawnChanceMultiplier = 1.0;

    @Override
    public void onLoaded() {
        config.addCommentRegionBased(getBasePath(), """
                Advanced performance tuning parameters.
                Adjust these values based on your server's needs.""",
                """
                        高级性能调优参数.
                        根据服务器需求调整这些值.""");

        // Tick settings
        targetTps = config.getInt(getBasePath() + ".target-tps", targetTps,
                config.pickStringRegionBased("Target TPS (max 20).", "目标 TPS (最大 20)"));

        maxCatchUpTicks = config.getInt(getBasePath() + ".max-catch-up-ticks", maxCatchUpTicks,
                config.pickStringRegionBased("Max ticks to catch up after lag.", "卡顿后最大补偿 tick 数"));

        enableTickSkipping = config.getBoolean(getBasePath() + ".enable-tick-skipping", enableTickSkipping,
                config.pickStringRegionBased("Skip ticks when severely lagging.", "严重卡顿时跳过 tick"));

        tickSkipThresholdMs = config.getInt(getBasePath() + ".tick-skip-threshold-ms", tickSkipThresholdMs,
                config.pickStringRegionBased("MSPT threshold to trigger tick skip.", "触发跳过 tick 的 MSPT 阈值"));

        // Entity settings
        maxEntitiesPerTick = config.getInt(getBasePath() + ".max-entities-per-tick", maxEntitiesPerTick,
                config.pickStringRegionBased("Max entities to process per tick (0 = unlimited).",
                        "每 tick 处理的最大实体数 (0 = 无限制)"));

        entityActivationRange = config.getInt(getBasePath() + ".entity-activation-range", entityActivationRange,
                config.pickStringRegionBased("Default entity activation range in blocks.", "默认实体激活范围 (方块)"));

        monsterActivationRange = config.getInt(getBasePath() + ".monster-activation-range", monsterActivationRange,
                config.pickStringRegionBased("Monster activation range.", "怪物激活范围"));

        animalActivationRange = config.getInt(getBasePath() + ".animal-activation-range", animalActivationRange,
                config.pickStringRegionBased("Animal activation range.", "动物激活范围"));

        miscActivationRange = config.getInt(getBasePath() + ".misc-activation-range", miscActivationRange,
                config.pickStringRegionBased("Misc entity activation range.", "杂项实体激活范围"));

        // Chunk settings
        maxChunkLoadsPerTick = config.getInt(getBasePath() + ".max-chunk-loads-per-tick", maxChunkLoadsPerTick,
                config.pickStringRegionBased("Max chunks to load per tick.", "每 tick 最大加载区块数"));

        maxChunkUnloadsPerTick = config.getInt(getBasePath() + ".max-chunk-unloads-per-tick", maxChunkUnloadsPerTick,
                config.pickStringRegionBased("Max chunks to unload per tick.", "每 tick 最大卸载区块数"));

        chunkLoadThreads = config.getInt(getBasePath() + ".chunk-load-threads", chunkLoadThreads,
                config.pickStringRegionBased("Threads for chunk loading.", "区块加载线程数"));

        // Spawn settings
        mobSpawnRange = config.getInt(getBasePath() + ".mob-spawn-range", mobSpawnRange,
                config.pickStringRegionBased("Mob spawn range in chunks.", "生物生成范围 (区块)"));

        maxMobsPerChunk = config.getInt(getBasePath() + ".max-mobs-per-chunk", maxMobsPerChunk,
                config.pickStringRegionBased("Max mobs per chunk (0 = vanilla).", "每区块最大生物数 (0 = 原版)"));

        spawnChanceMultiplier = config.getDouble(getBasePath() + ".spawn-chance-multiplier", spawnChanceMultiplier,
                config.pickStringRegionBased("Spawn chance multiplier (1.0 = normal).", "生成概率倍数 (1.0 = 正常)"));
    }
}
