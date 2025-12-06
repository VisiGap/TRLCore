package org.dreeam.leaf.util;

/**
 * String utilities for common operations.
 * 
 * @author TRLCore Team
 */
public final class StringUtils {

    private StringUtils() {
    }

    // ===== Empty/Blank Checks =====

    public static boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }

    public static boolean isNotEmpty(String str) {
        return str != null && !str.isEmpty();
    }

    public static boolean isBlank(String str) {
        return str == null || str.isBlank();
    }

    public static boolean isNotBlank(String str) {
        return str != null && !str.isBlank();
    }

    // ===== Default Values =====

    public static String defaultIfEmpty(String str, String defaultValue) {
        return isEmpty(str) ? defaultValue : str;
    }

    public static String defaultIfBlank(String str, String defaultValue) {
        return isBlank(str) ? defaultValue : str;
    }

    public static String nullToEmpty(String str) {
        return str == null ? "" : str;
    }

    // ===== Truncation =====

    public static String truncate(String str, int maxLength) {
        if (str == null || str.length() <= maxLength) {
            return str;
        }
        return str.substring(0, maxLength);
    }

    public static String truncateWithEllipsis(String str, int maxLength) {
        if (str == null || str.length() <= maxLength) {
            return str;
        }
        if (maxLength <= 3) {
            return str.substring(0, maxLength);
        }
        return str.substring(0, maxLength - 3) + "...";
    }

    // ===== Formatting =====

    public static String capitalize(String str) {
        if (isEmpty(str))
            return str;
        return Character.toUpperCase(str.charAt(0)) + str.substring(1);
    }

    public static String formatBytes(long bytes) {
        if (bytes < 1024)
            return bytes + " B";
        if (bytes < 1024 * 1024)
            return String.format("%.1f KB", bytes / 1024.0);
        if (bytes < 1024 * 1024 * 1024)
            return String.format("%.1f MB", bytes / (1024.0 * 1024));
        return String.format("%.2f GB", bytes / (1024.0 * 1024 * 1024));
    }

    public static String formatDuration(long millis) {
        if (millis < 1000)
            return millis + "ms";
        if (millis < 60000)
            return String.format("%.1fs", millis / 1000.0);
        if (millis < 3600000)
            return String.format("%.1fm", millis / 60000.0);
        return String.format("%.1fh", millis / 3600000.0);
    }

    public static String formatPercent(double value) {
        return String.format("%.1f%%", value * 100);
    }

    // ===== Joining =====

    public static String join(String delimiter, Object... parts) {
        if (parts == null || parts.length == 0)
            return "";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < parts.length; i++) {
            if (i > 0)
                sb.append(delimiter);
            sb.append(parts[i]);
        }
        return sb.toString();
    }
}
