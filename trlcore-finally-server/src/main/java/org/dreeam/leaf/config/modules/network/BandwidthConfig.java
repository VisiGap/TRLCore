package org.dreeam.leaf.config.modules.network;

import org.dreeam.leaf.config.ConfigModules;
import org.dreeam.leaf.config.EnumConfigCategory;

/**
 * Configuration for bandwidth management.
 */
public class BandwidthConfig extends ConfigModules {

    public String getBasePath() {
        return EnumConfigCategory.NETWORK.getBaseKeyName() + ".bandwidth";
    }

    public static boolean enableTracking = true;
    public static long maxUploadKbps = 0; // 0 = unlimited
    public static long maxDownloadKbps = 0;
    public static boolean enableThrottling = false;

    @Override
    public void onLoaded() {
        config.addCommentRegionBased(getBasePath(), """
                Bandwidth management settings.
                Track and optionally limit network bandwidth usage.""",
                """
                        带宽管理设置.
                        跟踪并可选限制网络带宽使用.""");

        enableTracking = config.getBoolean(getBasePath() + ".track-usage", enableTracking,
                config.pickStringRegionBased(
                        "Track bandwidth usage statistics.",
                        "跟踪带宽使用统计"));

        maxUploadKbps = config.getLong(getBasePath() + ".max-upload-kbps", maxUploadKbps,
                config.pickStringRegionBased(
                        "Maximum upload rate in KB/s (0 = unlimited).",
                        "最大上传速率 KB/s (0 = 无限制)"));

        maxDownloadKbps = config.getLong(getBasePath() + ".max-download-kbps", maxDownloadKbps,
                config.pickStringRegionBased(
                        "Maximum download rate in KB/s (0 = unlimited).",
                        "最大下载速率 KB/s (0 = 无限制)"));

        enableThrottling = config.getBoolean(getBasePath() + ".enable-throttling", enableThrottling,
                config.pickStringRegionBased(
                        "Enable bandwidth throttling when limits are reached.",
                        "达到限制时启用带宽节流"));
    }
}
