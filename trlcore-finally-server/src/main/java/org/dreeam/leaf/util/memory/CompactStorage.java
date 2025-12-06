package org.dreeam.leaf.util.memory;

/**
 * Compact storage utilities for reducing memory footprint.
 * 
 * @author TRLCore Team
 */
public final class CompactStorage {

    // ===== Packed Coordinates =====

    /**
     * Packs 3 shorts into a long (for positions within a chunk).
     */
    public static long packShorts(short x, short y, short z) {
        return ((long) x & 0xFFFF) | (((long) y & 0xFFFF) << 16) | (((long) z & 0xFFFF) << 32);
    }

    public static short unpackX(long packed) {
        return (short) (packed & 0xFFFF);
    }

    public static short unpackY(long packed) {
        return (short) ((packed >> 16) & 0xFFFF);
    }

    public static short unpackZ(long packed) {
        return (short) ((packed >> 32) & 0xFFFF);
    }

    // ===== Packed Chunk Position =====

    /**
     * Packs chunk X and Z into an int (world coordinate chunks).
     */
    public static int packChunkPos(int chunkX, int chunkZ) {
        return (chunkX & 0xFFFF) | ((chunkZ & 0xFFFF) << 16);
    }

    public static int unpackChunkX(int packed) {
        return (short) (packed & 0xFFFF);
    }

    public static int unpackChunkZ(int packed) {
        return (short) ((packed >> 16) & 0xFFFF);
    }

    // ===== Nibble Storage =====

    /**
     * Sets a nibble (4-bit value) in a byte array.
     */
    public static void setNibble(byte[] array, int index, int value) {
        int byteIndex = index >> 1;
        if ((index & 1) == 0) {
            array[byteIndex] = (byte) ((array[byteIndex] & 0xF0) | (value & 0x0F));
        } else {
            array[byteIndex] = (byte) ((array[byteIndex] & 0x0F) | ((value & 0x0F) << 4));
        }
    }

    /**
     * Gets a nibble (4-bit value) from a byte array.
     */
    public static int getNibble(byte[] array, int index) {
        int byteIndex = index >> 1;
        if ((index & 1) == 0) {
            return array[byteIndex] & 0x0F;
        } else {
            return (array[byteIndex] >> 4) & 0x0F;
        }
    }

    // ===== Bit Packing =====

    /**
     * Packs multiple small values into a single int.
     * 
     * @param values       Array of values to pack
     * @param bitsPerValue Bits per value (1-32/count)
     */
    public static int packBits(int[] values, int bitsPerValue) {
        int result = 0;
        int mask = (1 << bitsPerValue) - 1;
        for (int i = 0; i < values.length; i++) {
            result |= (values[i] & mask) << (i * bitsPerValue);
        }
        return result;
    }

    /**
     * Unpacks a value from packed bits.
     */
    public static int unpackBit(int packed, int index, int bitsPerValue) {
        int mask = (1 << bitsPerValue) - 1;
        return (packed >> (index * bitsPerValue)) & mask;
    }

    // ===== Compressed Boolean Array =====

    /**
     * Creates a compressed boolean array (1 bit per boolean).
     */
    public static byte[] compressBooleans(boolean[] booleans) {
        int byteCount = (booleans.length + 7) / 8;
        byte[] result = new byte[byteCount];
        for (int i = 0; i < booleans.length; i++) {
            if (booleans[i]) {
                result[i >> 3] |= (byte) (1 << (i & 7));
            }
        }
        return result;
    }

    /**
     * Gets a boolean from compressed storage.
     */
    public static boolean getCompressedBoolean(byte[] compressed, int index) {
        return (compressed[index >> 3] & (1 << (index & 7))) != 0;
    }

    /**
     * Sets a boolean in compressed storage.
     */
    public static void setCompressedBoolean(byte[] compressed, int index, boolean value) {
        if (value) {
            compressed[index >> 3] |= (byte) (1 << (index & 7));
        } else {
            compressed[index >> 3] &= (byte) ~(1 << (index & 7));
        }
    }
}
