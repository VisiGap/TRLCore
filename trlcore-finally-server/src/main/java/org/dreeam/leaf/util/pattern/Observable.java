package org.dreeam.leaf.util.pattern;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

/**
 * Observable pattern for event notification.
 * Thread-safe implementation using copy-on-write list.
 * 
 * @author TRLCore Team
 */
public class Observable<T> {

    private final CopyOnWriteArrayList<Consumer<T>> observers = new CopyOnWriteArrayList<>();

    /**
     * Adds an observer.
     * 
     * @return A handle to remove the observer
     */
    public Subscription subscribe(Consumer<T> observer) {
        observers.add(observer);
        return () -> observers.remove(observer);
    }

    /**
     * Notifies all observers with a value.
     */
    public void notify(T value) {
        for (Consumer<T> observer : observers) {
            try {
                observer.accept(value);
            } catch (Exception e) {
                // Log but don't break notification chain
                org.dreeam.leaf.util.ErrorHandler.handleWarning("Observer notification", e);
            }
        }
    }

    /**
     * Removes all observers.
     */
    public void clear() {
        observers.clear();
    }

    /**
     * Gets the number of observers.
     */
    public int observerCount() {
        return observers.size();
    }

    /**
     * Handle for unsubscribing an observer.
     */
    @FunctionalInterface
    public interface Subscription {
        void unsubscribe();
    }
}
