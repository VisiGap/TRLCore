package org.dreeam.leaf.util.pattern;

import java.util.function.Consumer;

/**
 * Generic builder pattern base class.
 * Extend this for type-safe builder implementations.
 * 
 * @author TRLCore Team
 */
public abstract class Builder<T, B extends Builder<T, B>> {

    /**
     * Builds the final object.
     */
    public abstract T build();

    /**
     * Returns this builder cast to the correct type.
     */
    @SuppressWarnings("unchecked")
    protected B self() {
        return (B) this;
    }

    /**
     * Applies a configuration action to this builder.
     */
    public B apply(Consumer<B> action) {
        action.accept(self());
        return self();
    }

    /**
     * Conditionally applies a configuration.
     */
    public B applyIf(boolean condition, Consumer<B> action) {
        if (condition) {
            action.accept(self());
        }
        return self();
    }

    /**
     * Builds and applies transformation.
     */
    public <R> R buildAndTransform(java.util.function.Function<T, R> transformer) {
        return transformer.apply(build());
    }
}
