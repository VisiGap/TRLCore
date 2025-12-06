package org.dreeam.leaf.util.pool;

import net.minecraft.world.phys.Vec3;

/**
 * Pooled mutable Vec3 wrapper for high-performance vector calculations.
 * Unlike immutable Vec3, this can be reused to avoid allocation overhead.
 *
 * @author TRLCore Team
 */
public final class MutableVec3 {

    private static final ObjectPool<MutableVec3> POOL = new ObjectPool<>(MutableVec3::new, 256);

    public double x;
    public double y;
    public double z;

    public MutableVec3() {
        this.x = 0;
        this.y = 0;
        this.z = 0;
    }

    public MutableVec3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Acquires a MutableVec3 from the pool.
     */
    public static MutableVec3 acquire() {
        return POOL.acquire();
    }

    /**
     * Acquires a MutableVec3 from the pool and sets its values.
     */
    public static MutableVec3 acquire(double x, double y, double z) {
        MutableVec3 vec = POOL.acquire();
        vec.set(x, y, z);
        return vec;
    }

    /**
     * Acquires a MutableVec3 from the pool and copies values from Vec3.
     */
    public static MutableVec3 acquire(Vec3 vec) {
        return acquire(vec.x, vec.y, vec.z);
    }

    /**
     * Releases this MutableVec3 back to the pool.
     */
    public void release() {
        POOL.release(this);
    }

    public MutableVec3 set(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }

    public MutableVec3 set(Vec3 vec) {
        return set(vec.x, vec.y, vec.z);
    }

    public MutableVec3 add(double x, double y, double z) {
        this.x += x;
        this.y += y;
        this.z += z;
        return this;
    }

    public MutableVec3 add(Vec3 vec) {
        return add(vec.x, vec.y, vec.z);
    }

    public MutableVec3 subtract(double x, double y, double z) {
        this.x -= x;
        this.y -= y;
        this.z -= z;
        return this;
    }

    public MutableVec3 subtract(Vec3 vec) {
        return subtract(vec.x, vec.y, vec.z);
    }

    public MutableVec3 scale(double factor) {
        this.x *= factor;
        this.y *= factor;
        this.z *= factor;
        return this;
    }

    public MutableVec3 normalize() {
        double length = length();
        if (length < 1.0E-4) {
            this.x = 0;
            this.y = 0;
            this.z = 0;
        } else {
            this.x /= length;
            this.y /= length;
            this.z /= length;
        }
        return this;
    }

    public double dot(double x, double y, double z) {
        return this.x * x + this.y * y + this.z * z;
    }

    public double dot(Vec3 vec) {
        return dot(vec.x, vec.y, vec.z);
    }

    public MutableVec3 cross(double x, double y, double z) {
        double newX = this.y * z - this.z * y;
        double newY = this.z * x - this.x * z;
        double newZ = this.x * y - this.y * x;
        this.x = newX;
        this.y = newY;
        this.z = newZ;
        return this;
    }

    public double lengthSqr() {
        return x * x + y * y + z * z;
    }

    public double length() {
        return Math.sqrt(lengthSqr());
    }

    public double distanceToSqr(double x, double y, double z) {
        double dx = this.x - x;
        double dy = this.y - y;
        double dz = this.z - z;
        return dx * dx + dy * dy + dz * dz;
    }

    public double distanceToSqr(Vec3 vec) {
        return distanceToSqr(vec.x, vec.y, vec.z);
    }

    /**
     * Creates an immutable Vec3 from this mutable vector.
     */
    public Vec3 toImmutable() {
        return new Vec3(x, y, z);
    }

    @Override
    public String toString() {
        return "MutableVec3(" + x + ", " + y + ", " + z + ")";
    }
}
