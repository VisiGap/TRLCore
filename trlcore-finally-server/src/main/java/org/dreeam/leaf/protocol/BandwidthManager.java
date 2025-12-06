package org.dreeam.leaf.protocol;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Bandwidth tracker and limiter for network traffic management.
 * 
 * @author TRLCore Team
 */
public final class BandwidthManager {

    private static final AtomicLong totalBytesSent = new AtomicLong(0);
    private static final AtomicLong totalBytesReceived = new AtomicLong(0);
    private static final AtomicLong bytesLastSecondSent = new AtomicLong(0);
    private static final AtomicLong bytesLastSecondReceived = new AtomicLong(0);

    private static volatile long lastSecondTimestamp = System.currentTimeMillis();
    private static volatile long uploadRateBps = 0;
    private static volatile long downloadRateBps = 0;

    // Limits (0 = unlimited)
    private static long maxUploadBps = 0;
    private static long maxDownloadBps = 0;

    /**
     * Records bytes sent.
     */
    public static void recordSent(int bytes) {
        totalBytesSent.addAndGet(bytes);
        bytesLastSecondSent.addAndGet(bytes);
        updateRates();
    }

    /**
     * Records bytes received.
     */
    public static void recordReceived(int bytes) {
        totalBytesReceived.addAndGet(bytes);
        bytesLastSecondReceived.addAndGet(bytes);
        updateRates();
    }

    private static void updateRates() {
        long now = System.currentTimeMillis();
        if (now - lastSecondTimestamp >= 1000) {
            uploadRateBps = bytesLastSecondSent.getAndSet(0);
            downloadRateBps = bytesLastSecondReceived.getAndSet(0);
            lastSecondTimestamp = now;
        }
    }

    /**
     * Gets current upload rate in bytes per second.
     */
    public static long getUploadRateBps() {
        return uploadRateBps;
    }

    /**
     * Gets current download rate in bytes per second.
     */
    public static long getDownloadRateBps() {
        return downloadRateBps;
    }

    /**
     * Gets total bytes sent since start.
     */
    public static long getTotalBytesSent() {
        return totalBytesSent.get();
    }

    /**
     * Gets total bytes received since start.
     */
    public static long getTotalBytesReceived() {
        return totalBytesReceived.get();
    }

    /**
     * Sets maximum upload rate (0 = unlimited).
     */
    public static void setMaxUploadBps(long bps) {
        maxUploadBps = bps;
    }

    /**
     * Sets maximum download rate (0 = unlimited).
     */
    public static void setMaxDownloadBps(long bps) {
        maxDownloadBps = bps;
    }

    /**
     * Checks if upload should be throttled.
     */
    public static boolean shouldThrottleUpload() {
        return maxUploadBps > 0 && uploadRateBps >= maxUploadBps;
    }

    /**
     * Checks if download should be throttled.
     */
    public static boolean shouldThrottleDownload() {
        return maxDownloadBps > 0 && downloadRateBps >= maxDownloadBps;
    }

    /**
     * Gets formatted bandwidth statistics.
     */
    public static String getStats() {
        return String.format("Upload: %s/s, Download: %s/s, Total: ↑%s ↓%s",
                formatBytes(uploadRateBps),
                formatBytes(downloadRateBps),
                formatBytes(totalBytesSent.get()),
                formatBytes(totalBytesReceived.get()));
    }

    private static String formatBytes(long bytes) {
        if (bytes < 1024)
            return bytes + "B";
        if (bytes < 1024 * 1024)
            return String.format("%.1fKB", bytes / 1024.0);
        if (bytes < 1024 * 1024 * 1024)
            return String.format("%.1fMB", bytes / (1024.0 * 1024));
        return String.format("%.2fGB", bytes / (1024.0 * 1024 * 1024));
    }

    /**
     * Resets all statistics.
     */
    public static void reset() {
        totalBytesSent.set(0);
        totalBytesReceived.set(0);
        bytesLastSecondSent.set(0);
        bytesLastSecondReceived.set(0);
        uploadRateBps = 0;
        downloadRateBps = 0;
    }
}
