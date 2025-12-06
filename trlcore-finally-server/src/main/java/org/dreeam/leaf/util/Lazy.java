package org.dreeam.leaf.util;

import java.util.function.Supplier;

/**
 * Thread-safe lazy initialization holder.
 * 
 * @author TRLCore Team
 */
public final class Lazy<T> {

    private volatile T value;
    private volatile boolean initialized = false;
    private final Supplier<T> supplier;

    private Lazy(Supplier<T> supplier) {
        this.supplier = supplier;
    }

    /**
     * Creates a lazy holder with the given supplier.
     */
    public static <T> Lazy<T> of(Supplier<T> supplier) {
        return new Lazy<>(supplier);
    }

    /**
     * Gets the value, initializing if needed.
     */
    public T get() {
        if (!initialized) {
            synchronized (this) {
                if (!initialized) {
                    value = supplier.get();
                    initialized = true;
                }
            }
        }
        return value;
    }

    /**
     * Checks if already initialized.
     */
    public boolean isInitialized() {
        return initialized;
    }

    /**
     * Gets value if initialized, otherwise returns null.
     */
    public T getIfPresent() {
        return initialized ? value : null;
    }

    /**
     * Resets the lazy holder (allows re-initialization).
     */
    public synchronized void reset() {
        value = null;
        initialized = false;
    }

    /**
     * Gets value or default if not yet initialized.
     */
    public T getOrDefault(T defaultValue) {
        return initialized ? value : defaultValue;
    }
}
