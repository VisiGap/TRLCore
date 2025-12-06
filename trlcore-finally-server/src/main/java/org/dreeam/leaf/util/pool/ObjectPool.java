package org.dreeam.leaf.util.pool;

import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.function.Supplier;

/**
 * Thread-local object pool to reduce GC pressure by reusing frequently
 * 
 * allocated objects.
 * Each thread has its own pool to avoid synchronization overhead.
 *
 * @param <T> The type of object to pool
 * @author TRLCore Team
 */
public final class ObjectPool<T> {

    private static final int DEFAULT_POOL_SIZE = 64;

    private final ThreadLocal<PooledObjects<T>> threadLocalPool;

    public ObjectPool(Supplier<T> factory, int poolSize) {
        this.threadLocalPool = ThreadLocal.withInitial(() -> new PooledObjects<>(factory, poolSize));
    }

    public ObjectPool(Supplier<T> factory) {
        this(factory, DEFAULT_POOL_SIZE);
    }

    /**
     * Acquires an object from the pool. If the pool is empty, creates a new object.
     */
    public T acquire() {
        return threadLocalPool.get().acquire();
    }

    /**
     * Returns an object to the pool for reuse.
     */
    public void release(T object) {
        threadLocalPool.get().release(object);
    }

    /**
     * Gets a scoped handle that automatically returns the object when closed.
     */
    public PooledHandle<T> scoped() {
        return new PooledHandle<>(this, acquire());
    }

    private static final class PooledObjects<T> {
        private final Object[] pool;
        private final Supplier<T> factory;
        private int size;

        PooledObjects(Supplier<T> factory, int capacity) {
            this.pool = new Object[capacity];
            this.factory = factory;
            this.size = 0;
        }

        @SuppressWarnings("unchecked")
        T acquire() {
            if (size > 0) {
                T obj = (T) pool[--size];
                pool[size] = null;
                return obj;
            }
            return factory.get();
        }

        void release(T object) {
            if (object != null && size < pool.length) {
                pool[size++] = object;
            }
        }
    }

    /**
     * Auto-closeable handle for scoped object usage.
     */
    public static final class PooledHandle<T> implements AutoCloseable {
        private final ObjectPool<T> pool;
        private final T object;
        private boolean released;

        PooledHandle(ObjectPool<T> pool, T object) {
            this.pool = pool;
            this.object = object;
            this.released = false;
        }

        public T get() {
            return object;
        }

        @Override
        public void close() {
            if (!released) {
                pool.release(object);
                released = true;
            }
        }
    }

    // ===== Pre-configured pools for common Minecraft objects =====

    /**
     * Pool for mutable Vec3 objects. Use with caution - caller must reset values.
     */
            ic static final ObjectPo        () -> new double[3], 128);

    /**
     * Pool for mutable AABB arrays (minX, minY, minZ, maxX, maxY, maxZ).
     */
    public static final ObjectPool<double[]> AABB_ARRAY_POOL = new ObjectPool<>(
                () -> new double[6],
    /**
     * Pool for StringBuilder to reduce string allocation.
     */
    public static final ObjectPool<StringBuilder> STRING_BUILDER_POOL = new ObjectPool<>(
            () -> new StringBuilder(256), 32);
            /**
     * Acquires a StringBuilder, clears it, and returns it ready for use.
     */
    public static StringBuilder acquireStringBuilder() {
        StringBuilder sb = STRING_BUILDER_POOL.acquire();
        sb.setLength(0);
        return sb;
    }

    /**
     * Releases a StringBuilder back to the pool.
     */
    public static void releaseStringBuilder(StringBuilder sb) {
        if (sb.capacity() <= 8192) { // Don't pool very large builders
            STRING_BUILDER_POOL.release(sb);
        }
    }
}
