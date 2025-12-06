package org.dreeam.leaf.util.pool;

import net.minecraft.core.BlockPos;

/**
 * Pooled mutable BlockPos for high-performance position calculations.
 * Thread-local pooling to avoid synchronization overhead.
 * 
 * @author TRLCore Team
 */
public final class MutableBlockPos {

    private static final ObjectPool<MutableBlockPos> POOL = new ObjectPool<>(MutableBlockPos::new, 512);

    public int x, y, z;

    public MutableBlockPos() {
    }

    public static MutableBlockPos acquire() {
        return POOL.acquire();
    }

    public static MutableBlockPos acquire(int x, int y, int z) {
        return POOL.acquire().set(x, y, z);
    }

    public static MutableBlockPos acquire(BlockPos pos) {
        return acquire(pos.getX(), pos.getY(), pos.getZ());
    }

    public void release() {
        POOL.release(this);
    }

    public MutableBlockPos set(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }

    public MutableBlockPos set(BlockPos pos) {
        return set(pos.getX(), pos.getY(), pos.getZ());
    }

    public MutableBlockPos offset(int dx, int dy, int dz) {
        this.x += dx;
        this.y += dy;
        this.z += dz;
        return this;
    }

    public MutableBlockPos above() {
        return offset(0, 1, 0);
    }

    public MutableBlockPos below() {
        return offset(0, -1, 0);
    }

    public MutableBlockPos north() {
        return offset(0, 0, -1);
    }

    public MutableBlockPos south() {
        return offset(0, 0, 1);
    }

    public MutableBlockPos east() {
        return offset(1, 0, 0);
    }

    public MutableBlockPos west() {
        return offset(-1, 0, 0);
    }

    public long asLong() {
        return BlockPos.asLong(x, y, z);
    }

    public BlockPos toImmutable() {
        return new BlockPos(x, y, z);
    }

    public int distManhattan(int x, int y, int z) {
        return Math.abs(this.x - x) + Math.abs(this.y - y) + Math.abs(this.z - z);
    }

    public double distSqr(int x, int y, int z) {
        double dx = this.x - x;
        double dy = this.y - y;
        double dz = this.z - z;
        return dx * dx + dy * dy + dz * dz;
    }

    @Override
    public int hashCode() {
        return (y + z * 31) * 31 + x;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof MutableBlockPos other))
            return false;
        return x == other.x && y == other.y && z == other.z;
    }

    @Override
    public String toString() {
        return "MutableBlockPos[" + x + ", " + y + ", " + z + "]";
    }
}
