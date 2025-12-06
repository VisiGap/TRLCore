package org.dreeam.leaf.util.pool;

import net.minecraft.world.phys.AABB;

/**
 * Pooled mutable AABB wrapper for high-performance bounding box calculations.
 * 
 * @author TRLCore Team
 */
public final class MutableAABB {

    private static final ObjectPool<MutableAABB> POOL = new ObjectPool<>(MutableAABB::new, 256);

    public double minX, minY, minZ, maxX, maxY, maxZ;

    public MutableAABB() {
    }

    public static MutableAABB acquire() {
        return POOL.acquire();
    }

    public static MutableAABB acquire(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        return POOL.acquire().set(minX, minY, minZ, maxX, maxY, maxZ);
    }

    public static MutableAABB acquire(AABB aabb) {
        return acquire(aabb.minX, aabb.minY, aabb.minZ, aabb.maxX, aabb.maxY, aabb.maxZ);
    }

    public void release() {
        POOL.release(this);
    }

    public MutableAABB set(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        this.minX = minX;
        this.minY = minY;
        this.minZ = minZ;
        this.maxX = maxX;
        this.maxY = maxY;
        this.maxZ = maxZ;
        return this;
    }

    public MutableAABB expand(double x, double y, double z) {
        minX -= x;
        minY -= y;
        minZ -= z;
        maxX += x;
        maxY += y;
        maxZ += z;
        return this;
    }

    public MutableAABB move(double x, double y, double z) {
        minX += x;
        minY += y;
        minZ += z;
        maxX += x;
        maxY += y;
        maxZ += z;
        return this;
    }

    public boolean intersects(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        return this.minX < maxX && this.maxX > minX
                && this.minY < maxY && this.maxY > minY
                && this.minZ < maxZ && this.maxZ > minZ;
    }

    public boolean intersects(AABB other) {
        return intersects(other.minX, other.minY, other.minZ, other.maxX, other.maxY, other.maxZ);
    }

    public boolean contains(double x, double y, double z) {
        return x >= minX && x < maxX && y >= minY && y < maxY && z >= minZ && z < maxZ;
    }

    public double getCenterX() {
        return (minX + maxX) * 0.5;
    }

    public double getCenterY() {
        return (minY + maxY) * 0.5;
    }

    public double getCenterZ() {
        return (minZ + maxZ) * 0.5;
    }

    public AABB toImmutable() {
        return new AABB(minX, minY, minZ, maxX, maxY, maxZ);
    }
}
