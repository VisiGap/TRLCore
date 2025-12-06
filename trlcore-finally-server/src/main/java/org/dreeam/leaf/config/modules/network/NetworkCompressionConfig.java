package org.dreeam.leaf.config.modules.network;

import org.dreeam.leaf.config.ConfigModules;
import org.dreeam.leaf.config.EnumConfigCategory;

/**
 * Configuration for network compression settings.
 */
public class NetworkCompressionConfig extends ConfigModules {

    public String getBasePath() {
        return EnumConfigCategory.NETWORK.getBaseKeyName() + ".compression";
    }

    public static boolean enableCompression = true;
    public static int compressionThreshold = 256;
    public static int compressionLevel = 1; // Deflater.BEST_SPEED

    @Override
    public void onLoaded() {
        config.addCommentRegionBased(getBasePath(), """
                Network packet compression settings.
                Compresses large packets to reduce bandwidth usage.""",
                """
                        网络数据包压缩设置.
                        压缩大型数据包以减少带宽使用.""");

        enableCompression = config.getBoolean(getBasePath() + ".enabled", enableCompression,
                config.pickStringRegionBased(
                        "Enable packet compression.",
                        "启用数据包压缩"));

        compressionThreshold = config.getInt(getBasePath() + ".threshold", compressionThreshold,
                config.pickStringRegionBased(
                        "Minimum packet size (bytes) to trigger compression.",
                        "触发压缩的最小数据包大小 (字节)"));

        compressionLevel = config.getInt(getBasePath() + ".level", compressionLevel,
                config.pickStringRegionBased(
                        "Compression level (1=fastest, 9=best compression).",
                        "压缩级别 (1=最快, 9=最佳压缩)"));
    }
}
