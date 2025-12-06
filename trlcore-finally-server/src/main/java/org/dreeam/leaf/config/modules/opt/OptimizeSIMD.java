package org.dreeam.leaf.config.modules.opt;

import org.dreeam.leaf.config.ConfigModules;
import org.dreeam.leaf.config.EnumConfigCategory;
import org.dreeam.leaf.util.simd.VectorMath;

/**
 * Configuration for SIMD vector math optimization.
 */
public class OptimizeSIMD extends ConfigModules {

    public String getBasePath() {
        return EnumConfigCategory.PERF.getBaseKeyName() + ".simd-math";
    }

    public static boolean enabled = true;
    public static boolean useFastInverseSqrt = true;

    @Override
    public void onLoaded() {
        config.addCommentRegionBased(getBasePath(), """
                Use SIMD (Single Instruction Multiple Data) for vector math.
                Accelerates distance calculations and collision detection.
                Requires JVM flag: --add-modules jdk.incubator.vector""",
                """
                        使用 SIMD 加速向量数学运算.
                        加速距离计算和碰撞检测.
                        需要 JVM 参数: --add-modules jdk.incubator.vector""");

        enabled = config.getBoolean(getBasePath() + ".enabled", enabled,
                config.pickStringRegionBased(
                        "Enable SIMD acceleration for vector math. Status: " +
                                (VectorMath.isSIMDAvailable() ? "Available" : "Not available"),
                        "启用 SIMD 向量数学加速. 状态: " +
                                (VectorMath.isSIMDAvailable() ? "可用" : "不可用")));

        useFastInverseSqrt = config.getBoolean(getBasePath() + ".use-fast-inverse-sqrt", useFastInverseSqrt,
                config.pickStringRegionBased(
                        "Use fast inverse square root for normalization (less precise).",
                        "使用快速逆平方根进行归一化 (精度较低)"));
    }
}
