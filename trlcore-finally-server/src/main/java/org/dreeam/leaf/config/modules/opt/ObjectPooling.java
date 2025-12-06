package org.dreeam.leaf.config.modules.opt;

import org.dreeam.leaf.config.ConfigModules;
import org.dreeam.leaf.config.EnumConfigCategory;

/**
 * Configuration for object pooling to reduce GC pressure.
 * Pools frequently allocated objects like Vec3, AABB, StringBuilder.
 */
public class ObjectPooling extends ConfigModules {

    public String getBasePath() {
        return EnumConfigCategory.PERF.getBaseKeyName() + ".object-pooling";
    }

    public static boolean enabled = true;
    public static int vec3PoolSize = 256;
    public static int aabbPoolSize = 256;
    public static int stringBuilderPoolSize = 32;

    @Override
    public void onLoaded() {
        config.addCommentRegionBased(getBasePath(), """
                Enable object pooling for frequently allocated objects.
                This reduces garbage collection pressure by reusing objects.
                Objects pooled: Vec3, AABB, StringBuilder""",
                """
                        启用对象池以减少 GC 压力.
                        通过复用对象来减少垃圾回收.
                        池化对象: Vec3, AABB, StringBuilder""");

        enabled = config.getBoolean(getBasePath() + ".enabled", enabled,
                config.pickStringRegionBased(
                        "Enable object pooling to reduce GC pressure.",
                        "启用对象池以减少垃圾回收压力"));

        vec3PoolSize = config.getInt(getBasePath() + ".vec3-pool-size", vec3PoolSize,
                config.pickStringRegionBased(
                        "Pool size for Vec3 objects per thread.",
                        "每线程 Vec3 对象池大小"));

        aabbPoolSize = config.getInt(getBasePath() + ".aabb-pool-size", aabbPoolSize,
                config.pickStringRegionBased(
                        "Pool size for AABB objects per thread.",
                        "每线程 AABB 对象池大小"));

        stringBuilderPoolSize = config.getInt(getBasePath() + ".stringbuilder-pool-size", stringBuilderPoolSize,
                config.pickStringRegionBased(
                        "Pool size for StringBuilder objects per thread.",
                        "每线程 StringBuilder 对象池大小"));
    }
}
