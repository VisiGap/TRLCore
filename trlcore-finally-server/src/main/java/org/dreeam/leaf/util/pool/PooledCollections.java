package org.dreeam.leaf.util.pool;

import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

/**
 * Pooled hash collections for temporary operations.
 * 
 * @author TRLCore Team
 */
public final class PooledCollections {

    private static final int DEFAULT_POOL_SIZE = 32;
    private static final int MAX_RETAINED_SIZE = 1024;

    @SuppressWarnings("rawtypes")
    private static final ObjectPool<Object2ObjectOpenHashMap> MAP_POOL = new ObjectPool<>(
            Object2ObjectOpenHashMap::new, DEFAULT_POOL_SIZE);

    private static final ObjectPool<LongOpenHashSet> LONG_SET_POOL = new ObjectPool<>(
            LongOpenHashSet::new, DEFAULT_POOL_SIZE);

    // ===== Object Map Pool =====

    @SuppressWarnings("unchecked")
    public static <K, V> Object2ObjectOpenHashMap<K, V> acquireMap() {
        Object2ObjectOpenHashMap<K, V> map = MAP_POOL.acquire();
        map.clear();
        return map;
    }

    @SuppressWarnings("unchecked")
    public static <K, V> void releaseMap(Object2ObjectOpenHashMap<K, V> map) {
        if (map == null)
            return;
        map.clear();
        if (map.size() <= MAX_RETAINED_SIZE) {
            MAP_POOL.release(map);
        }
    }

    // ===== Long Set Pool =====

    public static LongOpenHashSet acquireLongSet() {
        LongOpenHashSet set = LONG_SET_POOL.acquire();
        set.clear();
        return set;
    }

    public static void releaseLongSet(LongOpenHashSet set) {
        if (set == null)
            return;
        set.clear();
        if (set.size() <= MAX_RETAINED_SIZE) {
            LONG_SET_POOL.release(set);
        }
    }
}
