package org.dreeam.leaf.config.modules.opt;

import org.dreeam.leaf.config.ConfigModules;
import org.dreeam.leaf.config.EnumConfigCategory;
import org.dreeam.leaf.util.memory.MemoryOptimizer;

/**
 * Configuration for memory optimization features.
 */
public class MemoryOptimizationConfig extends ConfigModules {

    public String getBasePath() {
        return EnumConfigCategory.PERF.getBaseKeyName() + ".memory";
    }

    public static boolean enableObjectPooling = true;
    public static int pooledListMaxSize = 256;
    public static int pooledMapMaxSize = 1024;
    public static double memoryWarningThreshold = 0.70;
    public static double memoryCriticalThreshold = 0.85;
    public static boolean autoCleanupOnCritical = true;
    public static int cleanupCooldownSeconds = 30;

    @Override
    public void onLoaded() {
        config.addCommentRegionBased(getBasePath(), """
                Memory optimization settings.
                Reduces GC pressure through object pooling and automatic cleanup.""",
                """
                        内存优化设置.
                        通过对象池和自动清理减少 GC 压力.""");

        enableObjectPooling = config.getBoolean(getBasePath() + ".enable-object-pooling", enableObjectPooling,
                config.pickStringRegionBased(
                        "Enable object pooling for Vec3, AABB, BlockPos, lists, etc.",
                        "启用 Vec3, AABB, BlockPos, 列表等的对象池"));

        pooledListMaxSize = config.getInt(getBasePath() + ".pooled-list-max-size", pooledListMaxSize,
                config.pickStringRegionBased(
                        "Max capacity of lists to retain in pool (larger lists are discarded).",
                        "池中保留的列表最大容量 (更大的列表将被丢弃)"));

        pooledMapMaxSize = config.getInt(getBasePath() + ".pooled-map-max-size", pooledMapMaxSize,
                config.pickStringRegionBased(
                        "Max size of maps to retain in pool.",
                        "池中保留的映射最大大小"));

        memoryWarningThreshold = config.getDouble(getBasePath() + ".warning-threshold", memoryWarningThreshold,
                config.pickStringRegionBased(
                        "Heap usage fraction to trigger warning (0.0 - 1.0).",
                        "触发警告的堆使用率 (0.0 - 1.0)"));

        memoryCriticalThreshold = config.getDouble(getBasePath() + ".critical-threshold", memoryCriticalThreshold,
                config.pickStringRegionBased(
                        "Heap usage fraction to trigger critical actions (0.0 - 1.0).",
                        "触发关键操作的堆使用率 (0.0 - 1.0)"));

        autoCleanupOnCritical = config.getBoolean(getBasePath() + ".auto-cleanup", autoCleanupOnCritical,
                config.pickStringRegionBased(
                        "Automatically trigger cleanup when memory is critical.",
                        "内存关键时自动触发清理"));

        cleanupCooldownSeconds = config.getInt(getBasePath() + ".cleanup-cooldown-seconds", cleanupCooldownSeconds,
                config.pickStringRegionBased(
                        "Minimum seconds between automatic cleanups.",
                        "自动清理之间的最小秒数"));
    }

    @Override
    public void onPostLoaded() {
        // Log current memory status
        org.dreeam.leaf.config.LeafConfig.LOGGER.info("Memory Status: {}", MemoryOptimizer.getMemoryStatus());
    }
}
