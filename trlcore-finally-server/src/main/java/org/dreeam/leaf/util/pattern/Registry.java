package org.dreeam.leaf.util.pattern;

import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

/**
 * Registry pattern for registering and retrieving components by key.
 * Thread-safe and supports lazy initialization.
 * 
 * @author TRLCore Team
 */
public class Registry<K, V> {

    private final ConcurrentHashMap<K, V> entries = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<K, Supplier<V>> factories = new ConcurrentHashMap<>();

    /**
     * Registers a value directly.
     */
    public void register(K key, V value) {
        entries.put(key, value);
    }

    /**
     * Registers a lazy factory for a value.
     */
    public void registerFactory(K key, Supplier<V> factory) {
        factories.put(key, factory);
    }

    /**
     * Gets a registered value or creates from factory.
     */
    public V get(K key) {
        V value = entries.get(key);
        if (value != null) {
            return value;
        }

        Supplier<V> factory = factories.get(key);
        if (factory != null) {
            value = factory.get();
            entries.put(key, value);
            return value;
        }

        return null;
    }

    /**
     * Gets a value or returns default.
     */
    public V getOrDefault(K key, V defaultValue) {
        V value = get(key);
        return value != null ? value : defaultValue;
    }

    /**
     * Checks if a key is registered.
     */
    public boolean contains(K key) {
        return entries.containsKey(key) || factories.containsKey(key);
    }

    /**
     * Removes a registration.
     */
    public V unregister(K key) {
        factories.remove(key);
        return entries.remove(key);
    }

    /**
     * Clears all registrations.
     */
    public void clear() {
        entries.clear();
        factories.clear();
    }

    /**
     * Gets the number of entries.
     */
    public int size() {
        return entries.size() + factories.size();
    }

    /**
     * Gets all registered keys.
     */
    public java.util.Set<K> keys() {
        java.util.Set<K> allKeys = new java.util.HashSet<>(entries.keySet());
        allKeys.addAll(factories.keySet());
        return allKeys;
    }
}
