package org.dreeam.leaf.config.modules.network;

import org.dreeam.leaf.config.ConfigModules;
import org.dreeam.leaf.config.EnumConfigCategory;

/**
 * Configuration for packet throttling.
 */
public class PacketThrottlingConfig extends ConfigModules {

    public String getBasePath() {
        return EnumConfigCategory.NETWORK.getBaseKeyName() + ".throttling";
    }

    public static boolean enableThrottling = false;
    public static int defaultCooldownMs = 50;
    public static int entityUpdateCooldownMs = 50;
    public static int blockUpdateCooldownMs = 25;
    public static int chatCooldownMs = 100;

    @Override
    public void onLoaded() {
        config.addCommentRegionBased(getBasePath(), """
                Packet rate limiting to reduce unnecessary network traffic.
                Limits how often certain packet types can be sent.""",
                """
                        数据包速率限制以减少不必要的网络流量.
                        限制某些数据包类型的发送频率.""");

        enableThrottling = config.getBoolean(getBasePath() + ".enabled", enableThrottling,
                config.pickStringRegionBased(
                        "Enable packet rate limiting.",
                        "启用数据包速率限制"));

        defaultCooldownMs = config.getInt(getBasePath() + ".default-cooldown-ms", defaultCooldownMs,
                config.pickStringRegionBased(
                        "Default cooldown between same packet types (ms).",
                        "相同数据包类型之间的默认冷却时间 (毫秒)"));

        entityUpdateCooldownMs = config.getInt(getBasePath() + ".entity-update-cooldown-ms", entityUpdateCooldownMs,
                config.pickStringRegionBased(
                        "Cooldown for entity update packets (ms).",
                        "实体更新数据包冷却时间 (毫秒)"));

        blockUpdateCooldownMs = config.getInt(getBasePath() + ".block-update-cooldown-ms", blockUpdateCooldownMs,
                config.pickStringRegionBased(
                        "Cooldown for block update packets (ms).",
                        "方块更新数据包冷却时间 (毫秒)"));

        chatCooldownMs = config.getInt(getBasePath() + ".chat-cooldown-ms", chatCooldownMs,
                config.pickStringRegionBased(
                        "Cooldown for chat packets (ms).",
                        "聊天数据包冷却时间 (毫秒)"));
    }
}
