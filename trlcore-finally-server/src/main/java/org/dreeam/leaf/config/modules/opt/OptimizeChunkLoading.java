package org.dreeam.leaf.config.modules.opt;

import org.dreeam.leaf.config.ConfigModules;
import org.dreeam.leaf.config.EnumConfigCategory;

/**
 * Configuration for chunk loading prioritization.
 */
public class OptimizeChunkLoading extends ConfigModules {

    public String getBasePath() {
        return EnumConfigCategory.PERF.getBaseKeyName() + ".chunk-loading";
    }

    public static boolean prioritizeByDistance = true;
    public static int immediateRadius = 2;
    public static int viewRadius = 8;
    public static int maxImmediateChunksPerTick = 16;
    public static int maxBackgroundChunksPerTick = 2;

    @Override
    public void onLoaded() {
        config.addCommentRegionBased(getBasePath(), """
                Optimizes chunk loading by prioritizing chunks near players.
                Improves perceived world loading speed.""",
                """
                        通过优先加载玩家附近的区块来优化区块加载.
                        改善世界加载的感知速度.""");

        prioritizeByDistance = config.getBoolean(getBasePath() + ".prioritize-by-distance", prioritizeByDistance,
                config.pickStringRegionBased(
                        "Prioritize loading chunks closer to players first.",
                        "优先加载距离玩家更近的区块"));

        immediateRadius = config.getInt(getBasePath() + ".immediate-radius", immediateRadius,
                config.pickStringRegionBased(
                        "Radius in chunks for immediate priority loading.",
                        "立即优先加载的区块半径"));

        viewRadius = config.getInt(getBasePath() + ".view-radius", viewRadius,
                config.pickStringRegionBased(
                        "Radius for view-distance priority loading.",
                        "视距优先加载的区块半径"));

        maxImmediateChunksPerTick = config.getInt(getBasePath() + ".max-immediate-per-tick", maxImmediateChunksPerTick,
                config.pickStringRegionBased(
                        "Max immediate priority chunks to load per tick.",
                        "每 tick 最大立即加载区块数"));

        maxBackgroundChunksPerTick = config.getInt(getBasePath() + ".max-background-per-tick",
                maxBackgroundChunksPerTick,
                config.pickStringRegionBased(
                        "Max background priority chunks to load per tick.",
                        "每 tick 最大后台加载区块数"));
    }
}
