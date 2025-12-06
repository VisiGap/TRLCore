package org.dreeam.leaf.util;

/**
 * Expanded array constants for common empty/default arrays.
 * Avoids allocating new empty arrays repeatedly.
 * 
 * @author TRLCore Team
 */
public final class ArrayConstants {

    private ArrayConstants() {
    }

    // ===== Empty Primitive Arrays =====
    public static final byte[] EMPTY_BYTES = new byte[0];
    public static final short[] EMPTY_SHORTS = new short[0];
    public static final int[] EMPTY_INTS = new int[0];
    public static final long[] EMPTY_LONGS = new long[0];
    public static final float[] EMPTY_FLOATS = new float[0];
    public static final double[] EMPTY_DOUBLES = new double[0];
    public static final boolean[] EMPTY_BOOLEANS = new boolean[0];
    public static final char[] EMPTY_CHARS = new char[0];

    // ===== Empty Object Arrays =====
    public static final Object[] EMPTY_OBJECTS = new Object[0];
    public static final String[] EMPTY_STRINGS = new String[0];
    public static final Class<?>[] EMPTY_CLASSES = new Class<?>[0];

    // ===== Common Default Arrays =====
    public static final int[] ZERO_INT_3 = { 0, 0, 0 };
    public static final double[] ZERO_DOUBLE_3 = { 0.0, 0.0, 0.0 };
    public static final float[] ZERO_FLOAT_3 = { 0.0f, 0.0f, 0.0f };

    // ===== Utility Methods =====

    /**
     * Returns an empty byte array (shared instance).
     */
    public static byte[] emptyBytes() {
        return EMPTY_BYTES;
    }

    /**
     * Returns an empty int array (shared instance).
     */
    public static int[] emptyInts() {
        return EMPTY_INTS;
    }

    /**
     * Returns an empty String array (shared instance).
     */
    public static String[] emptyStrings() {
        return EMPTY_STRINGS;
    }

    /**
     * Checks if array is null or empty.
     */
    public static boolean isEmpty(byte[] array) {
        return array == null || array.length == 0;
    }

    public static boolean isEmpty(int[] array) {
        return array == null || array.length == 0;
    }

    public static boolean isEmpty(Object[] array) {
        return array == null || array.length == 0;
    }

    /**
     * Returns the array or empty array if null.
     */
    public static byte[] nullToEmpty(byte[] array) {
        return array == null ? EMPTY_BYTES : array;
    }

    public static int[] nullToEmpty(int[] array) {
        return array == null ? EMPTY_INTS : array;
    }

    public static String[] nullToEmpty(String[] array) {
        return array == null ? EMPTY_STRINGS : array;
    }
}
