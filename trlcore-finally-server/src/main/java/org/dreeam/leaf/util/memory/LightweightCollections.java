package org.dreeam.leaf.util.memory;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.longs.LongArrayList;

/**
 * Lightweight primitive collections that grow efficiently and minimize memory.
 * Alternative to boxed collections for hot paths.
 * 
 * @author TRLCore Team
 */
public final class LightweightCollections {

    // ===== Growable Int Array =====

    /**
     * Creates a new growable int array with minimal initial capacity.
     */
    public static IntArrayList newIntList() {
        return new IntArrayList(8);
    }

    public static IntArrayList newIntList(int initialCapacity) {
        return new IntArrayList(initialCapacity);
    }

    /**
     * Trims excess capacity from an int list.
     */
    public static void trimIntList(IntArrayList list) {
        list.trim();
    }

    // ===== Growable Long Array =====

    public static LongArrayList newLongList() {
        return new LongArrayList(8);
    }

    public static LongArrayList newLongList(int initialCapacity) {
        return new LongArrayList(initialCapacity);
    }

    public static void trimLongList(LongArrayList list) {
        list.trim();
    }

    // ===== Compact Byte Buffer =====

    /**
     * Creates a resizable byte buffer with efficient growth.
     */
    public static CompactByteBuffer newByteBuffer() {
        return new CompactByteBuffer(64);
    }

    public static CompactByteBuffer newByteBuffer(int initialCapacity) {
        return new CompactByteBuffer(initialCapacity);
    }

    /**
     * Compact resizable byte buffer.
     */
    public static final class CompactByteBuffer {
        private byte[] data;
        private int size;

        public CompactByteBuffer(int initialCapacity) {
            this.data = new byte[initialCapacity];
            this.size = 0;
        }

        public void add(byte b) {
            ensureCapacity(size + 1);
            data[size++] = b;
        }

        public void addAll(byte[] bytes) {
            ensureCapacity(size + bytes.length);
            System.arraycopy(bytes, 0, data, size, bytes.length);
            size += bytes.length;
        }

        public byte get(int index) {
            return data[index];
        }

        public void set(int index, byte value) {
            data[index] = value;
        }

        public int size() {
            return size;
        }

        public void clear() {
            size = 0;
        }

        public byte[] toArray() {
            byte[] result = new byte[size];
            System.arraycopy(data, 0, result, 0, size);
            return result;
        }

        public void trim() {
            if (data.length > size) {
                byte[] trimmed = new byte[size];
                System.arraycopy(data, 0, trimmed, 0, size);
                data = trimmed;
            }
        }

        private void ensureCapacity(int minCapacity) {
            if (minCapacity > data.length) {
                int newCapacity = Math.max(data.length * 2, minCapacity);
                byte[] newData = new byte[newCapacity];
                System.arraycopy(data, 0, newData, 0, size);
                data = newData;
            }
        }
    }
}
