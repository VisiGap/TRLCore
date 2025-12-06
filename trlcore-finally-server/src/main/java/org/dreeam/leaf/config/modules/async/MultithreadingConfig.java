package org.dreeam.leaf.config.modules.async;

import org.dreeam.leaf.async.MultithreadingManager;
import org.dreeam.leaf.config.ConfigModules;
import org.dreeam.leaf.config.EnumConfigCategory;

/**
 * Configuration for the multi-threading system.
 */
public class MultithreadingConfig extends ConfigModules {

    public String getBasePath() {
        return EnumConfigCategory.ASYNC.getBaseKeyName() + ".multithreading";
    }

    public static boolean enabled = false;
    public static int threadCount = 0; // 0 = auto (CPU cores)
    public static int maxQueueSize = 1024;
    public static boolean useVirtualThreads = false;

    @Override
    public void onLoaded() {
        config.addCommentRegionBased(getBasePath(), """
                Multi-threading configuration.
                Enables parallel processing for various server tasks.
                Can be enabled or disabled at runtime via /leaf reload.""",
                """
                        多线程配置.
                        为各种服务器任务启用并行处理.
                        可在运行时通过 /leaf reload 启用或禁用.""");

        enabled = config.getBoolean(getBasePath() + ".enabled", enabled,
                config.pickStringRegionBased(
                        "Enable multi-threading for async tasks.",
                        "启用多线程异步任务处理"));

        threadCount = config.getInt(getBasePath() + ".thread-count", threadCount,
                config.pickStringRegionBased(
                        "Number of worker threads (0 = auto based on CPU cores).",
                        "工作线程数 (0 = 根据 CPU 核心数自动设置)"));

        maxQueueSize = config.getInt(getBasePath() + ".max-queue-size", maxQueueSize,
                config.pickStringRegionBased(
                        "Maximum pending tasks in queue before blocking.",
                        "队列中最大待处理任务数"));

        useVirtualThreads = config.getBoolean(getBasePath() + ".use-virtual-threads", useVirtualThreads,
                config.pickStringRegionBased(
                        "Use Java 21+ virtual threads instead of platform threads.",
                        "使用 Java 21+ 虚拟线程替代平台线程"));
    }

    @Override
    public void onPostLoaded() {
        // Apply configuration
        if (enabled) {
            int threads = threadCount > 0 ? threadCount : Runtime.getRuntime().availableProcessors();
            MultithreadingManager.enable(threads);
        } else {
            MultithreadingManager.disable();
        }
    }
}
