package org.dreeam.leaf.async;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Configurable multi-threading executor pool for server tasks.
 * Can be enabled/disabled at runtime with graceful shutdown.
 * 
 * @author TRLCore Team
 */
public final class MultithreadingManager {

    private static volatile boolean enabled = false;
    private static volatile ExecutorService executor = null;
    private static volatile ScheduledExecutorService scheduler = null;

    private static int threadCount = Runtime.getRuntime().availableProcessors();
    private static final AtomicInteger taskCount = new AtomicInteger(0);
    private static final AtomicInteger completedTasks = new AtomicInteger(0);

    private MultithreadingManager() {
    }

    /**
     * Enables multi-threading with specified thread count.
     */
    public static synchronized void enable(int threads) {
        if (enabled)
            return;

        threadCount = threads > 0 ? threads : Runtime.getRuntime().availableProcessors();

        ThreadFactory threadFactory = new ThreadFactory() {
            private final AtomicInteger counter = new AtomicInteger(0);

            @Override
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r, "TRLCore-Worker-" + counter.getAndIncrement());
                t.setDaemon(true);
                t.setPriority(Thread.NORM_PRIORITY - 1);
                return t;
            }
        };

        executor = new ThreadPoolExecutor(
                threadCount / 2, // Core pool size
                threadCount, // Max pool size
                60L, TimeUnit.SECONDS, // Keep alive
                new LinkedBlockingQueue<>(1024),
                threadFactory,
                new ThreadPoolExecutor.CallerRunsPolicy());

        scheduler = Executors.newScheduledThreadPool(2, r -> {
            Thread t = new Thread(r, "TRLCore-Scheduler");
            t.setDaemon(true);
            return t;
        });

        enabled = true;
        org.dreeam.leaf.config.LeafConfig.LOGGER.info("Multi-threading enabled with {} threads", threadCount);
    }

    /**
     * Disables multi-threading with graceful shutdown.
     */
    public static synchronized void disable() {
        if (!enabled)
            return;

        enabled = false;

        if (executor != null) {
            executor.shutdown();
            try {
                if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                    executor.shutdownNow();
                }
            } catch (InterruptedException e) {
                executor.shutdownNow();
            }
            executor = null;
        }

        if (scheduler != null) {
            scheduler.shutdown();
            try {
                if (!scheduler.awaitTermination(2, TimeUnit.SECONDS)) {
                    scheduler.shutdownNow();
                }
            } catch (InterruptedException e) {
                scheduler.shutdownNow();
            }
            scheduler = null;
        }

        org.dreeam.leaf.config.LeafConfig.LOGGER.info("Multi-threading disabled");
    }

    /**
     * Checks if multi-threading is enabled.
     */
    public static boolean isEnabled() {
        return enabled;
    }

    /**
     * Submits a task for async execution.
     * Falls back to synchronous execution if disabled.
     */
    public static void submitAsync(Runnable task) {
        if (enabled && executor != null) {
            taskCount.incrementAndGet();
            executor.submit(() -> {
                try {
                    task.run();
                } finally {
                    completedTasks.incrementAndGet();
                }
            });
        } else {
            task.run();
        }
    }

    /**
     * Submits a task with result.
     */
    public static <T> CompletableFuture<T> submitAsync(Callable<T> task) {
        if (enabled && executor != null) {
            taskCount.incrementAndGet();
            CompletableFuture<T> future = new CompletableFuture<>();
            executor.submit(() -> {
                try {
                    future.complete(task.call());
                } catch (Throwable t) {
                    future.completeExceptionally(t);
                } finally {
                    completedTasks.incrementAndGet();
                }
            });
            return future;
        } else {
            try {
                return CompletableFuture.completedFuture(task.call());
            } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
            }
        }
    }

    /**
     * Schedules a delayed task.
     */
    public static ScheduledFuture<?> scheduleDelayed(Runnable task, long delay, TimeUnit unit) {
        if (enabled && scheduler != null) {
            return scheduler.schedule(task, delay, unit);
        }
        // Fallback: run immediately
        task.run();
        return null;
    }

    /**
     * Schedules a repeating task.
     */
    public static ScheduledFuture<?> scheduleRepeating(Runnable task, long initialDelay, long period, TimeUnit unit) {
        if (enabled && scheduler != null) {
            return scheduler.scheduleAtFixedRate(task, initialDelay, period, unit);
        }
        return null;
    }

    // ===== Statistics =====

    public static int getThreadCount() {
        return threadCount;
    }

    public static int getActiveTaskCount() {
        return taskCount.get() - completedTasks.get();
    }

    public static int getTotalTasksSubmitted() {
        return taskCount.get();
    }

    public static int getCompletedTaskCount() {
        return completedTasks.get();
    }

    public static String getStatus() {
        if (!enabled)
            return "Disabled";
        return String.format("Enabled (%d threads, %d active tasks, %d completed)",
                threadCount, getActiveTaskCount(), completedTasks.get());
    }
}
