package org.dreeam.leaf.config.modules.opt;

import org.dreeam.leaf.config.ConfigModules;
import org.dreeam.leaf.config.EnumConfigCategory;

/**
 * Server defaults configuration for optimized starting values.
 */
public class OptimizedDefaults extends ConfigModules {

    public String getBasePath() {
        return EnumConfigCategory.PERF.getBaseKeyName() + ".optimized-defaults";
    }

    public static boolean applyOptimizedDefaults = false;
    public static String profile = "balanced"; // low, balanced, high, extreme

    // Profile presets
    public static int viewDistance = 10;
    public static int simulationDistance = 5;
    public static int maxPlayers = 20;
    public static int networkCompressionThreshold = 256;
    public static int entityBroadcastRange = 3;

    @Override
    public void onLoaded() {
        config.addCommentRegionBased(getBasePath(), """
                Apply pre-tuned optimization profiles.
                Profiles: low (resource-saving), balanced, high (performance), extreme""",
                """
                        应用预调优化配置.
                        配置: low (省资源), balanced (平衡), high (高性能), extreme (极限)""");

        applyOptimizedDefaults = config.getBoolean(getBasePath() + ".apply", applyOptimizedDefaults,
                config.pickStringRegionBased("Apply optimized default values.", "应用优化的默认值"));

        profile = config.getString(getBasePath() + ".profile", profile,
                config.pickStringRegionBased("Optimization profile: low, balanced, high, extreme.",
                        "优化配置: low, balanced, high, extreme"));

        // Apply profile settings
        if (applyOptimizedDefaults) {
            applyProfile(profile.toLowerCase());
        }

        // Configurable values
        viewDistance = config.getInt(getBasePath() + ".view-distance", viewDistance,
                config.pickStringRegionBased("Server view distance.", "服务器视距"));

        simulationDistance = config.getInt(getBasePath() + ".simulation-distance", simulationDistance,
                config.pickStringRegionBased("Server simulation distance.", "服务器模拟距离"));

        maxPlayers = config.getInt(getBasePath() + ".max-players", maxPlayers,
                config.pickStringRegionBased("Maximum players.", "最大玩家数"));

        networkCompressionThreshold = config.getInt(getBasePath() + ".network-compression-threshold",
                networkCompressionThreshold,
                config.pickStringRegionBased("Network compression threshold.", "网络压缩阈值"));

        entityBroadcastRange = config.getInt(getBasePath() + ".entity-broadcast-range", entityBroadcastRange,
                config.pickStringRegionBased("Entity broadcast range multiplier.", "实体广播范围倍数"));
    }

    private void applyProfile(String profileName) {
        switch (profileName) {
            case "low" -> {
                viewDistance = 6;
                simulationDistance = 4;
                networkCompressionThreshold = 128;
                entityBroadcastRange = 2;
            }
            case "balanced" -> {
                viewDistance = 10;
                simulationDistance = 5;
                networkCompressionThreshold = 256;
                entityBroadcastRange = 3;
            }
            case "high" -> {
                viewDistance = 14;
                simulationDistance = 8;
                networkCompressionThreshold = 512;
                entityBroadcastRange = 4;
            }
            case "extreme" -> {
                viewDistance = 20;
                simulationDistance = 10;
                networkCompressionThreshold = 1024;
                entityBroadcastRange = 5;
            }
        }
    }
}
