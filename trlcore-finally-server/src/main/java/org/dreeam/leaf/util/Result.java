package org.dreeam.leaf.util;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Result type for operations that may fail.
 * Alternative to exceptions for expected failures.
 * 
 * @author TRLCore Team
 */
public sealed interface Result<T> permits Result.Success, Result.Failure {

    /**
     * Creates a success result.
     */
    static <T> Result<T> success(T value) {
        return new Success<>(value);
    }

    /**
     * Creates a failure result.
     */
    static <T> Result<T> failure(Throwable error) {
        return new Failure<>(error);
    }

    /**
     * Creates a failure result with message.
     */
    static <T> Result<T> failure(String message) {
        return new Failure<>(new RuntimeException(message));
    }

    /**
     * Wraps a throwing supplier.
     */
    static <T> Result<T> of(ThrowingSupplier<T> supplier) {
        try {
            return success(supplier.get());
        } catch (Throwable t) {
            return failure(t);
        }
    }

    boolean isSuccess();

    boolean isFailure();

    T getValue();

    Throwable getError();

    /**
     * Maps the value if success.
     */
    default <R> Result<R> map(Function<T, R> mapper) {
        if (isSuccess()) {
            return success(mapper.apply(getValue()));
        }
        return failure(getError());
    }

    /**
     * Flat maps the value if success.
     */
    default <R> Result<R> flatMap(Function<T, Result<R>> mapper) {
        if (isSuccess()) {
            return mapper.apply(getValue());
        }
        return failure(getError());
    }

    /**
     * Gets value or default.
     */
    default T getOrElse(T defaultValue) {
        return isSuccess() ? getValue() : defaultValue;
    }

    /**
     * Executes action if success.
     */
    default Result<T> onSuccess(Consumer<T> action) {
        if (isSuccess()) {
            action.accept(getValue());
        }
        return this;
    }

    /**
     * Executes action if failure.
     */
    default Result<T> onFailure(Consumer<Throwable> action) {
        if (isFailure()) {
            action.accept(getError());
        }
        return this;
    }

    // ===== Implementations =====

    record Success<T>(T value) implements Result<T> {
        @Override
        public boolean isSuccess() {
            return true;
        }

        @Override
        public boolean isFailure() {
            return false;
        }

        @Override
        public T getValue() {
            return value;
        }

        @Override
        public Throwable getError() {
            return null;
        }
    }

    record Failure<T>(Throwable error) implements Result<T> {
        @Override
        public boolean isSuccess() {
            return false;
        }

        @Override
        public boolean isFailure() {
            return true;
        }

        @Override
        public T getValue() {
            return null;
        }

        @Override
        public Throwable getError() {
            return error;
        }
    }

    @FunctionalInterface
    interface ThrowingSupplier<T> {
        T get() throws Throwable;
    }
}
