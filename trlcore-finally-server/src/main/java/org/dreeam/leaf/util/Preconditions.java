package org.dreeam.leaf.util;

import java.util.Collection;

/**
 * Precondition validation utilities for defensive programming.
 * 
 * @author TRLCore Team
 */
public final class Preconditions {

    private Preconditions() {
    }

    // ===== Null Checks =====

    public static <T> T checkNotNull(T reference, String name) {
        if (reference == null) {
            throw new NullPointerException(name + " cannot be null");
        }
        return reference;
    }

    public static <T> T checkNotNull(T reference) {
        if (reference == null) {
            throw new NullPointerException();
        }
        return reference;
    }

    // ===== String Checks =====

    public static String checkNotEmpty(String value, String name) {
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException(name + " cannot be null or empty");
        }
        return value;
    }

    public static String checkNotBlank(String value, String name) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(name + " cannot be null or blank");
        }
        return value;
    }

    // ===== Collection Checks =====

    public static <T extends Collection<?>> T checkNotEmpty(T collection, String name) {
        if (collection == null || collection.isEmpty()) {
            throw new IllegalArgumentException(name + " cannot be null or empty");
        }
        return collection;
    }

    // ===== Numeric Checks =====

    public static int checkPositive(int value, String name) {
        if (value <= 0) {
            throw new IllegalArgumentException(name + " must be positive, got: " + value);
        }
        return value;
    }

    public static int checkNonNegative(int value, String name) {
        if (value < 0) {
            throw new IllegalArgumentException(name + " must be non-negative, got: " + value);
        }
        return value;
    }

    public static long checkPositive(long value, String name) {
        if (value <= 0) {
            throw new IllegalArgumentException(name + " must be positive, got: " + value);
        }
        return value;
    }

    public static double checkPositive(double value, String name) {
        if (value <= 0) {
            throw new IllegalArgumentException(name + " must be positive, got: " + value);
        }
        return value;
    }

    public static int checkInRange(int value, int min, int max, String name) {
        if (value < min || value > max) {
            throw new IllegalArgumentException(name + " must be in range [" + min + ", " + max + "], got: " + value);
        }
        return value;
    }

    public static double checkInRange(double value, double min, double max, String name) {
        if (value < min || value > max) {
            throw new IllegalArgumentException(name + " must be in range [" + min + ", " + max + "], got: " + value);
        }
        return value;
    }

    // ===== Array Checks =====

    public static <T> T[] checkNotEmpty(T[] array, String name) {
        if (array == null || array.length == 0) {
            throw new IllegalArgumentException(name + " cannot be null or empty");
        }
        return array;
    }

    public static int checkArrayIndex(int index, int length, String name) {
        if (index < 0 || index >= length) {
            throw new IndexOutOfBoundsException(name + " index " + index + " out of bounds for length " + length);
        }
        return index;
    }

    // ===== State Checks =====

    public static void checkState(boolean condition, String message) {
        if (!condition) {
            throw new IllegalStateException(message);
        }
    }

    public static void checkArgument(boolean condition, String message) {
        if (!condition) {
            throw new IllegalArgumentException(message);
        }
    }
}
