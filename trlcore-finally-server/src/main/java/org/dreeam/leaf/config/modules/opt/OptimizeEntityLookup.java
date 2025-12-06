package org.dreeam.leaf.config.modules.opt;

import org.dreeam.leaf.config.ConfigModules;
import org.dreeam.leaf.config.EnumConfigCategory;

/**
 * Configuration for entity type lookup caching.
 */
public class OptimizeEntityLookup extends ConfigModules {

    public String getBasePath() {
        return EnumConfigCategory.PERF.getBaseKeyName() + ".entity-lookup-cache";
    }

    public static boolean enabled = true;
    public static boolean warmupOnStart = true;

    @Override
    public void onLoaded() {
        config.addCommentRegionBased(getBasePath(), """
                Cache entity type lookups to reduce registry access overhead.
                Uses ClassValue for lock-free class-to-type mapping.""",
                """
                        缓存实体类型查找以减少注册表访问开销.
                        使用 ClassValue 进行无锁类型映射.""");

        enabled = config.getBoolean(getBasePath() + ".enabled", enabled,
                config.pickStringRegionBased(
                        "Enable entity type lookup caching.",
                        "启用实体类型查找缓存"));

        warmupOnStart = config.getBoolean(getBasePath() + ".warmup-on-start", warmupOnStart,
                config.pickStringRegionBased(
                        "Pre-warm cache on server start for all entity types.",
                        "服务器启动时预热所有实体类型缓存"));
    }
}
