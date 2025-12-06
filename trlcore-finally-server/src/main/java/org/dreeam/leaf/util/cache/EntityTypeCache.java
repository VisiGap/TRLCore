package org.dreeam.leaf.util.cache;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Cache for entity type lookups to reduce reflection and registry access
 * overhead.
 * 
 * @author TRLCore Team
 */
public final class EntityTypeCache {

    // Class to EntityType cache using ClassValue for lock-free access
    private static final ClassValue<EntityType<?>> CLASS_TO_TYPE = new ClassValue<>() {
        @Override
        protected EntityType<?> computeValue(Class<?> type) {
            for (EntityType<?> entityType : BuiltInRegistries.ENTITY_TYPE) {
                // This is expensive but cached once per class
                if (entityType.getBaseClass() != null && entityType.getBaseClass().isAssignableFrom(type)) {
                    return entityType;
                }
            }
            return null;
        }
    };

    // ResourceLocation string to EntityType cache
    private static final ConcurrentHashMap<String, EntityType<?>> STRING_TO_TYPE = new ConcurrentHashMap<>(256);

    /**
     * Gets EntityType for an entity class with caching.
     */
    @SuppressWarnings("unchecked")
    public static <T extends Entity> EntityType<T> getTypeForClass(Class<T> entityClass) {
        return (EntityType<T>) CLASS_TO_TYPE.get(entityClass);
    }

    /**
     * Gets EntityType from string ID with caching.
     */
    public static EntityType<?> getTypeFromString(String id) {
        return STRING_TO_TYPE.computeIfAbsent(id, key -> {
            ResourceLocation loc = ResourceLocation.tryParse(key);
            if (loc == null)
                return null;
            return BuiltInRegistries.ENTITY_TYPE.getValue(loc);
        });
    }

    /**
     * Pre-warms the cache with common entity types.
     */
    public static void warmup() {
        // Pre-cache common entity classes
        for (EntityType<?> type : BuiltInRegistries.ENTITY_TYPE) {
            if (type.getBaseClass() != null) {
                CLASS_TO_TYPE.get(type.getBaseClass());
            }
            ResourceLocation key = BuiltInRegistries.ENTITY_TYPE.getKey(type);
            if (key != null) {
                STRING_TO_TYPE.put(key.toString(), type);
            }
        }
    }

    public static void clearCache() {
        STRING_TO_TYPE.clear();
    }
}
