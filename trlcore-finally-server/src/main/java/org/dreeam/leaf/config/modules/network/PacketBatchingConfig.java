package org.dreeam.leaf.config.modules.network;

import org.dreeam.leaf.config.ConfigModules;
import org.dreeam.leaf.config.EnumConfigCategory;

/**
 * Configuration for packet batching optimization.
 */
public class PacketBatchingConfig extends ConfigModules {

    public String getBasePath() {
        return EnumConfigCategory.NETWORK.getBaseKeyName() + ".packet-batching";
    }

    public static boolean enabled = true;
    public static int maxBatchSize = 64;
    public static int batchDelayTicks = 1;

    @Override
    public void onLoaded() {
        config.addCommentRegionBased(getBasePath(), """
                Batch multiple small packets together before sending.
                Reduces network overhead and improves throughput.""",
                """
                        在发送前将多个小数据包批量处理.
                        减少网络开销并提高吞吐量.""");

        enabled = config.getBoolean(getBasePath() + ".enabled", enabled,
                config.pickStringRegionBased(
                        "Enable packet batching to reduce network calls.",
                        "启用数据包批处理以减少网络调用"));

        maxBatchSize = config.getInt(getBasePath() + ".max-batch-size", maxBatchSize,
                config.pickStringRegionBased(
                        "Maximum number of packets to batch together.",
                        "批量处理的最大数据包数量"));

        batchDelayTicks = config.getInt(getBasePath() + ".batch-delay-ticks", batchDelayTicks,
                config.pickStringRegionBased(
                        "Ticks to wait before flushing incomplete batches.",
                        "刷新不完整批次前等待的 tick 数"));
    }
}
