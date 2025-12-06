package org.dreeam.leaf.util.pool;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import java.util.List;

/**
 * Pooled ArrayList for temporary list operations.
 * Avoids creating new lists for common operations like entity collection.
 * 
 * @author TRLCore Team
 */
public final class PooledList<T> {

    private static final int DEFAULT_POOL_SIZE = 64;
    private static final int MAX_RETAINED_CAPACITY = 256;

    @SuppressWarnings("rawtypes")
    private static final ObjectPool<ObjectArrayList> LIST_POOL = new ObjectPool<>(
            () -> new ObjectArrayList<>(16), DEFAULT_POOL_SIZE);

    /**
     * Acquires an empty list from the pool.
     */
    @SuppressWarnings("unchecked")
    public static <T> ObjectArrayList<T> acquire() {
        ObjectArrayList<T> list = LIST_POOL.acquire();
        list.clear();
        return list;
    }

    /**
     * Acquires a list and adds all elements from source.
     */
    public static <T> ObjectArrayList<T> acquire(Iterable<T> source) {
        ObjectArrayList<T> list = acquire();
        for (T item : source) {
            list.add(item);
        }
        return list;
    }

    /**
     * Releases a list back to the pool.
     */
    @SuppressWarnings("unchecked")
    public static <T> void release(ObjectArrayList<T> list) {
        if (list == null)
            return;
        list.clear();
        // Don't pool very large lists to prevent memory bloat
        if (list.elements().length <= MAX_RETAINED_CAPACITY) {
            LIST_POOL.release(list);
        }
    }

    /**
     * Executes an action with a pooled list, automatically releasing after.
     */
    public static <T> void withList(java.util.function.Consumer<ObjectArrayList<T>> action) {
        ObjectArrayList<T> list = acquire();
        try {
            action.accept(list);
        } finally {
            release(list);
        }
    }
}
