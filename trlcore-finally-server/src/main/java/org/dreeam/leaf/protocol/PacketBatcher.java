package org.dreeam.leaf.protocol;

import io.netty.channel.Channel;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.ServerPlayer;

import java.util.List;

/**
 * Packet batcher for reducing network overhead by combining small packets.
 * 
 * @author TRLCore Team
 */
public final class PacketBatcher {

    private static final int MAX_BATCH_SIZE = 64;
    private static final ThreadLocal<ObjectArrayList<Packet<?>>> BATCH_BUFFER = ThreadLocal
            .withInitial(() -> new ObjectArrayList<>(MAX_BATCH_SIZE));

    /**
     * Batches multiple packets for a player and flushes them efficiently.
     */
    public static void batchAndSend(ServerPlayer player, List<Packet<?>> packets) {
        if (packets.isEmpty())
            return;
        if (packets.size() == 1) {
            player.connection.send(packets.getFirst());
            return;
        }

        Channel channel = player.connection.connection.channel;
        if (channel == null || !channel.isActive())
            return;

        // Use event loop for batch write
        channel.eventLoop().execute(() -> {
            for (int i = 0; i < packets.size(); i++) {
                if (i == packets.size() - 1) {
                    channel.writeAndFlush(packets.get(i));
                } else {
                    channel.write(packets.get(i));
                }
            }
        });
    }

    /**
     * Gets a thread-local batch buffer for collecting packets.
     */
    public static ObjectArrayList<Packet<?>> acquireBatchBuffer() {
        ObjectArrayList<Packet<?>> buffer = BATCH_BUFFER.get();
        buffer.clear();
        return buffer;
    }

    /**
     * Flushes the batch buffer by sending all packets and clearing.
     */
    public static void flushBatch(ServerPlayer player, ObjectArrayList<Packet<?>> batch) {
        if (batch.isEmpty())
            return;

        Channel channel = player.connection.connection.channel;
        if (channel == null || !channel.isActive()) {
            batch.clear();
            return;
        }

        if (batch.size() == 1) {
            player.connection.send(batch.getFirst());
        } else {
            channel.eventLoop().execute(() -> {
                for (int i = 0; i < batch.size(); i++) {
                    if (i == batch.size() - 1) {
                        channel.writeAndFlush(batch.get(i));
                    } else {
                        channel.write(batch.get(i));
                    }
                }
            });
        }
        batch.clear();
    }
}
