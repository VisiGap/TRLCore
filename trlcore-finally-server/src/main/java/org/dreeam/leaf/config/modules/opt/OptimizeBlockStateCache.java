package org.dreeam.leaf.config.modules.opt;

import org.dreeam.leaf.config.ConfigModules;
import org.dreeam.leaf.config.EnumConfigCategory;

/**
 * Configuration for block state caching optimization.
 */
public class OptimizeBlockStateCache extends ConfigModules {

    public String getBasePath() {
        return EnumConfigCategory.PERF.getBaseKeyName() + ".block-state-cache";
    }

    public static boolean enabled = true;
    public static int maxCacheSize = 4096;

    @Override
    public void onLoaded() {
        config.addCommentRegionBased(getBasePath(), """
                Cache frequently accessed BlockState properties.
                Reduces repeated computation for block state lookups.""",
                """
                        缓存频繁访问的方块状态属性.
                        减少重复计算方块状态查询.""");

        enabled = config.getBoolean(getBasePath() + ".enabled", enabled,
                config.pickStringRegionBased(
                        "Enable BlockState property caching.",
                        "启用方块状态属性缓存"));

        maxCacheSize = config.getInt(getBasePath() + ".max-cache-size", maxCacheSize,
                config.pickStringRegionBased(
                        "Maximum number of cached BlockState entries.",
                        "最大缓存方块状态数量"));
    }
}
