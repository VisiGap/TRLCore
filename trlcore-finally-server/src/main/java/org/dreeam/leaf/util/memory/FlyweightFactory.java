package org.dreeam.leaf.util.memory;

import java.lang.ref.SoftReference;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

/**
 * Flyweight factory for immutable objects that can be shared.
 * Uses soft references to allow GC when memory is needed.
 * 
 * @author TRLCore Team
 */
public final class FlyweightFactory<K, V> {

    private final ConcurrentHashMap<K, SoftReference<V>> cache;
    private final Supplier<V> defaultFactory;
    private final int maxSize;

    public FlyweightFactory(int maxSize, Supplier<V> defaultFactory) {
        this.cache = new ConcurrentHashMap<>(maxSize / 2);
        this.defaultFactory = defaultFactory;
        this.maxSize = maxSize;
    }

    public FlyweightFactory(int maxSize) {
        this(maxSize, null);
    }

    /**
     * Gets or creates a flyweight instance for the given key.
     */
    public V get(K key, Supplier<V> factory) {
        SoftReference<V> ref = cache.get(key);
        V value = ref != null ? ref.get() : null;

        if (value == null) {
            value = factory.get();
            if (cache.size() < maxSize) {
                cache.put(key, new SoftReference<>(value));
            }
        }

        return value;
    }

    /**
     * Gets or creates using default factory.
     */
    public V get(K key) {
        if (defaultFactory == null) {
            throw new IllegalStateException("No default factory set");
        }
        return get(key, defaultFactory);
    }

    /**
     * Clears the cache to free memory.
     */
    public void clear() {
        cache.clear();
    }

    /**
     * Gets current cache size.
     */
    public int size() {
        return cache.size();
    }

    /**
     * Removes stale (GC'd) entries from the cache.
     */
    public void cleanStale() {
        cache.entrySet().removeIf(entry -> entry.getValue().get() == null);
    }
}
