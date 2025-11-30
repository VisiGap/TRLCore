// Gale - JettPack - reduce array allocations

package me.titaniumtown;

/**
 * Constants for commonly used empty and singleton arrays.
 * <p>
 * Using these shared array constants instead of creating new arrays (e.g.,
 * {@code new int[0]})
 * reduces memory allocations and GC pressure. This is particularly beneficial
 * in hot paths
 * where arrays are frequently created and discarded.
 * </p>
 * <p>
 * <b>Usage Example:</b>
 * 
 * <pre>
 * // Instead of:
 * return new String[0];
 * 
 * // Use:
 * return ArrayConstants.emptyStringArray;
 * </pre>
 * </p>
 *
 * @author Leaf Development Team
 * @since 1.21.8
 */
public final class ArrayConstants {

    private ArrayConstants() {
    }

    // ========== Primitive Type Empty Arrays ==========

    /** Shared empty byte array */
    public static final byte[] emptyByteArray = new byte[0];

    /** Shared empty short array */
    public static final short[] emptyShortArray = new short[0];

    /** Shared empty int array */
    public static final int[] emptyIntArray = new int[0];

    /** Shared empty long array */
    public static final long[] emptyLongArray = new long[0];

    // ========== Common Object Type Empty Arrays ==========

    /** Shared empty Object array */
    public static final Object[] emptyObjectArray = new Object[0];

    /** Shared empty String array */
    public static final String[] emptyStringArray = new String[0];

    // ========== Minecraft Type Empty Arrays ==========

    /** Shared empty Bukkit Entity array */
    public static final org.bukkit.entity.Entity[] emptyBukkitEntityArray = new org.bukkit.entity.Entity[0];

    /** Shared empty NMS Entity array */
    public static final net.minecraft.world.entity.Entity[] emptyEntityArray = new net.minecraft.world.entity.Entity[0];

    /** Shared empty TagKey array - used for fluid checks optimization */
    public static final net.minecraft.tags.TagKey[] emptyTagKeyArray = new net.minecraft.tags.TagKey[0]; // Leaf -
                                                                                                         // Optimize
                                                                                                         // isEyeInFluid

    // Leaf start - Code quality improvements - additional empty arrays
    /** Shared empty UUID array - commonly used in player/entity management */
    public static final java.util.UUID[] emptyUUIDArray = new java.util.UUID[0];

    /** Shared empty Bukkit Player array - frequently used in player lists */
    public static final org.bukkit.entity.Player[] emptyPlayerArray = new org.bukkit.entity.Player[0];

    /** Shared empty CompoundTag array - used in NBT operations */
    public static final net.minecraft.nbt.CompoundTag[] emptyCompoundTagArray = new net.minecraft.nbt.CompoundTag[0];

    /** Shared empty ItemStack array - used in inventory operations */
    public static final org.bukkit.inventory.ItemStack[] emptyItemStackArray = new org.bukkit.inventory.ItemStack[0];

    /** Shared empty Chunk array - used in chunk management */
    public static final org.bukkit.Chunk[] emptyChunkArray = new org.bukkit.Chunk[0];

    /** Shared empty World array - used in multi-world operations */
    public static final org.bukkit.World[] emptyWorldArray = new org.bukkit.World[0];
    // Leaf end

    // ========== Singleton Arrays ==========

    /**
     * Singleton int array containing only zero - used to avoid repeated allocation
     */
    public static final int[] zeroSingletonIntArray = new int[] { 0 };

    // Leaf start - Code quality improvements - additional singleton arrays
    /** Singleton boolean array containing true - used in flag operations */
    public static final boolean[] trueSingletonBooleanArray = new boolean[] { true };

    /** Singleton boolean array containing false - used in flag operations */
    public static final boolean[] falseSingletonBooleanArray = new boolean[] { false };

    /** Singleton int array containing one - commonly used in counting operations */
    public static final int[] oneSingletonIntArray = new int[] { 1 };
    // Leaf end

    // ========== Commented Out (Future Use) ==========

    // Uncomment if needed - currently conflicts with some patch logic
    // public static final net.minecraft.server.level.ServerLevel[]
    // emptyServerLevelArray = new net.minecraft.server.level.ServerLevel[0];
}
