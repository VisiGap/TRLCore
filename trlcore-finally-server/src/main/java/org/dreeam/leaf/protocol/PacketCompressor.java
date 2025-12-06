package org.dreeam.leaf.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.util.zip.Deflater;
import java.util.zip.Inflater;

/**
 * Packet compression utilities for reducing network bandwidth.
 * Uses zlib compression with configurable levels.
 * 
 * @author TRLCore Team
 */
public final class PacketCompressor {

    private static final int DEFAULT_THRESHOLD = 256; // Only compress packets > 256 bytes
    private static final int COMPRESSION_LEVEL = Deflater.BEST_SPEED; // Fast compression

    private static final ThreadLocal<Deflater> DEFLATER = ThreadLocal
            .withInitial(() -> new Deflater(COMPRESSION_LEVEL));
    private static final ThreadLocal<Inflater> INFLATER = ThreadLocal.withInitial(Inflater::new);
    private static final ThreadLocal<byte[]> COMPRESS_BUFFER = ThreadLocal.withInitial(() -> new byte[8192]);

    private static int compressionThreshold = DEFAULT_THRESHOLD;

    /**
     * Sets the compression threshold.
     */
    public static void setThreshold(int threshold) {
        compressionThreshold = threshold;
    }

    public static int getThreshold() {
        return compressionThreshold;
    }

    /**
     * Compresses data if it exceeds the threshold.
     * Returns null if compression is not beneficial.
     */
    public static byte[] compress(byte[] data) {
        if (data.length < compressionThreshold) {
            return null; // Don't compress small packets
        }

        Deflater deflater = DEFLATER.get();
        deflater.reset();
        deflater.setInput(data);
        deflater.finish();

        byte[] buffer = COMPRESS_BUFFER.get();
        if (buffer.length < data.length) {
            buffer = new byte[data.length];
            COMPRESS_BUFFER.set(buffer);
        }

        int compressedLength = deflater.deflate(buffer);

        // Only use compression if it actually reduces size
        if (compressedLength >= data.length) {
            return null;
        }

        byte[] result = new byte[compressedLength];
        System.arraycopy(buffer, 0, result, 0, compressedLength);
        return result;
    }

    /**
     * Decompresses data.
     */
    public static byte[] decompress(byte[] data, int expectedLength) throws Exception {
        Inflater inflater = INFLATER.get();
        inflater.reset();
        inflater.setInput(data);

        byte[] result = new byte[expectedLength];
        int resultLength = inflater.inflate(result);

        if (resultLength != expectedLength) {
            throw new IllegalStateException("Decompressed length mismatch");
        }

        return result;
    }

    /**
     * Compresses a ByteBuf in-place if beneficial.
     */
    public static ByteBuf compressBuf(ByteBuf input) {
        int readable = input.readableBytes();
        if (readable < compressionThreshold) {
            return input;
        }

        byte[] data = new byte[readable];
        input.getBytes(input.readerIndex(), data);

        byte[] compressed = compress(data);
        if (compressed == null) {
            return input;
        }

        return Unpooled.wrappedBuffer(compressed);
    }

    /**
     * Gets compression ratio for statistics.
     */
    public static double getCompressionRatio(int originalSize, int compressedSize) {
        return 1.0 - ((double) compressedSize / originalSize);
    }
}
