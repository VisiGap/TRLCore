package org.dreeam.leaf.config.modules.opt;

import org.dreeam.leaf.config.ConfigModules;
import org.dreeam.leaf.config.EnumConfigCategory;

/**
 * Configuration for entity distance-based tick culling.
 */
public class EntityDistanceCulling extends ConfigModules {

    public String getBasePath() {
        return EnumConfigCategory.PERF.getBaseKeyName() + ".entity-distance-culling";
    }

    public static boolean enabled = true;
    public static int fullTickDistance = 32;
    public static int halfTickDistance = 64;
    public static int quarterTickDistance = 96;
    public static boolean excludeHostileMobs = false;

    @Override
    public void onLoaded() {
        config.addCommentRegionBased(getBasePath(), """
                Reduces entity tick frequency based on distance to nearest player.
                Distant entities are processed less frequently, improving TPS.""",
                """
                        根据距离最近玩家的距离降低实体 tick 频率.
                        远处的实体处理频率更低, 提高 TPS.""");

        enabled = config.getBoolean(getBasePath() + ".enabled", enabled,
                config.pickStringRegionBased(
                        "Enable distance-based entity tick culling.",
                        "启用基于距离的实体 tick 裁剪"));

        fullTickDistance = config.getInt(getBasePath() + ".full-tick-distance", fullTickDistance,
                config.pickStringRegionBased(
                        "Distance in blocks for full tick frequency (every tick).",
                        "完整 tick 频率的距离 (每tick) (方块)"));

        halfTickDistance = config.getInt(getBasePath() + ".half-tick-distance", halfTickDistance,
                config.pickStringRegionBased(
                        "Distance for half tick frequency (every 2 ticks).",
                        "半 tick 频率的距离 (每2tick) (方块)"));

        quarterTickDistance = config.getInt(getBasePath() + ".quarter-tick-distance", quarterTickDistance,
                config.pickStringRegionBased(
                        "Distance for quarter tick frequency (every 4 ticks).",
                        "四分之一 tick 频率的距离 (每4tick) (方块)"));

        excludeHostileMobs = config.getBoolean(getBasePath() + ".exclude-hostile-mobs", excludeHostileMobs,
                config.pickStringRegionBased(
                        "Exclude hostile mobs from distance culling (always full tick).",
                        "排除敌对生物不受距离裁剪影响 (始终完整tick)"));
    }
}
