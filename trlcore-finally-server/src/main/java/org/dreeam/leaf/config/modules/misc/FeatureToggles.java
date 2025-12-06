package org.dreeam.leaf.config.modules.misc;

import org.dreeam.leaf.config.ConfigModules;
import org.dreeam.leaf.config.EnumConfigCategory;

/**
 * Feature toggles for enabling/disabling major systems.
 */
public class FeatureToggles extends ConfigModules {

    public String getBasePath() {
        return EnumConfigCategory.MISC.getBaseKeyName() + ".features";
    }

    // Core features
    public static boolean enableObjectPooling = true;
    public static boolean enableAdaptiveTick = true;
    public static boolean enableEntityDistanceCulling = true;
    public static boolean enableChunkPrioritization = true;

    // Network features
    public static boolean enablePacketCompression = true;
    public static boolean enablePacketBatching = true;
    public static boolean enableDeltaEncoding = false;
    public static boolean enablePacketThrottling = false;

    // Memory features
    public static boolean enableMemoryMonitoring = true;
    public static boolean enableAutoGC = false;
    public static boolean enableCacheCleanup = true;

    // Async features
    public static boolean enableAsyncPathfinding = true;
    public static boolean enableAsyncChunkSend = true;
    public static boolean enableAsyncEntityTracking = false;

    @Override
    public void onLoaded() {
        config.addCommentRegionBased(getBasePath(), """
                Feature toggles for enabling/disabling major systems.
                Use these to quickly turn features on or off.""",
                """
                        功能开关, 用于启用/禁用主要系统.
                        使用这些快速开启或关闭功能.""");

        // Core
        enableObjectPooling = config.getBoolean(getBasePath() + ".object-pooling", enableObjectPooling,
                config.pickStringRegionBased("Enable object pooling for GC reduction.", "启用对象池以减少 GC"));

        enableAdaptiveTick = config.getBoolean(getBasePath() + ".adaptive-tick", enableAdaptiveTick,
                config.pickStringRegionBased("Enable adaptive tick scheduling.", "启用自适应 tick 调度"));

        enableEntityDistanceCulling = config.getBoolean(getBasePath() + ".entity-distance-culling",
                enableEntityDistanceCulling,
                config.pickStringRegionBased("Enable distance-based entity tick reduction.", "启用基于距离的实体 tick 降低"));

        enableChunkPrioritization = config.getBoolean(getBasePath() + ".chunk-prioritization",
                enableChunkPrioritization,
                config.pickStringRegionBased("Enable chunk load prioritization.", "启用区块加载优先级"));

        // Network
        enablePacketCompression = config.getBoolean(getBasePath() + ".packet-compression", enablePacketCompression,
                config.pickStringRegionBased("Enable packet compression.", "启用数据包压缩"));

        enablePacketBatching = config.getBoolean(getBasePath() + ".packet-batching", enablePacketBatching,
                config.pickStringRegionBased("Enable packet batching.", "启用数据包批处理"));

        enableDeltaEncoding = config.getBoolean(getBasePath() + ".delta-encoding", enableDeltaEncoding,
                config.pickStringRegionBased("Enable delta encoding for updates.", "启用增量编码更新"));

        enablePacketThrottling = config.getBoolean(getBasePath() + ".packet-throttling", enablePacketThrottling,
                config.pickStringRegionBased("Enable packet rate limiting.", "启用数据包速率限制"));

        // Memory
        enableMemoryMonitoring = config.getBoolean(getBasePath() + ".memory-monitoring", enableMemoryMonitoring,
                config.pickStringRegionBased("Enable memory usage monitoring.", "启用内存使用监控"));

        enableAutoGC = config.getBoolean(getBasePath() + ".auto-gc", enableAutoGC,
                config.pickStringRegionBased("Enable automatic GC on high memory usage.", "高内存时自动触发 GC"));

        enableCacheCleanup = config.getBoolean(getBasePath() + ".cache-cleanup", enableCacheCleanup,
                config.pickStringRegionBased("Enable periodic cache cleanup.", "启用定期缓存清理"));

        // Async
        enableAsyncPathfinding = config.getBoolean(getBasePath() + ".async-pathfinding", enableAsyncPathfinding,
                config.pickStringRegionBased("Enable async pathfinding.", "启用异步寻路"));

        enableAsyncChunkSend = config.getBoolean(getBasePath() + ".async-chunk-send", enableAsyncChunkSend,
                config.pickStringRegionBased("Enable async chunk sending.", "启用异步区块发送"));

        enableAsyncEntityTracking = config.getBoolean(getBasePath() + ".async-entity-tracking",
                enableAsyncEntityTracking,
                config.pickStringRegionBased("Enable async entity tracking (experimental).", "启用异步实体追踪 (实验性)"));
    }
}
