package org.dreeam.leaf.config.modules.misc;

import org.dreeam.leaf.config.ConfigModules;
import org.dreeam.leaf.config.EnumConfigCategory;

/**
 * Debug and logging configuration.
 */
public class DebugConfig extends ConfigModules {

    public String getBasePath() {
        return EnumConfigCategory.MISC.getBaseKeyName() + ".debug";
    }

    // Logging
    public static boolean verboseLogging = false;
    public static boolean logTickTiming = false;
    public static boolean logChunkOperations = false;
    public static boolean logNetworkStats = false;
    public static boolean logMemoryStats = false;

    // Profiling
    public static boolean enableProfiling = false;
    public static int profilingSampleRate = 100;
    public static boolean profileEntityTicking = false;
    public static boolean profileChunkLoading = false;

    // Debug features
    public static boolean showDebugInfo = false;
    public static boolean dumpStatsOnShutdown = false;
    public static int statsReportIntervalSeconds = 0; // 0 = disabled

    @Override
    public void onLoaded() {
        config.addCommentRegionBased(getBasePath(), """
                Debug and logging settings.
                Enable these for troubleshooting performance issues.""",
                """
                        调试和日志设置.
                        启用这些以排查性能问题.""");

        // Logging
        verboseLogging = config.getBoolean(getBasePath() + ".verbose-logging", verboseLogging,
                config.pickStringRegionBased("Enable verbose logging.", "启用详细日志"));

        logTickTiming = config.getBoolean(getBasePath() + ".log-tick-timing", logTickTiming,
                config.pickStringRegionBased("Log tick timing information.", "记录 tick 时间信息"));

        logChunkOperations = config.getBoolean(getBasePath() + ".log-chunk-operations", logChunkOperations,
                config.pickStringRegionBased("Log chunk load/unload operations.", "记录区块加载/卸载操作"));

        logNetworkStats = config.getBoolean(getBasePath() + ".log-network-stats", logNetworkStats,
                config.pickStringRegionBased("Log network statistics periodically.", "定期记录网络统计"));

        logMemoryStats = config.getBoolean(getBasePath() + ".log-memory-stats", logMemoryStats,
                config.pickStringRegionBased("Log memory usage periodically.", "定期记录内存使用"));

        // Profiling
        enableProfiling = config.getBoolean(getBasePath() + ".enable-profiling", enableProfiling,
                config.pickStringRegionBased("Enable performance profiling.", "启用性能分析"));

        profilingSampleRate = config.getInt(getBasePath() + ".profiling-sample-rate", profilingSampleRate,
                config.pickStringRegionBased("Profiling sample rate (1 in N ticks).", "分析采样率 (每 N tick 采样一次)"));

        profileEntityTicking = config.getBoolean(getBasePath() + ".profile-entity-ticking", profileEntityTicking,
                config.pickStringRegionBased("Profile entity tick performance.", "分析实体 tick 性能"));

        profileChunkLoading = config.getBoolean(getBasePath() + ".profile-chunk-loading", profileChunkLoading,
                config.pickStringRegionBased("Profile chunk loading performance.", "分析区块加载性能"));

        // Debug
        showDebugInfo = config.getBoolean(getBasePath() + ".show-debug-info", showDebugInfo,
                config.pickStringRegionBased("Show debug info in MOTD or player join.", "在 MOTD 或玩家加入时显示调试信息"));

        dumpStatsOnShutdown = config.getBoolean(getBasePath() + ".dump-stats-on-shutdown", dumpStatsOnShutdown,
                config.pickStringRegionBased("Dump statistics on server shutdown.", "服务器关闭时转储统计信息"));

        statsReportIntervalSeconds = config.getInt(getBasePath() + ".stats-report-interval", statsReportIntervalSeconds,
                config.pickStringRegionBased("Interval for stats report (0 = disabled).", "统计报告间隔 (0 = 禁用)"));
    }
}
