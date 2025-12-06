package org.dreeam.leaf.config.modules.opt;

import org.dreeam.leaf.config.ConfigModules;
import org.dreeam.leaf.config.EnumConfigCategory;

/**
 * Configuration for adaptive tick scheduling.
 */
public class AdaptiveTickConfig extends ConfigModules {

    public String getBasePath() {
        return EnumConfigCategory.PERF.getBaseKeyName() + ".adaptive-tick";
    }

    public static boolean enabled = true;
    public static int targetMspt = 50;
    public static int warningMspt = 45;
    public static int criticalMspt = 55;
    public static int maxSkipFactor = 4;

    @Override
    public void onLoaded() {
        config.addCommentRegionBased(getBasePath(), """
                Adaptive tick scheduling that throttles entity processing during lag.
                Lower priority entities (items, distant mobs) are skipped when TPS drops.""",
                """
                        自适应 tick 调度, 在服务器卡顿时减少低优先级实体的处理.
                        低优先级实体 (物品, 远处生物) 在 TPS 下降时会被跳过.""");

        enabled = config.getBoolean(getBasePath() + ".enabled", enabled,
                config.pickStringRegionBased(
                        "Enable adaptive tick scheduling.",
                        "启用自适应 tick 调度"));

        targetMspt = config.getInt(getBasePath() + ".target-mspt", targetMspt,
                config.pickStringRegionBased(
                        "Target milliseconds per tick (50 = 20 TPS).",
                        "目标每 tick 毫秒数 (50 = 20 TPS)"));

        warningMspt = config.getInt(getBasePath() + ".warning-mspt", warningMspt,
                config.pickStringRegionBased(
                        "MSPT at which to start light throttling.",
                        "开始轻度节流的 MSPT 阈值"));

        criticalMspt = config.getInt(getBasePath() + ".critical-mspt", criticalMspt,
                config.pickStringRegionBased(
                        "MSPT at which to apply heavy throttling.",
                        "应用重度节流的 MSPT 阈值"));

        maxSkipFactor = config.getInt(getBasePath() + ".max-skip-factor", maxSkipFactor,
                config.pickStringRegionBased(
                        "Maximum entity skip factor (4 = skip 3/4 of low priority entities).",
                        "最大实体跳过因子 (4 = 跳过 3/4 的低优先级实体)"));
    }
}
